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
package com.excilys.ebi.gatling.jenkins.TargetEnvGraphs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbstractGraphiteGraph implements GraphiteGraphInterface{

    public String getGraphiteGraphForTarget(String host, String target, String vTitle, String title, String yMin, String yMax, Calendar startTime, Calendar endTime) {
        StringBuilder result = new StringBuilder();

        SimpleDateFormat graphiteFormat = new SimpleDateFormat("HH:mm_yyyyMMDD");
        String startTimeString = graphiteFormat.format(startTime.getTime());
        String endTimeString = graphiteFormat.format(endTime.getTime());

        result.append(host);
        result.append("/render?");
        result.append("width=600");
        result.append("&from=").append(startTimeString);
        result.append("&until=").append(endTimeString);
        result.append("&height=400");
        result.append("&lineMode=connected");
        result.append("&target=").append(target);
        result.append("&vtitle=").append(vTitle);
        result.append("&fgcolor=000000");
        result.append("&bgcolor=FFFFFF");
        if(!yMin.equals("")) {
            result.append("&yMin=").append(yMin);
        }
        if(!yMax.equals("")) {
            result.append("&yMax=").append(yMax);
        }
        result.append("&_uniq=0.06565146492917762");
        result.append("&title=").append(title);

        return result.toString();
    }
}
