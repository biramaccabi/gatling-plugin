/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.jenkins;

import com.excilys.ebi.gatling.jenkins.chart.Graph;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.excilys.ebi.gatling.jenkins.PluginConstants.*;


public class GatlingProjectAction implements Action {

    private static final Logger logger = Logger.getLogger(GatlingProjectAction.class.getName());
    public enum GRAPHITE_ASSERT_TYPE {
        percentiles95,
        mean,
        min,
        max,
        stddev,
        throughput,
        ko;

        public static GRAPHITE_ASSERT_TYPE fromGatlingAssertType(String assertionType){
            if(assertionType.contains("95th")){
                return percentiles95;
            } else if(assertionType.contains("mean")){
                return mean;
            } else if(assertionType.contains("KO")){
                return ko;
            } else if(assertionType.contains("min")){
                return min;
            } else if(assertionType.contains("max")){
                return max;
            } else if(assertionType.contains("standard deviation")){
                return stddev;
            } else if(assertionType.contains("requests per second")){
                return throughput;
            }
            throw new IllegalArgumentException("Unexpected gatling type: " + assertionType);
        }
    }

    private String gatlingReportUrl;
	private final AbstractProject<?, ?> project;
    private final Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-.*?$");
    private final String urlTemplate =
        "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
            "target=alias(color(secondYAxis(" +
            "load.summary.${env}.${simName}.${reqName}.ko.percent" +
            ")%2C%22red%22)%2C%22percent%20KOs%22)" +
            "&target=alias(" +
            "load.summary.${env}.${simName}.${reqName}.all.${assertName}%2C%22${assertDescr}%22" +
            ")" +
            "&target=alias(" +
            "load.summary.${env}.${simName}.${reqName}.all.expected.${assertName}%2C%22performance+assert+threshold%22" +
            ")" +
            "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(" +
            "sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C%22Release%20Branch%20Created%22" +
            ")" +
            "&width=586&height=308&lineMode=connected&from=${fromDateTime}&" +
            "title=${reqName}+-+${assertDescr}" +
            "&vtitle=${performanceMetricLabel}&vtitleRight=Percentage_KOs" +
            "&bgcolor=FFFFFF&fgcolor=000000&yMaxRight=100&yMinRight=0";


	public GatlingProjectAction(AbstractProject<?, ?> project, String gatlingReportUrl) {
		this.project = project;
        this.gatlingReportUrl = gatlingReportUrl;
	}

	public String getIconFileName() {
		return ICON_URL;
	}

	public String getDisplayName() {
		return DISPLAY_NAME_REPORTS;
	}

	public String getUrlName() {
		return gatlingReportUrl;
	}

	public AbstractProject<?, ?> getProject() {
		return project;
	}

	public boolean isVisible() {
		for (AbstractBuild<?, ?> build : getProject().getBuilds()) {
			GatlingBuildAction gatlingBuildAction = build.getAction(GatlingBuildAction.class);
			if (gatlingBuildAction != null) {
				return true;
			}
		}
		return false;
	}

	public Graph<Long> getDashboardGraph() {
		return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY_DASHBOARD) {
			@Override
			public Long getValue(RequestReport requestReport) {
				return requestReport.getMeanResponseTime().getTotal();
			}
		};
	}

	public Graph<Long> getMeanResponseTimeGraph() {
		return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
			@Override
			public Long getValue(RequestReport requestReport) {
				return requestReport.getMeanResponseTime().getTotal();
			}
		};
	}

	public Graph<Long> getPercentileResponseTimeGraph() {
		return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
			@Override
			public Long getValue(RequestReport requestReport) {
				return requestReport.getPercentiles95().getTotal();
			}
		};
	}

	public Graph<Long> getRequestKOPercentageGraph() {
		return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
			@Override
			public Long getValue(RequestReport requestReport) {
				return Math.round(requestReport.getNumberOfRequests().getKO() * 100.0 / requestReport.getNumberOfRequests().getTotal());
			}
		};
	}

	public Map<AbstractBuild<?, ?>, List<String>> getReports() {
		Map<AbstractBuild<?, ?>, List<String>> reports = new LinkedHashMap<AbstractBuild<?, ?>, List<String>>();

		for (AbstractBuild<?, ?> build : project.getBuilds()) {
			GatlingBuildAction action = build.getAction(GatlingBuildAction.class);
			if (action != null) {
                List<String> simNames = new ArrayList<String>();
                for (BuildSimulation sim : action.getSimulations()) {
                    simNames.add(sim.getSimulationName());
                }
                reports.put(build, simNames);
            }
		}

		return reports;
	}

	public String getReportURL(int build, String simName) {
		return new StringBuilder().append(build).append("/").append(URL_NAME).append("/report/").append(simName).toString();
	}

    private String getEnvFromProjectName(String projectName) {
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(1).toLowerCase();
        }
        return "";
    }

    public List<String> getGraphiteGraphUrls() {
        for (AbstractBuild<?, ?> build : getProject().getBuilds()) {
            List<String> retVal = getGraphiteUrlsForBuild(build);
            if (retVal != null)
                return retVal;
        }
        return new ArrayList<String>();
    }

    private List<String> getGraphiteUrlsForBuild(AbstractBuild<?, ?> build) {
        List<String> retVal = new ArrayList<String>();
        GatlingBuildAction gatlingBuildAction = build.getAction(GatlingBuildAction.class);
        if (gatlingBuildAction != null) {
           List<AssertionData> assertionDataList = gatlingBuildAction.getAssertionDataList();
           if(assertionDataList != null){
               for(AssertionData assertionData : assertionDataList){
                   String url = getGraphiteUrlForAssertion(assertionData);
                   if(url != null){
                       retVal.add(url);
                   }
               }
               // only build urls from the most recent build that has assertions -Vito
               return retVal;
           }
        }
        return null;
    }

    private String getGraphiteUrlForAssertion(AssertionData assertionData) {
        try{
            Map<String,String> values = new HashMap<String,String>();
            String env = getEnvFromProjectName(assertionData.projectName);
            if(env != null){
                GRAPHITE_ASSERT_TYPE graphiteAssertionType = convertAssertionTypeFromGatlingToGraphite(
                    assertionData.assertionType);
                String performanceMetricLabel = "Response_Time_in_ms";
                if((graphiteAssertionType != null) && (graphiteAssertionType != GRAPHITE_ASSERT_TYPE.ko)){
                    if(graphiteAssertionType == GRAPHITE_ASSERT_TYPE.throughput){
                        performanceMetricLabel = "Requests_per_second";
                    }
                    values.put("env", URLEncoder.encode(env, "UTF-8"));
                    values.put("simName",URLEncoder.encode(graphiteSanitize(assertionData.simulationName),"UTF-8"));
                    values.put("reqName",URLEncoder.encode(
                        graphiteSanitize(gatlingRequestNameToGraphiteRequestName(assertionData.requestName)),"UTF-8"));
                    values.put("assertName",URLEncoder.encode(graphiteAssertionType.name(),"UTF-8"));
                    values.put("assertDescr",URLEncoder.encode(assertionData.assertionType,"UTF-8"));
                    values.put("projName", URLEncoder.encode(assertionData.projectName,"UTF-8"));
                    values.put("performanceMetricLabel", performanceMetricLabel);
                    values.put("fromDateTime", getOldestBuildDateTime());
                    StrSubstitutor sub = new StrSubstitutor(values);
                    return sub.replace(urlTemplate);
                }
            }
        }catch(UnsupportedEncodingException ex){
             throw new RuntimeException(ex);
        }
        return null;
    }

    private String getOldestBuildDateTime() {
        try {
            AbstractBuild oldestBuild = project.getFirstBuild();

            SimpleDateFormat graphiteFormat = new SimpleDateFormat("HH:mm_yyyyMMdd");

            String startTimeString = graphiteFormat.format(oldestBuild.getTimestamp().getTime());

            return startTimeString;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to find date/time of oldest build for project.  Defaulting range to -1 month", e);
            return "-1months";
        }
    }

    private String gatlingRequestNameToGraphiteRequestName(String requestName) {
        if(requestName.compareTo("Global") == 0){
            return "Global_Information";
        }
        return requestName;
    }

    private String graphiteSanitize(String data) {
        return data.replaceAll("[^\\w\\.\\-_]", "_");
    }

    private GRAPHITE_ASSERT_TYPE convertAssertionTypeFromGatlingToGraphite(String assertionType) {
        if(assertionType == null)
           return GRAPHITE_ASSERT_TYPE.fromGatlingAssertType(assertionType);
        try{
           return GRAPHITE_ASSERT_TYPE.fromGatlingAssertType(assertionType);
        }catch(Exception ex){
            logger.log(Level.WARNING, "Failed to convert gatling type " + assertionType + " to graphite type.", ex);
        }
        return null;
    }
}
