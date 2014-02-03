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
package com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite;


import com.excilys.ebi.gatling.jenkins.targetenvgraphs.BuildInfoForTargetEnvGraph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class BuildInfoBasedUrlGenerator {

    public BuildInfoBasedUrlGenerator() {}

    GraphiteGraphSettingsBuilder graphiteGraphSettingsBuilder = new GraphiteGraphSettingsBuilder();

    public ArrayList<String> getUrlsForCriteria(BuildInfoForTargetEnvGraph buildInfo) {

        ArrayList<String> urlList = new ArrayList<String>();

        for(GraphiteGraphSettings setting: graphiteGraphSettingsBuilder.getGraphiteGraphSettings(buildInfo) ) {
            urlList.add(getGraphiteGraphForCriteriaGraphSettings(buildInfo, setting));
        }

        return urlList;
    }


    private String getGraphiteGraphForCriteriaGraphSettings(BuildInfoForTargetEnvGraph criteria, GraphiteGraphSettings graphSettings) {
        StringBuilder result = new StringBuilder();

        SimpleDateFormat graphiteFormat = new SimpleDateFormat("HH:mm_yyyyMMdd");
        String startTimeString = graphiteFormat.format(criteria.getGraphStartTime().getTime());
        String endTimeString = graphiteFormat.format(criteria.getGraphEndTime().getTime());

        result.append(graphSettings.getHost());
        result.append("/render?");
        result.append("width=").append(graphSettings.getParameter("width", "600"));
        result.append("&from=").append(startTimeString);
        result.append("&until=").append(endTimeString);
        result.append("&height=").append(graphSettings.getParameter("height", "400"));
        result.append("&lineMode=").append(graphSettings.getParameter("lineMode", "connected"));
        result.append("&target=").append(graphSettings.getTarget());
        result.append("&vtitle=").append(graphSettings.getVerticalTitle());
        result.append("&fgcolor=").append(graphSettings.getParameter("fgcolor", "000000"));
        result.append("&bgcolor=").append(graphSettings.getParameter("bgcolor", "FFFFFF"));

        if(null == graphSettings.getYMin() || !(graphSettings.getYMin().equals(""))) {
            result.append("&yMin=").append(graphSettings.getYMin());
        }
        if(null == graphSettings.getYMax() || !(graphSettings.getYMax().equals(""))) {
            result.append("&yMax=").append(graphSettings.getYMax());
        }
        result.append("&_uniq=0.06565146492917762");
        result.append("&title=").append(graphSettings.getTitle());

        return result.toString();
    }
}
