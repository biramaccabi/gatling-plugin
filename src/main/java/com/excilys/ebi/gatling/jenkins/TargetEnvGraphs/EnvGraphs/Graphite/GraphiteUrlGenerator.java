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
package com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.EnvGraphGenerator;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.GraphCriteria;


public class GraphiteUrlGenerator implements EnvGraphGenerator {

    public GraphiteUrlGenerator() {}

    public ArrayList<String> getUrlsForCriteria(GraphCriteria criteria) {
        ArrayList<String> urlList = new ArrayList<String>();

        GraphiteMetricBuilder myBuilder = new GraphiteMetricBuilder();

        for(GraphiteMetrics metric: myBuilder.getMetrics(criteria) ) {
            urlList.add(getGraphiteGraphForCriteriaMetrics(criteria, metric));
        }

        return urlList;
    }


    public String getGraphiteGraphForCriteriaMetrics(GraphCriteria criteria, GraphiteMetrics metrics) {
        StringBuilder result = new StringBuilder();

        SimpleDateFormat graphiteFormat = new SimpleDateFormat("HH:mm_yyyyMMDD");
        String startTimeString = graphiteFormat.format(criteria.getStartTime().getTime());
        String endTimeString = graphiteFormat.format(criteria.getEndTime().getTime());

        result.append(metrics.getHost());
        result.append("/render?");
        result.append("width=").append(metrics.getParameter("width", "600"));
        result.append("&from=").append(startTimeString);
        result.append("&until=").append(endTimeString);
        result.append("&height=").append(metrics.getParameter("height", "400"));
        result.append("&lineMode=").append(metrics.getParameter("lineMode", "connected"));
        result.append("&target=").append(metrics.getTarget());
        result.append("&vtitle=").append(metrics.getVerticalTitle());
        result.append("&fgcolor=").append(metrics.getParameter("fgcolor", "000000"));
        result.append("&bgcolor=").append(metrics.getParameter("bgcolor", "FFFFFF"));

        if(null == metrics.getYMin() || !(metrics.getYMin().equals(""))) {
            result.append("&yMin=").append(metrics.getYMin());
        }
        if(null == metrics.getYMax() || !(metrics.getYMax().equals(""))) {
            result.append("&yMax=").append(metrics.getYMax());
        }
        result.append("&_uniq=0.06565146492917762");
        result.append("&title=").append(metrics.getTitle());

        return result.toString();
    }
}
