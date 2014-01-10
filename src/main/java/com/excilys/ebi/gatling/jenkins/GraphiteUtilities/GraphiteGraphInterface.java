package com.excilys.ebi.gatling.jenkins.GraphiteUtilities;

import java.util.Calendar;

public interface GraphiteGraphInterface {

    public String getGraphiteGraphForTarget(String host, String target, String vTitle, String title, String yMin, String yMax, Calendar startTime, Calendar endTime);
}
