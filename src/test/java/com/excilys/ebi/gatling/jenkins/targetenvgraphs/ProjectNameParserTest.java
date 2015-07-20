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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ProjectNameParserTest {
    public static final String SFLY_ENVIRONMENT_NAME = "kappa";
    public static final String TINYPRINTS_ENVIRONMENT_NAME = "lnp";

    @Test
    public void testSflyParsing() {

        for (Map.Entry<String, String> stringStringEntry : getSflyProjectToPoolMap().entrySet()) {
            Map.Entry pairs = (Map.Entry) stringStringEntry;
            String projectName = (String) pairs.getKey();
            String expectedPool = (String) pairs.getValue();
            ProjectNameParser projectNameParser = new ProjectNameParser(projectName);

            Assert.assertEquals(expectedPool, projectNameParser.getPool());
            Assert.assertEquals(SFLY_ENVIRONMENT_NAME, projectNameParser.getEnv());
            Assert.assertEquals(Brand.SHUTTERFLY, projectNameParser.getBrand());
        }
    }

    @Test
    public void testTPParsing() {

        for (Map.Entry<String, String> stringStringEntry : getTPProjectToPoolMap().entrySet()) {
            Map.Entry pairs = (Map.Entry) stringStringEntry;
            String projectName = (String) pairs.getKey();
            String expectedPool = (String) pairs.getValue();
            ProjectNameParser projectNameParser = new ProjectNameParser(projectName);

            Assert.assertEquals(Brand.TINYPRINTS, projectNameParser.getBrand());
            Assert.assertEquals(expectedPool, projectNameParser.getPool());
            Assert.assertEquals(TINYPRINTS_ENVIRONMENT_NAME, projectNameParser.getEnv());

        }
    }

    private Map<String, String> getSflyProjectToPoolMap() {
        /*
        currently our naming convention for load test jobs in tre-jenkins is:
         Web_Performance_Tests-${ENV}-${SIMULATION_PACKAGE_AND_NAME}

         We are assuming ( <- bad sign) that the package name begins with the pool name.

         Testing various ways of formatting the package name . _
         */

        Map<String, String> resultMap = new HashMap<String, String>();

        resultMap.put("Web_Performance_Tests-kappa-appserver.home.Some.Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-kappa-appserver_home_Some_Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-kappa-appserver-home-Some-Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-kappa-appserver.home_Some-Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-kappa-appserver_home-Some.Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-kappa-appserver-home.Some_Simulation", "appserver");

        resultMap.put("Web_Performance_Tests-kappa-apiserver.home.Some.Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-kappa-apiserver_home_Some_Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-kappa-apiserver-home-Some-Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-kappa-apiserver.home_Some-Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-kappa-apiserver_home-Some.Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-kappa-apiserver-home.Some_Simulation", "apiserver");

        resultMap.put("Web_Performance_Tests-kappa-wsserver.home.Some.Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-kappa-wsserver_home_Some_Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-kappa-wsserver-home-Some-Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-kappa-wsserver.home_Some-Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-kappa-wsserver_home-Some.Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-kappa-wsserver-home.Some_Simulation", "wsserver");

        resultMap.put("Web_Performance_Tests-kappa-wsserverHomeSomeSimulation", "wsserverhomesomesimulation");
        resultMap.put("Web_Performance_Tests-kappa-fakeserver_home_SomeSimulation", "fakeserver");
        resultMap.put("Web_Performance_Tests-kappa-fakestserverhomeSomeSimulation", "fakestserverhomesomesimulation");

        return resultMap;
    }

    private Map<String, String> getTPProjectToPoolMap() {
        /*
        currently our naming convention for load test jobs in tre-jenkins is:
         Web_Performance_Tests-${ENV}-${SIMULATION_PACKAGE_AND_NAME}

         We are assuming ( <- bad sign) that the package name begins with the pool name.

         Testing various ways of formatting the package name . _
         */

        Map<String, String> resultMap = new HashMap<String, String>();

        resultMap.put("Web_Performance_Tests-TP-lnp-appserver.home.Some.Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-appserver_home_Some_Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-appserver-home-Some-Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-appserver.home_Some-Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-appserver_home-Some.Simulation", "appserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-appserver-home.Some_Simulation", "appserver");

        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver.home.Some.Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver_home_Some_Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver-home-Some-Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver.home_Some-Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver_home-Some.Simulation", "apiserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-apiserver-home.Some_Simulation", "apiserver");

        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver.home.Some.Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver_home_Some_Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver-home-Some-Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver.home_Some-Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver_home-Some.Simulation", "wsserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-wsserver-home.Some_Simulation", "wsserver");

        resultMap.put("Web_Performance_Tests-TP-lnp-wsserverHomeSomeSimulation", "wsserverhomesomesimulation");
        resultMap.put("Web_Performance_Tests-TP-lnp-fakeserver_home_SomeSimulation", "fakeserver");
        resultMap.put("Web_Performance_Tests-TP-lnp-fakestserverhomeSomeSimulation", "fakestserverhomesomesimulation");

        return resultMap;
    }
}
