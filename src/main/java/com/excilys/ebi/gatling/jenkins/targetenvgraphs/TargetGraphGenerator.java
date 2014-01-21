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
package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

import com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite.BuildInfoBasedUrlGenerator;
import hudson.model.AbstractBuild;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TargetGraphGenerator {

    BuildInfoBasedUrlGenerator envPoolUrlGenerator;

    public TargetGraphGenerator() {
        envPoolUrlGenerator =  new BuildInfoBasedUrlGenerator();
    }

    public ArrayList<String> getGraphUrls(AbstractBuild<?, ?> build){
        BuildInfoForTargetEnvGraph criteria = getCriteriaFromBuild(build);
        return envPoolUrlGenerator.getUrlsForCriteria(criteria);
    }

    private BuildInfoForTargetEnvGraph getCriteriaFromBuild(AbstractBuild build){
        BuildInfoForTargetEnvGraph result = new BuildInfoForTargetEnvGraph();

        result.setEnvironmentName(getEnvFromBuild(build));
        result.setPoolName(getPoolFromBuild(build));
        result.setBuildStartTime(build.getTimestamp());
        result.setBuildDuration(build.getDuration());

        return result;
    }


    private String getEnvFromBuild(AbstractBuild build) {
        String projectName = build.getProject().getName();
        Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-.*?$");
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(1).toLowerCase();
        }
        return "";
    }

    private String getPoolFromBuild(AbstractBuild build) {
        String projectName = build.getProject().getName();
        Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-+([^_]+).*?$");
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(2).toLowerCase();
        }
        return "";
    }

}