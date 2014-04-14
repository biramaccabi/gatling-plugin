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
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite.TrendGraphBuilder;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;

import java.util.*;

import static com.excilys.ebi.gatling.jenkins.PluginConstants.*;


public class GatlingProjectAction implements Action {

    private String gatlingReportUrl;
	private final AbstractProject<?, ?> project;

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
               AbstractBuild<?, ?> firstBuild = project.getFirstBuild();
               if(firstBuild == null){
                   return retVal;
               }
               Calendar timestamp = firstBuild.getTimestamp();
               Date time = timestamp.getTime();
               TrendGraphBuilder builder = new TrendGraphBuilder(time);
               for(AssertionData assertionData : assertionDataList){
                   String url = builder.getGraphiteUrlForAssertion(assertionData);
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
}
