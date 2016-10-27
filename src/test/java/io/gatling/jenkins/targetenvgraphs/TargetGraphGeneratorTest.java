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
import hudson.model.Run;
import hudson.model.AbstractProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import hudson.model.Job;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TargetGraphGeneratorTest {
    public static final String SFLY_ENVIRONMENT_NAME = "kappa";
    public static final String TINYPRINTS_ENVIRONMENT_NAME = "lnp";
    public static final String TINYPRINTS_BRAND_NAME = "TP";
    public static final String POOL_NAME = "appserver";
    @Mock
    BuildInfoBasedUrlGenerator buildInfoBasedUrlGenerator;

    @Mock
    Run mockedSflyRun;

    @Mock
    Run mockedTinyPrintsRun;

    //This is just a placeholder for the chained methods call
    //now required to get the job name
    @Mock
    Job mockedJobSFLY;
    @Mock
    Job mockedJobTP;

    @Before
    public void setup() {
        buildInfoBasedUrlGenerator = mock(BuildInfoBasedUrlGenerator.class);

        mockedSflyRun = mock(Run.class);
        when(mockedSflyRun.getParent()).thenReturn(mockedJobSFLY);
        when(mockedJobSFLY.getName()).thenReturn("Web_Performance_Tests-"
                + SFLY_ENVIRONMENT_NAME + "-" + POOL_NAME + "_SomeSimulation");
        when(mockedSflyRun.getTimestamp()).thenReturn(this.getStartTime());
        when(mockedSflyRun.getDuration()).thenReturn(this.getDuration());


        mockedTinyPrintsRun = mock(Run.class);
        when(mockedTinyPrintsRun.getParent()).thenReturn(mockedJobTP);
        when(mockedJobTP.getName()).thenReturn("Web_Performance_Tests-"
                + TINYPRINTS_BRAND_NAME + "-" + TINYPRINTS_ENVIRONMENT_NAME + "-" + POOL_NAME + "_SomeSimulation");
        when(mockedTinyPrintsRun.getTimestamp()).thenReturn(this.getStartTime());
        when(mockedTinyPrintsRun.getDuration()).thenReturn(this.getDuration());

    }

    @Test
    public void testGetSflyGraphUrls() {
        TargetGraphGenerator targetGraphGenerator = new TargetGraphGenerator();
        targetGraphGenerator.envPoolUrlGenerator = buildInfoBasedUrlGenerator;

        targetGraphGenerator.getGraphUrls(mockedSflyRun);

        BuildInfoForTargetEnvGraph expectedCriteria = new BuildInfoForTargetEnvGraph();
        expectedCriteria.setBuildDuration(this.getDuration());
        expectedCriteria.setEnvironmentName(SFLY_ENVIRONMENT_NAME);
        expectedCriteria.setPoolName(POOL_NAME);
        expectedCriteria.setBrand(Brand.SHUTTERFLY);
        expectedCriteria.setBuildStartTime(this.getStartTime());

        verify(buildInfoBasedUrlGenerator, times(1)).getUrlsForCriteria(expectedCriteria);
    }

   @Test
    public void testGetTinyPrintsGraphUrls() {
        TargetGraphGenerator targetGraphGenerator = new TargetGraphGenerator();
        targetGraphGenerator.envPoolUrlGenerator = buildInfoBasedUrlGenerator;

        targetGraphGenerator.getGraphUrls(mockedTinyPrintsRun);

        BuildInfoForTargetEnvGraph expectedCriteria = new BuildInfoForTargetEnvGraph();
        expectedCriteria.setBuildDuration(this.getDuration());
        expectedCriteria.setEnvironmentName(TINYPRINTS_ENVIRONMENT_NAME);
        expectedCriteria.setPoolName(POOL_NAME);
        expectedCriteria.setBuildStartTime(this.getStartTime());
        expectedCriteria.setBrand(Brand.TINYPRINTS);

        // not called because we are actively blocking TP brand in TargetGraphGenerator
        // verify(buildInfoBasedUrlGenerator, times(1)).getUrlsForCriteria(expectedCriteria);

    }

    private Calendar getStartTime() {
        Calendar startTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:00 am
        startTime.set(2000, Calendar.JANUARY, 1, 8, 0, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }

    private Calendar getEndTime() {
        Calendar endTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:30 am
        endTime.set(2000, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    private long getDuration() {
        return getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis();
    }


}
