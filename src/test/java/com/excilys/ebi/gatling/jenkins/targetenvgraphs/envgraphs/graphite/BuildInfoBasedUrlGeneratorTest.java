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
package com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite;

import com.excilys.ebi.gatling.jenkins.targetenvgraphs.BuildInfoForTargetEnvGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BuildInfoBasedUrlGeneratorTest {

    public static final String TARGET_HOST_NAME = "http://graphite.internal.shutterfly.com:443/";
    public static final String URL_DATE_FORMAT = "HH:mm_yyyyMMdd";
    @Mock
    GraphiteGraphSettingsBuilder mockedSettingsBuilder;

    Calendar testStartTime = Calendar.getInstance();

    static final int GRAPH_START_BUFFER_TIME_IN_MINUTES = -5;
    static final int GRAPH_END_BUFFER_TIME_IN_MINUTES = 5;
    static final int BUILD_DURATION_IN_MINUTES = 10;

    @Before
    public void setup() {
        mockedSettingsBuilder = mock(GraphiteGraphSettingsBuilder.class);
    }

    @Test
    public void testGetUrlsForCriteriaWithoutYMinYMax() {
        List<String> generatedUrls = getUrlsForGraphiteGraphSetting(getSettingWithoutYMinYMax());

        String expectedURL = TARGET_HOST_NAME + "/render?width=600&from=${fromTime}&until=${untilTime}" +
                "&height=400&lineMode=connected&target=sfly.kappa.host.app.*.aggregation-cpu-" +
                "average.cpu-{user%2C}.value%2Ccolor%28sfly.kappa.host.app.*.aggregation-cpu-average.cpu-idle" +
                "&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_CPU_User_Usage";

        expectedURL = expectedURL.replace("${fromTime}", getStartTimeString());
        expectedURL = expectedURL.replace("${untilTime}", getBuildEndTimeString());

        Assert.assertEquals(1, generatedUrls.size());
        Assert.assertEquals(expectedURL, generatedUrls.get(0));
    }

    @Test
    public void testGetUrlsForCriteriaWithYMinYMax() {

        List<String> generatedUrls = getUrlsForGraphiteGraphSetting(getSettingWithYMinYMax());


        String expectedURL = TARGET_HOST_NAME + "/render?width=600&from=${fromTime}&until=${untilTime}" +
                "&height=400&lineMode=connected&target=sfly.kappa.host.app.*.aggregation-cpu-" +
                "average.cpu-{user%2C}.value%2Ccolor%28sfly.kappa.host.app.*.aggregation-cpu-average.cpu-idle" +
                "&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100" +
                "&_uniq=0.06565146492917762&title=APP_POOL_CPU_User_Usage";

        expectedURL = expectedURL.replace("${fromTime}", getStartTimeString());
        expectedURL = expectedURL.replace("${untilTime}", getBuildEndTimeString());

        Assert.assertEquals(1, generatedUrls.size());
        Assert.assertEquals(expectedURL, generatedUrls.get(0));
    }

    private List<String> getUrlsForGraphiteGraphSetting(GraphiteGraphSettings settings) {
        BuildInfoForTargetEnvGraph testBuildInfoForTargetEnvGraph = new BuildInfoForTargetEnvGraph();
        testBuildInfoForTargetEnvGraph.setBuildDuration(getDuration());
        testBuildInfoForTargetEnvGraph.setBuildStartTime(getBuildStartTime());
        testBuildInfoForTargetEnvGraph.setEnvironmentName("kappa");
        testBuildInfoForTargetEnvGraph.setPoolName("appserver");

        List<GraphiteGraphSettings> settingsList = new ArrayList<GraphiteGraphSettings>();
        settingsList.add(settings);

        when(mockedSettingsBuilder.getGraphiteGraphSettings((BuildInfoForTargetEnvGraph) any())).thenReturn(settingsList);

        BuildInfoBasedUrlGenerator testBuildInfoBasedUrlGenerator = new BuildInfoBasedUrlGenerator();
        testBuildInfoBasedUrlGenerator.graphiteGraphSettingsBuilder = mockedSettingsBuilder;
        return testBuildInfoBasedUrlGenerator.getUrlsForCriteria(testBuildInfoForTargetEnvGraph);
    }


    private Calendar getBuildStartTime() {
        return (Calendar) testStartTime.clone();
    }

    private Calendar getBuildEndTime() {
        Calendar endTime = (Calendar) getBuildStartTime().clone();
        endTime.add(Calendar.MINUTE, BUILD_DURATION_IN_MINUTES);
        return endTime;
    }

    private String getStartTimeString() {
        Calendar startTime = (Calendar) getBuildStartTime().clone();
        startTime.add(Calendar.MINUTE, GRAPH_START_BUFFER_TIME_IN_MINUTES);

        return buildDateTimeStringFromDate(startTime.getTime());
    }

    private String getBuildEndTimeString() {
        Calendar endTime = (Calendar) getBuildEndTime().clone();
        endTime.add(Calendar.MINUTE, GRAPH_END_BUFFER_TIME_IN_MINUTES);

        return buildDateTimeStringFromDate(endTime.getTime());
    }

    private String buildDateTimeStringFromDate(Date date) {
        SimpleDateFormat graphiteFormat = new SimpleDateFormat(URL_DATE_FORMAT);
        return graphiteFormat.format(date);
    }

    private long getDuration() {
        return getBuildEndTime().getTimeInMillis() - getBuildStartTime().getTimeInMillis();
    }

    private GraphiteGraphSettings getSettingWithoutYMinYMax() {
        GraphiteGraphSettings appCpu = new GraphiteGraphSettings();
        appCpu.setHost("http://graphite.internal.shutterfly.com:443/");
        appCpu.setTarget(GraphiteTargetEnum.POOL_CPU_USER_USAGE.getTarget("sfly","kappa","app"));
        appCpu.setYMax("");
        appCpu.setYMin("");
        appCpu.setTitle("APP_POOL_CPU_User_Usage");
        appCpu.setVerticalTitle("CPU_Percent_User_Used");

        return appCpu;
    }

    private GraphiteGraphSettings getSettingWithYMinYMax() {
        GraphiteGraphSettings appCpu = getSettingWithoutYMinYMax();
        appCpu.setYMax("100");
        appCpu.setYMin("0");
        return appCpu;
    }

}
