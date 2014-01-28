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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphiteGraphSettingsBuilderTest {

    public static final String FOXTROT = "foxtrot";
    public static final String SUPPORTED_SERVER = "appserver";
    public static final String UNSUPPORTED_SERVER = "nonsenseserver";
    public static final int expectedNumOfGraphs = 9;

    @Test
    public void testGetDefinedSettings() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setEnvironmentName(FOXTROT);
        inputCriteria.setPoolName(SUPPORTED_SERVER);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());

        List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

        Assert.assertEquals(expectedNumOfGraphs, generatedSettings.size());

        ArrayList<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings();
        for(GraphiteGraphSettings generatedSetting: generatedSettings) {
            Assert.assertTrue(expectedSettings.contains(generatedSetting));
        }
    }

    @Test
    public void testGetUndefinedSettings() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        String env = FOXTROT;
        String pool = UNSUPPORTED_SERVER;

        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setEnvironmentName(env);
        inputCriteria.setPoolName(pool);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());

        List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

        Assert.assertEquals(0, generatedSettings.size());
    }

    private ArrayList<GraphiteGraphSettings> getListGraphiteSettings() {
        ArrayList<GraphiteGraphSettings> expectedSettings = new ArrayList<GraphiteGraphSettings>();

        GraphiteGraphSettings gcMarkSweepHeap = new GraphiteGraphSettings();
        gcMarkSweepHeap.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeap.setTarget("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeap.setTitle("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeap.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeap.setYMax("100");
        gcMarkSweepHeap.setYMin("0");

        GraphiteGraphSettings gcMarkSweepTime = new GraphiteGraphSettings();
        gcMarkSweepTime.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTime.setTarget("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTime.setTitle("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTime.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTime.setYMax("");
        gcMarkSweepTime.setYMin("");


        GraphiteGraphSettings gcParNewHeap= new GraphiteGraphSettings();
        gcParNewHeap.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeap.setTarget("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeap.setTitle("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeap.setVerticalTitle("percent_heap_used");
        gcParNewHeap.setYMax("100");
        gcParNewHeap.setYMin("0");

        GraphiteGraphSettings gcParNewTime= new GraphiteGraphSettings();
        gcParNewTime.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTime.setTarget("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTime.setTitle("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTime.setVerticalTitle("collection_time_in_ms");
        gcParNewTime.setYMax("");
        gcParNewTime.setYMin("");

        GraphiteGraphSettings appPoolCPUUser= new GraphiteGraphSettings();
        appPoolCPUUser.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolCPUUser.setTarget("sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle");
        appPoolCPUUser.setTitle("APP_POOL_CPU_User_Usage");
        appPoolCPUUser.setVerticalTitle("CPU_Percent_User_Used");
        appPoolCPUUser.setYMax("100");
        appPoolCPUUser.setYMin("0");

        GraphiteGraphSettings appPoolCPUSystem= new GraphiteGraphSettings();
        appPoolCPUSystem.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolCPUSystem.setTarget("sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle");
        appPoolCPUSystem.setTitle("APP_POOL_CPU_System_Usage");
        appPoolCPUSystem.setVerticalTitle("CPU_Percent_System_Used");
        appPoolCPUSystem.setYMax("100");
        appPoolCPUSystem.setYMin("0");

        GraphiteGraphSettings appPoolCPUIOWait = new GraphiteGraphSettings();
        appPoolCPUIOWait.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolCPUIOWait.setTarget("sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle");
        appPoolCPUIOWait.setTitle("APP_POOL_CPU_IO_Wait_Usage");
        appPoolCPUIOWait.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        appPoolCPUIOWait.setYMax("100");
        appPoolCPUIOWait.setYMin("0");

        GraphiteGraphSettings appPoolRam= new GraphiteGraphSettings();
        appPoolRam.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolRam.setTarget("sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered");
        appPoolRam.setTitle("APP_POOL_RAM_Usage");
        appPoolRam.setVerticalTitle("Amount_RAM_Used");
        appPoolRam.setYMax("");
        appPoolRam.setYMin("");

        GraphiteGraphSettings appPoolSwap= new GraphiteGraphSettings();
        appPoolSwap.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolSwap.setTarget("sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used");
        appPoolSwap.setTitle("APP_POOL_SWAP_Usage");
        appPoolSwap.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwap.setYMax("");
        appPoolSwap.setYMin("");

        expectedSettings.add(gcMarkSweepHeap);
        expectedSettings.add(gcMarkSweepTime);
        expectedSettings.add(gcParNewHeap);
        expectedSettings.add(gcParNewTime);
        expectedSettings.add(appPoolCPUUser);
        expectedSettings.add(appPoolCPUSystem);
        expectedSettings.add(appPoolCPUIOWait);
        expectedSettings.add(appPoolRam);
        expectedSettings.add(appPoolSwap);
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
