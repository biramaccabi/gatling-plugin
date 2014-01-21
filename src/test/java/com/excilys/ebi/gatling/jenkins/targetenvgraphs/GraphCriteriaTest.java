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

import java.util.Calendar;

public class GraphCriteriaTest {


    public static final long TEN_MIN_IN_MS = 10 * 60 * 1000;

    @org.junit.Test
    public void testBuildStartTime() {
        GraphCriteria testCriteria = new GraphCriteria();
        Calendar startTime = Calendar.getInstance();

        testCriteria.setBuildStartTime(startTime);

        Calendar expectedGraphStartTime = (Calendar)startTime.clone();
        expectedGraphStartTime.add(Calendar.MINUTE, GraphCriteria.graphStartBufferTimeMinutes);

        Assert.assertEquals(expectedGraphStartTime, testCriteria.getGraphStartTime());

    }

    @org.junit.Test
    public void testBuildEndTime() {
        GraphCriteria testCriteria = new GraphCriteria();
        Calendar startTime = Calendar.getInstance();


        testCriteria.setBuildDuration(TEN_MIN_IN_MS);

        testCriteria.setBuildStartTime(startTime);

        Calendar expectedEndTime = (Calendar)startTime.clone();
        expectedEndTime.add(Calendar.MINUTE, 10);
        expectedEndTime.add(Calendar.MINUTE, GraphCriteria.graphEndBufferTimeMinutes);


        Assert.assertEquals(expectedEndTime, testCriteria.getGraphEndTime());
    }

    @org.junit.Test
    public void testEquals() {
        GraphCriteria testCriteria = new GraphCriteria();
        Calendar startTime = Calendar.getInstance();
        long duration = 123456;

        testCriteria.setBuildDuration(duration);
        testCriteria.setBuildStartTime(startTime);
        testCriteria.setEnvironmentName("TestEnv");
        testCriteria.setPoolName("SwimmingPool");

        GraphCriteria expectedCriteria = new GraphCriteria();
        expectedCriteria.setBuildDuration(duration);
        expectedCriteria.setBuildStartTime(startTime);
        expectedCriteria.setEnvironmentName("TestEnv");
        expectedCriteria.setPoolName("SwimmingPool");

        Assert.assertTrue(expectedCriteria.equals(testCriteria));
        Assert.assertEquals(expectedCriteria.hashCode(), testCriteria.hashCode());

    }


}
