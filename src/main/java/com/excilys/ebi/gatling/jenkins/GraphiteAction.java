package com.excilys.ebi.gatling.jenkins;

import com.excilys.ebi.gatling.jenkins.GraphiteUtilities.GraphiteUTIL;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.util.List;

public class GraphiteAction implements Action {

    AbstractBuild build;
    String url;
    String icon;
    String displayName;

    public GraphiteAction(String urlName, String displayName, String iconFileName, AbstractBuild build) {
        super();
        this.url = urlName;
        this.displayName = displayName;
        this.icon = iconFileName;
        this.build = build;
    }

    public AbstractBuild<?,?> getBuild() {
        return build;
    }

    /*
        This is used, but not by jelly to render the page.  Need to fix that.
     */
    public List<String> getGraphiteUrls() {
        GraphiteUTIL myUtil = new GraphiteUTIL();
        return myUtil.getGraphiteUrls(build);
    }

    public String getIconFileName() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlName() {
        return url;
    }
}