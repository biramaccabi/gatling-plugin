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

import hudson.model.AbstractBuild;

public class ProjectNameParser {

    private static final int ENV_NAME_LOCATION_SFLY = 1;
    private static final int ENV_NAME_LOCATION_TINYPRINTS = 2;
    private static final int POOL_NAME_LOCATION_SFLY = 2;
    private static final int POOL_NAME_LOCATION_TINYPRINTS = 3;

    public String getEnvFromBuild(AbstractBuild build) {
        if (isShutterflyProject(build)) {
            return getEnvFromShutterflyBuild(build);
        } else {
            return getEnvFromTinyPrintsBuild(build);
        }
    }

    public String getPoolFromBuild(AbstractBuild build) {
        if (isShutterflyProject(build)) {
            return getPoolFromShutterflyBuild(build);
        } else {
            return getPoolFromTinyPrintsBuild(build);
        }
    }

    public Brand getBrandFromBuild(AbstractBuild build) {
        if (isShutterflyProject(build)) {
            return Brand.SHUTTERFLY;
        } else {
            return Brand.TINYPRINTS;
        }

    }

    private static String getEnvFromShutterflyBuild(AbstractBuild build) {
        String[] splices = spliceProjectNameByDash(build);
        return splices[ENV_NAME_LOCATION_SFLY];
    }

    private static String getEnvFromTinyPrintsBuild(AbstractBuild build) {
        String[] splices = spliceProjectNameByDash(build);
        return splices[ENV_NAME_LOCATION_TINYPRINTS];
    }

    private static String getPoolFromShutterflyBuild(AbstractBuild build) {
        return getPoolFromBuildByLocation(build, POOL_NAME_LOCATION_SFLY);
    }

    private static String getPoolFromTinyPrintsBuild(AbstractBuild build) {
        return getPoolFromBuildByLocation(build, POOL_NAME_LOCATION_TINYPRINTS);
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
