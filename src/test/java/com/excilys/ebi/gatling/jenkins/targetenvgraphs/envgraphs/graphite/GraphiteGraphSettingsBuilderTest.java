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
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.ServerPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphiteGraphSettingsBuilderTest {

    public static final String FOXTROT = "foxtrot";
    public static final String SUPPORTED_POOL = "appserver";
    public static final String UNSUPPORTED_POOL = "nonsenseserver";
    public static final int expectedNumOfGraphs = 9;

    @Test
    public void testGetDefinedSettings() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setEnvironmentName(FOXTROT);
        inputCriteria.setPoolName(SUPPORTED_POOL);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());

        List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

        Assert.assertEquals(expectedNumOfGraphs, generatedSettings.size());

        ArrayList<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings(FOXTROT, SUPPORTED_POOL);
        for(GraphiteGraphSettings generatedSetting: generatedSettings) {
            Assert.assertTrue(expectedSettings.contains(generatedSetting));
        }
    }

    @Test
    public void testGetUndefinedSettings() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        String env = FOXTROT;
        String pool = UNSUPPORTED_POOL;

        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setEnvironmentName(env);
        inputCriteria.setPoolName(pool);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());

        List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

        Assert.assertEquals(0, generatedSettings.size());
    }

    private ArrayList<GraphiteGraphSettings> getListGraphiteSettings(String env, String pool) {
        ArrayList<GraphiteGraphSettings> expectedSettings = new ArrayList<GraphiteGraphSettings>();

        String poolShortName = ServerPool.getEnumForPoolName(pool).shortName;

        GraphiteGraphSettings gcMarkSweepHeapSetting = new GraphiteGraphSettings();
        gcMarkSweepHeapSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeapSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSetting.setTitle("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSetting.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeapSetting.setYMax("100");
        gcMarkSweepHeapSetting.setYMin("0");

        GraphiteGraphSettings gcMarkSweepTimeSetting = new GraphiteGraphSettings();
        gcMarkSweepTimeSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTimeSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSetting.setTitle("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSetting.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTimeSetting.setYMax("");
        gcMarkSweepTimeSetting.setYMin("");


        GraphiteGraphSettings gcParNewHeapSetting= new GraphiteGraphSettings();
        gcParNewHeapSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeapSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSetting.setTitle("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSetting.setVerticalTitle("percent_heap_used");
        gcParNewHeapSetting.setYMax("100");
        gcParNewHeapSetting.setYMin("0");

        GraphiteGraphSettings gcParNewTimeSetting= new GraphiteGraphSettings();
        gcParNewTimeSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTimeSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSetting.setTitle("sfly." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSetting.setVerticalTitle("collection_time_in_ms");
        gcParNewTimeSetting.setYMax("");
        gcParNewTimeSetting.setYMin("");

        GraphiteGraphSettings poolCPUUserSetting= new GraphiteGraphSettings();
        poolCPUUserSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUUserSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUUserSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_User_Usage");
        poolCPUUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        poolCPUUserSetting.setYMax("100");
        poolCPUUserSetting.setYMin("0");

        GraphiteGraphSettings poolCPUSystemSetting= new GraphiteGraphSettings();
        poolCPUSystemSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUSystemSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUSystemSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_System_Usage");
        poolCPUSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        poolCPUSystemSetting.setYMax("100");
        poolCPUSystemSetting.setYMin("0");

        GraphiteGraphSettings poolCPUIOWaitSetting = new GraphiteGraphSettings();
        poolCPUIOWaitSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUIOWaitSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUIOWaitSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        poolCPUIOWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        poolCPUIOWaitSetting.setYMax("100");
        poolCPUIOWaitSetting.setYMin("0");

        GraphiteGraphSettings poolRamSetting= new GraphiteGraphSettings();
        poolRamSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        poolRamSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.memory.memory-{used%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.memory.memory-buffered");
        poolRamSetting.setTitle(poolShortName.toUpperCase() + "_POOL_RAM_Usage");
        poolRamSetting.setVerticalTitle("Amount_RAM_Used");
        poolRamSetting.setYMax("");
        poolRamSetting.setYMin("");

        GraphiteGraphSettings appPoolSwapSetting= new GraphiteGraphSettings();
        appPoolSwapSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolSwapSetting.setTarget("sfly." + env + ".host." + poolShortName + ".*.swap.swap-{used%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.swap.swap-used");
        appPoolSwapSetting.setTitle(poolShortName.toUpperCase() + "_POOL_SWAP_Usage");
        appPoolSwapSetting.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwapSetting.setYMax("");
        appPoolSwapSetting.setYMin("");

        expectedSettings.add(gcMarkSweepHeapSetting);
        expectedSettings.add(gcMarkSweepTimeSetting);
        expectedSettings.add(gcParNewHeapSetting);
        expectedSettings.add(gcParNewTimeSetting);
        expectedSettings.add(poolCPUUserSetting);
        expectedSettings.add(poolCPUSystemSetting);
        expectedSettings.add(poolCPUIOWaitSetting);
        expectedSettings.add(poolRamSetting);
        expectedSettings.add(appPoolSwapSetting);
        return expectedSettings;
    }

    private Calendar getStartTime() {
        Calendar startTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:00 am
        startTime.set(2014, Calendar.JANUARY, 1, 8, 0, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }

    private Calendar getEndTime() {
        Calendar endTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:30 am
        endTime.set(2014, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    private long getDuration() {
        return getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis();
    }

}
