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

import static com.excilys.ebi.gatling.jenkins.PluginConstants.*;
import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.excilys.ebi.gatling.jenkins.chart.Graph;
import org.apache.commons.lang.text.StrSubstitutor;

public class GatlingProjectAction implements Action {

	private final AbstractProject<?, ?> project;
    private Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-.*?$");

	public GatlingProjectAction(AbstractProject<?, ?> project) {
		this.project = project;
	}

	public String getIconFileName() {
		return ICON_URL;
	}

	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	public String getUrlName() {
		return URL_NAME;
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
        String urlTemplate =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(" +
                "load.summary.${env}.${simName}.${reqName}.ko.percent" +
                ")%2C%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(" +
                "load.summary.${env}.${simName}.${reqName}.all.${assertName}%2C%22${assertDescr}%22" +
                ")" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(" +
                "sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C%22Release%20Branch%20Created%22" +
                ")" +
                "&width=586&height=308&lineMode=connected&from=-1months&" +
                "title=%22${reqName}%20${projName}%22";

        List<String> retVal = new ArrayList<String>();
        for (AbstractBuild<?, ?> build : getProject().getBuilds()) {
            ods("Processing " + build.getNumber());
            GatlingBuildAction gatlingBuildAction = build.getAction(GatlingBuildAction.class);
            if (gatlingBuildAction != null) {
               List<AssertionData> assertionDataList = gatlingBuildAction.getAssertionDataList();
               if(assertionDataList != null){
                   for(AssertionData assertionData : assertionDataList){
                       try{
                           Map<String,String> values = new HashMap<String,String>();
                           ods("Processing " + build.getNumber());
                           ods("Before getEnvFromProjectName(" + assertionData.projectName + ")");
                           String env = getEnvFromProjectName(assertionData.projectName);
                           ods("env=" + env);
                           if((env != null) && (env.compareToIgnoreCase("foxtrot") == 0)){
                               String graphiteAssertionType = getGraphiteAsserionTypeFromGatlingAssertionType(
                                   assertionData.assertionType);
                               ods("graphiteAssertionType=" + graphiteAssertionType);
                               if(graphiteAssertionType != null){
                                   values.put("env",URLEncoder.encode(env,"UTF-8"));
                                   values.put("simName",URLEncoder.encode(assertionData.simulationName,"UTF-8"));
                                   values.put("reqName",URLEncoder.encode(assertionData.requestName,"UTF-8"));
                                   values.put("assertName",URLEncoder.encode(graphiteAssertionType,"UTF-8"));
                                   values.put("assertDescr",URLEncoder.encode(assertionData.assertionType,"UTF-8"));
                                   values.put("projName", URLEncoder.encode(assertionData.projectName,"UTF-8"));
                                   StrSubstitutor sub = new StrSubstitutor(values);
                                   String url = sub.replace(urlTemplate);
                                   ods("url=" + url);
                                   retVal.add(url);
                               }
                           }
                       }catch(UnsupportedEncodingException ex){
                            //TODO log error
                       }
                   }
                   // only build urls from the last set of assertions -Vito
                   return retVal;
               }
            }
        }
        return retVal;
    }

    private void ods(String msg) {
        System.out.println(msg);
    }

    private String getGraphiteAsserionTypeFromGatlingAssertionType(String assertionType) {
        if(assertionType.compareTo("95th percentile response time") == 0){
            return "percentiles95";
        }
        return null;
    }
}
