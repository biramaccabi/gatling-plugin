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

public class ProjectNameParser {

    private static final int ENV_NAME_LOCATION_SFLY = 1;
    private static final int ENV_NAME_LOCATION_TINYPRINTS = 2;
    private static final int POOL_NAME_LOCATION_SFLY = 2;
    private static final int POOL_NAME_LOCATION_TINYPRINTS = 3;
    private final String projectName;

    public ProjectNameParser(String projectName) {
        this.projectName = projectName;
    }

    public String getEnv() {
        if (isShutterflyProject()) {
            return getEnvFromShutterflyBuild();
        } else {
            return getEnvFromTinyPrintsBuild();
        }
    }

    public String getPool() {
        if (isShutterflyProject()) {
            return getPoolFromShutterflyBuild();
        } else {
            return getPoolFromTinyPrintsBuild();
        }
    }

    public Brand getBrand() {
        if (isShutterflyProject()) {
            return Brand.SHUTTERFLY;
        } else {
            return Brand.TINYPRINTS;
        }

    }

    private String getEnvFromShutterflyBuild() {
        String[] splices = getProjectNameSplitByDash();
        return splices[ENV_NAME_LOCATION_SFLY];
    }

    private String getEnvFromTinyPrintsBuild() {
        String[] splices = getProjectNameSplitByDash();
        return splices[ENV_NAME_LOCATION_TINYPRINTS];
    }

    private String getPoolFromShutterflyBuild() {
        return getPoolFromBuildByLocation(POOL_NAME_LOCATION_SFLY);
    }

    private String getPoolFromTinyPrintsBuild() {
        return getPoolFromBuildByLocation(POOL_NAME_LOCATION_TINYPRINTS);
    }

    private  String getPoolFromBuildByLocation(int location) {
        String[] splices = getProjectNameSplitByDash();
        String longPoolName = splices[location];
        return extractPoolShortNameFromLongName(longPoolName);
    }

    private static String extractPoolShortNameFromLongName(String longPoolName) {
        String shortPoolName;
        String[] splices = longPoolName.split("\\-|_|\\.");
        shortPoolName = splices[0];
        return shortPoolName.toLowerCase();
    }

    private String[] getProjectNameSplitByDash() {
        String projectName = this.projectName;
        return projectName.split("-");
    }

    private boolean isShutterflyProject() {
        String projectName = getEnvFromShutterflyBuild();
        return !projectName.equalsIgnoreCase("TP");
    }
}
