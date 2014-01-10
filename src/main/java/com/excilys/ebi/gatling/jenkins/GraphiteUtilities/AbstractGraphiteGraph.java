package com.excilys.ebi.gatling.jenkins.GraphiteUtilities;

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
