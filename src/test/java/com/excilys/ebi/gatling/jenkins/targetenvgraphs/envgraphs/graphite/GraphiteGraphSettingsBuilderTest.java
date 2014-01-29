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

        GraphiteGraphSettings gcMarkSweepHeap = new GraphiteGraphSettings();
        gcMarkSweepHeap.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeap.setTarget("sfly."+env+".host."+ poolShortName+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeap.setTitle("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeap.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeap.setYMax("100");
        gcMarkSweepHeap.setYMin("0");

        GraphiteGraphSettings gcMarkSweepTime = new GraphiteGraphSettings();
        gcMarkSweepTime.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTime.setTarget("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTime.setTitle("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTime.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTime.setYMax("");
        gcMarkSweepTime.setYMin("");


        GraphiteGraphSettings gcParNewHeap= new GraphiteGraphSettings();
        gcParNewHeap.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeap.setTarget("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeap.setTitle("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeap.setVerticalTitle("percent_heap_used");
        gcParNewHeap.setYMax("100");
        gcParNewHeap.setYMin("0");

        GraphiteGraphSettings gcParNewTime= new GraphiteGraphSettings();
        gcParNewTime.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTime.setTarget("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTime.setTitle("sfly."+env+".host."+poolShortName+".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTime.setVerticalTitle("collection_time_in_ms");
        gcParNewTime.setYMax("");
        gcParNewTime.setYMin("");

        GraphiteGraphSettings poolCPUUser= new GraphiteGraphSettings();
        poolCPUUser.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUUser.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUUser.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_User_Usage");
        poolCPUUser.setVerticalTitle("CPU_Percent_User_Used");
        poolCPUUser.setYMax("100");
        poolCPUUser.setYMin("0");

        GraphiteGraphSettings poolCPUSystem= new GraphiteGraphSettings();
        poolCPUSystem.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUSystem.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUSystem.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_System_Usage");
        poolCPUSystem.setVerticalTitle("CPU_Percent_System_Used");
        poolCPUSystem.setYMax("100");
        poolCPUSystem.setYMin("0");

        GraphiteGraphSettings poolCPUIOWait = new GraphiteGraphSettings();
        poolCPUIOWait.setHost("http://graphite.internal.shutterfly.com:443/");
        poolCPUIOWait.setTarget("sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUIOWait.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        poolCPUIOWait.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        poolCPUIOWait.setYMax("100");
        poolCPUIOWait.setYMin("0");

        GraphiteGraphSettings poolRam= new GraphiteGraphSettings();
        poolRam.setHost("http://graphite.internal.shutterfly.com:443/");
        poolRam.setTarget("sfly." + env + ".host." + poolShortName + ".*.memory.memory-{used%2C}.value%2Ccolor%28sfly." + env + ".host."+poolShortName+".*.memory.memory-buffered");
        poolRam.setTitle(poolShortName.toUpperCase() + "_POOL_RAM_Usage");
        poolRam.setVerticalTitle("Amount_RAM_Used");
        poolRam.setYMax("");
        poolRam.setYMin("");

        GraphiteGraphSettings appPoolSwap= new GraphiteGraphSettings();
        appPoolSwap.setHost("http://graphite.internal.shutterfly.com:443/");
        appPoolSwap.setTarget("sfly."+env+".host."+poolShortName+".*.swap.swap-{used%2C}.value%2Ccolor%28sfly."+env+".host."+poolShortName+".*.swap.swap-used");
        appPoolSwap.setTitle(poolShortName.toUpperCase()+"_POOL_SWAP_Usage");
        appPoolSwap.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwap.setYMax("");
        appPoolSwap.setYMin("");

        expectedSettings.add(gcMarkSweepHeap);
        expectedSettings.add(gcMarkSweepTime);
        expectedSettings.add(gcParNewHeap);
        expectedSettings.add(gcParNewTime);
        expectedSettings.add(poolCPUUser);
        expectedSettings.add(poolCPUSystem);
        expectedSettings.add(poolCPUIOWait);
        expectedSettings.add(poolRam);
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
