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
import hudson.model.AbstractProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TargetGraphGeneratorTest {
    public static final String SFLY_ENVIRONMENT_NAME = "foxtrot";
    public static final String TINYPRINTS_ENVIRONMENT_NAME = "lnp";
    public static final String TINYPRINTS_BRAND_NAME = "TP";
    public static final String POOL_NAME = "appserver";
    @Mock
    BuildInfoBasedUrlGenerator buildInfoBasedUrlGenerator;

    @Mock
    AbstractProject mockedSflyProject;

    @Mock
    AbstractBuild mockedSflyBuild;

    @Mock
    AbstractProject mockedTinyPrintsProject;

    @Mock
    AbstractBuild mockedTinyPrintsBuild;

    @Before
    public void setup() {
        buildInfoBasedUrlGenerator = mock(BuildInfoBasedUrlGenerator.class);

        mockedSflyBuild = mock(AbstractBuild.class);
        mockedSflyProject = mock(AbstractProject.class);
        when(mockedSflyProject.getName()).thenReturn("Web_Performance_Tests-"
                + SFLY_ENVIRONMENT_NAME + "-" + POOL_NAME + "_SomeSimulation");
        when(mockedSflyBuild.getProject()).thenReturn(mockedSflyProject);
        when(mockedSflyBuild.getTimestamp()).thenReturn(this.getStartTime());
        when(mockedSflyBuild.getDuration()).thenReturn(this.getDuration());


        mockedTinyPrintsBuild = mock(AbstractBuild.class);
        mockedTinyPrintsProject = mock(AbstractProject.class);
        when(mockedTinyPrintsProject.getName()).thenReturn("Web_Performance_Tests-"
                + TINYPRINTS_BRAND_NAME +"-" +TINYPRINTS_ENVIRONMENT_NAME + "-" + POOL_NAME + "_SomeSimulation");
        when(mockedTinyPrintsBuild.getProject()).thenReturn(mockedTinyPrintsProject);
        when(mockedTinyPrintsBuild.getTimestamp()).thenReturn(this.getStartTime());
        when(mockedTinyPrintsBuild.getDuration()).thenReturn(this.getDuration());

    }

    @Test
    public void testGetSflyGraphUrls() {
        TargetGraphGenerator targetGraphGenerator = new TargetGraphGenerator();
        targetGraphGenerator.envPoolUrlGenerator = buildInfoBasedUrlGenerator;

        targetGraphGenerator.getGraphUrls(mockedSflyBuild);

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

        targetGraphGenerator.getGraphUrls(mockedTinyPrintsBuild);

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
