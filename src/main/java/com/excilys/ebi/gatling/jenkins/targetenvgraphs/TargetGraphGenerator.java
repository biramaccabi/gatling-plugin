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


public class TargetGraphGenerator {

    private static final int env_name_location_sfly = 1;
    private static final int env_name_location_tinyprints = 2;
    private static final int pool_name_location_sfly = 2;
    private static final int pool_name_location_tinyprints = 3;

    BuildInfoBasedUrlGenerator envPoolUrlGenerator;

    public TargetGraphGenerator() {
        envPoolUrlGenerator =  new BuildInfoBasedUrlGenerator();
    }

    public ArrayList<String> getGraphUrls(AbstractBuild<?, ?> build){
        BuildInfoForTargetEnvGraph criteria = getCriteriaFromBuild(build);
        // right here, we will block all calls for TP
        if(criteria.getBrandName().equals(Brand.TINYPRINTS.name)){
            return  new ArrayList<String>();
        }else{
            return envPoolUrlGenerator.getUrlsForCriteria(criteria);
        }
    }

    private BuildInfoForTargetEnvGraph getCriteriaFromBuild(AbstractBuild build){
        BuildInfoForTargetEnvGraph result = new BuildInfoForTargetEnvGraph();

        result.setEnvironmentName(getEnvFromBuild(build));
        result.setPoolName(getPoolFromBuild(build));
        result.setBuildStartTime(build.getTimestamp());
        result.setBuildDuration(build.getDuration());
        result.setBrandName(getBrandFromBuild(build));

        return result;
    }

    static String getEnvFromBuild(AbstractBuild build) {
        if (isShutterflyProject(build)){
            return getEnvFromShutterflyBuild(build);
        } else {
            return getEnvFromTinyPrintsBuild(build);
        }
    }

    static String getPoolFromBuild(AbstractBuild build) {
        if (isShutterflyProject(build)){
            return getPoolFromShutterflyBuild(build);
        } else {
            return getPoolFromTinyPrintsBuild(build);
        }
    }

    static String getBrandFromBuild(AbstractBuild build) {
        if(isShutterflyProject(build)){
            return "sfly";
        } else {
            return "tp";
        }

    }

    private static String getEnvFromShutterflyBuild(AbstractBuild build) {
        String[] splices = spliceProjectNameByDash(build);
        return splices[env_name_location_sfly];
    }

    private static String getEnvFromTinyPrintsBuild(AbstractBuild build) {
        String[] splices = spliceProjectNameByDash(build);
        return splices[env_name_location_tinyprints];
    }

    private static String getPoolFromShutterflyBuild(AbstractBuild build) {
        return getPoolFromBuildByLocation(build, pool_name_location_sfly);
    }

    private static String getPoolFromTinyPrintsBuild(AbstractBuild build) {
        return getPoolFromBuildByLocation(build, pool_name_location_tinyprints);
    }

    private static String getPoolFromBuildByLocation(AbstractBuild build, int location) {
        String[] splices = spliceProjectNameByDash(build);
        String longPoolName = splices[location];
        return extractPoolShortNameFromLongName(longPoolName);
    }

    private static String extractPoolShortNameFromLongName(String longPoolName) {
        String shortPoolName;
        String[] splices = longPoolName.split("\\-|_|\\.");
        shortPoolName = splices[0];
        return shortPoolName.toLowerCase();
    }

    private static String[] spliceProjectNameByDash(AbstractBuild build) {
        String projectName = build.getProject().getName();
        return projectName.split("-");
    }

    private static boolean isShutterflyProject(AbstractBuild build) {
        String projectName = getEnvFromShutterflyBuild(build);
        return !projectName.equalsIgnoreCase("TP");
    }

}