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
package io.gatling.jenkins.targetenvgraphs;

import io.gatling.jenkins.targetenvgraphs.envgraphs.graphite.BuildInfoBasedUrlGenerator;
import hudson.model.AbstractBuild;
import hudson.model.Run;

import java.util.ArrayList;


public class TargetGraphGenerator {
    BuildInfoBasedUrlGenerator envPoolUrlGenerator;

    public TargetGraphGenerator() {
        envPoolUrlGenerator =  new BuildInfoBasedUrlGenerator();
    }

    public ArrayList<String> getGraphUrls(Run<?, ?> run){
        BuildInfoForTargetEnvGraph criteria = getCriteriaFromBuild(run);
        // right here, we will block all calls for TP
        if(criteria.getBrand().equals(Brand.TINYPRINTS)){
            return  new ArrayList<String>();
        }else{
            return envPoolUrlGenerator.getUrlsForCriteria(criteria);
        }
    }

    private BuildInfoForTargetEnvGraph getCriteriaFromBuild(Run<?, ?> run){
        BuildInfoForTargetEnvGraph result = new BuildInfoForTargetEnvGraph();

        ProjectNameParser projectNameParser = new ProjectNameParser(run.getParent().getName());

        result.setEnvironmentName(projectNameParser.getEnv());
        result.setPoolName(projectNameParser.getPool());
        result.setBuildStartTime(run.getTimestamp());
        result.setBuildDuration(run.getDuration());
        result.setBrand(projectNameParser.getBrand());

        return result;
    }

}