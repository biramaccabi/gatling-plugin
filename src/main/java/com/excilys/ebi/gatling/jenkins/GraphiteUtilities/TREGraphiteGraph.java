package com.excilys.ebi.gatling.jenkins.GraphiteUtilities;

import java.util.Calendar;

public class TREGraphiteGraph extends AbstractGraphiteGraph implements GraphiteGraphInterface {

    String host = "http://tre-stats.internal.shutterfly.com";

    public String getGraphiteGraphForTargetNoYMinYMax(String target, String vTitle, String title, Calendar startTime, Calendar endTime) {
        return getGraphiteGraphForTarget(target, vTitle, title, "", "", startTime, endTime);
    }

    public String getGraphiteGraphForTarget(String target, String vTitle, String title, String yMin, String yMax, Calendar startTime, Calendar endTime) {
        return super.getGraphiteGraphForTarget(this.host, target, vTitle, title, yMin, yMax, startTime, endTime);
    }

}
