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
package io.gatling.jenkins;

import io.gatling.jenkins.targetenvgraphs.TargetGraphGenerator;
import hudson.model.Run;
import hudson.model.Action;
import java.util.List;

public class TargetEnvGraphAction implements Action {

    Run run;
    String url;
    String icon;
    String displayName;

    public TargetEnvGraphAction(String urlName, String displayName, String iconFileName, Run run) {
        super();
        this.url = urlName;
        this.displayName = displayName;
        this.icon = iconFileName;
        this.run = run;
    }

    public Run<?,?> getrun() {
        return run;
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

    public List<String> getGraphiteUrls() {
        TargetGraphGenerator myUtil = new TargetGraphGenerator();
        return myUtil.getGraphUrls(run);
    }

    public String getTargetEnvGraphMessage() {
        return getGraphiteUrls().isEmpty()
                ? "No Graphs available for this simulation."
                : "";
    }
}