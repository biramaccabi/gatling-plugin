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
import io.gatling.jenkins.targetenvgraphs.envgraphs.graphite.GrafanaUrl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphCriteriaBasedUrlGeneratorTest {

    @Mock
    BuildInfoForTargetEnvGraph supportedSflyEnvKappaCriteria;
    @Mock
    BuildInfoForTargetEnvGraph supportedSflyEnvProdCriteria;
    @Mock
    BuildInfoForTargetEnvGraph unsupportedSflyEnvPoolCriteria;
    @Mock
    BuildInfoForTargetEnvGraph unsupportedTPEnvPoolCriteria;

    @Before
    public void setup() {
        supportedSflyEnvKappaCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(supportedSflyEnvKappaCriteria.getEnvironmentName()).thenReturn("kappa");
        when(supportedSflyEnvKappaCriteria.getPoolName()).thenReturn("appserver");
        when(supportedSflyEnvKappaCriteria.getBrandName()).thenReturn("sfly");
        when(supportedSflyEnvKappaCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(supportedSflyEnvKappaCriteria.getGraphStartTime()).thenReturn(getStartTime());

        supportedSflyEnvProdCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(supportedSflyEnvProdCriteria.getEnvironmentName()).thenReturn("prod");
        when(supportedSflyEnvProdCriteria.getPoolName()).thenReturn("appserver");
        when(supportedSflyEnvProdCriteria.getBrandName()).thenReturn("sfly");
        when(supportedSflyEnvProdCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(supportedSflyEnvProdCriteria.getGraphStartTime()).thenReturn(getStartTime());

        unsupportedSflyEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(unsupportedSflyEnvPoolCriteria.getEnvironmentName()).thenReturn("fakeenv");
        when(unsupportedSflyEnvPoolCriteria.getPoolName()).thenReturn("fakeserver");
        when(unsupportedSflyEnvPoolCriteria.getBrandName()).thenReturn("sfly");
        when(unsupportedSflyEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(unsupportedSflyEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());

        unsupportedTPEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(unsupportedTPEnvPoolCriteria.getEnvironmentName()).thenReturn("fakeenv");
        when(unsupportedTPEnvPoolCriteria.getPoolName()).thenReturn("fakeserver");
        when(unsupportedTPEnvPoolCriteria.getBrandName()).thenReturn("tp");
        when(unsupportedTPEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(unsupportedTPEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());
    }

    @Test
    public void testGetGraphUrlsForSupportedSflyKappaPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<GrafanaUrl> graphUrls = testGenerator.getUrlsForCriteria(supportedSflyEnvKappaCriteria);

        String sizeAssertString = "expected URLS and actual number not the same";
        int expectedGraphUrlCount = 1;
        Assert.assertEquals(sizeAssertString, expectedGraphUrlCount, graphUrls.size());

        ArrayList<GrafanaUrl> expectedUrls = getListAppUrls("sfly","kappa","app");

        for(GrafanaUrl url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }

        for(GrafanaUrl expectedURl : expectedUrls) {
            Assert.assertTrue(graphUrls.contains(expectedURl));
        }
    }

    @Test
    public void testGetGraphUrlsForSupportedSflyProdPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<GrafanaUrl> graphUrls = testGenerator.getUrlsForCriteria(supportedSflyEnvProdCriteria);

        String sizeAssertString = "expected URLS and actual number not the same";
        int expectedGraphUrlCount = 1;
        Assert.assertEquals(sizeAssertString, expectedGraphUrlCount, graphUrls.size());

        ArrayList<GrafanaUrl> expectedUrls= getListAppUrls("sfly","prod","app");

        for(GrafanaUrl url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }

        for(GrafanaUrl expectedURl: expectedUrls) {
            Assert.assertTrue(graphUrls.contains(expectedURl));
        }
    }


    @Test
    public void testGetGraphUrlsForUnsupportedSflyEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<GrafanaUrl> graphUrls = testGenerator.getUrlsForCriteria(unsupportedSflyEnvPoolCriteria);

        Assert.assertEquals("should come back with 0, when looking for an unsupported env/pool combo", 0, graphUrls.size());
    }

    @Test
    public void testGetGraphUrlsForUnsupportedTPEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<GrafanaUrl> graphUrls = testGenerator.getUrlsForCriteria(unsupportedTPEnvPoolCriteria);

        Assert.assertEquals("should come back with 0, when looking for an unsupported env/pool combo", 0, graphUrls.size());
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
        // set date to Jan 1, 2000 at 8:45 am
        endTime.set(2000, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    private ArrayList<GrafanaUrl> getListAppUrls(String brand, String env, String pool) {
        String sflyGrafanaUrl="https://grafana.internal.shutterfly.com/dashboard/db/sfly-servers?var-env="+env+"&var-pool="+pool+"&from=20000101T080000&to=20000101T084500";
        String sflyGrafanaName = "Grafana Dashboard for "+env+" "+pool+ " pool";

        ArrayList<GrafanaUrl> list = new ArrayList<GrafanaUrl>();
        GrafanaUrl temp = new GrafanaUrl(sflyGrafanaUrl, sflyGrafanaName);
        list.add(temp);

        return list;
    }


}
