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
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.Environment;
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.ServerPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphiteGraphSettingsBuilderTest {

    private static final String SUPPORTED_POOL = "appserver";
    private static final String SUPPORTED_POOL_WITH_SUPPLEMENTAL = "apiserver";
    private static final String SUPPLMENTAL_POOL = "wsserver";
    private static final String UNSUPPORTED_POOL = "nonsenseserver";


    private static final int NORMAL_EXPECTED_NUM_GRAPHS = 21;
    private static final int NUMBER_DUPLICATE_GRAPHS = 12;
    private static final int SUPPLEMENTED_EXPECTED_NUM_GRAPHS = (2 * NORMAL_EXPECTED_NUM_GRAPHS) - NUMBER_DUPLICATE_GRAPHS;

    @Test
    public void testGetDefinedSettingsNoSupplementalGraphsAllEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        for(Environment env: Environment.values()) {
            String envName = env.name;

            BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(envName, SUPPORTED_POOL);

            List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

            String assertMessage = "Error getting num for env/pool: " + envName +"/" + SUPPORTED_POOL;
            Assert.assertEquals(assertMessage, NORMAL_EXPECTED_NUM_GRAPHS, generatedSettings.size());

            ArrayList<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings(envName, SUPPORTED_POOL);

            Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
            for( int i = 0; i < expectedSettings.size(); i++) {
                Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
            }
        }
    }

    @Test
    public void testGetDefinedSettingsWithSupplementalGraphsAllEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();
        for(Environment env: Environment.values()) {
            String envName = env.name;


            BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);

            List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);


            String assertMessage = "Error getting num for env/pool: " + envName +"/" + SUPPORTED_POOL;
            Assert.assertEquals(assertMessage, SUPPLEMENTED_EXPECTED_NUM_GRAPHS, generatedSettings.size());

            List<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings(envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);
            List<GraphiteGraphSettings> expectedSupplementalSettings = getListGraphiteSettings(envName, SUPPLMENTAL_POOL);

            expectedSettings = mergeAndSortGraphSettings(expectedSettings, expectedSupplementalSettings);

            Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
            for( int i = 0; i < expectedSettings.size(); i++) {
                Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
            }
        }
    }

    @Test
    public void testGetUndefinedSettingsAllEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();
        for(Environment env: Environment.values()) {
            String envName = env.name;
            String pool = UNSUPPORTED_POOL;

            BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
            inputCriteria.setEnvironmentName(envName);
            inputCriteria.setPoolName(pool);
            inputCriteria.setBuildStartTime(getStartTime());
            inputCriteria.setBuildDuration(getDuration());

            List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

            Assert.assertEquals(0, generatedSettings.size());
        }
    }

    private BuildInfoForTargetEnvGraph getBuildInfoForEnvPool(String env, String pool) {
        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setEnvironmentName(env);
        inputCriteria.setPoolName(pool);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());
        return inputCriteria;
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

        GraphiteGraphSettings mspCpuUserSetting= new GraphiteGraphSettings();
        mspCpuUserSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mspCpuUserSetting.setTarget("sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuUserSetting.setTitle("MSP_DATABASE_CPU_User_Usage");
        mspCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        mspCpuUserSetting.setYMax("100");
        mspCpuUserSetting.setYMin("0");

        GraphiteGraphSettings mspCpuSystemSetting= new GraphiteGraphSettings();
        mspCpuSystemSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mspCpuSystemSetting.setTarget("sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuSystemSetting.setTitle("MSP_DATABASE_CPU_System_Usage");
        mspCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        mspCpuSystemSetting.setYMax("100");
        mspCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings mspCpuWaitSetting= new GraphiteGraphSettings();
        mspCpuWaitSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mspCpuWaitSetting.setTarget("sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuWaitSetting.setTitle("MSP_DATABASE_CPU_IO_Wait_Usage");
        mspCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mspCpuWaitSetting.setYMax("100");
        mspCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings mspLoadAvgSetting= new GraphiteGraphSettings();
        mspLoadAvgSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mspLoadAvgSetting.setTarget("sfly." + env + ".host.oracle.*.load.load.*term");
        mspLoadAvgSetting.setTitle("MSP_DATABASE_Load_Average");
        mspLoadAvgSetting.setVerticalTitle("Load_Average");
        mspLoadAvgSetting.setYMax("100");
        mspLoadAvgSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuUserSetting= new GraphiteGraphSettings();
        nxGenCpuUserSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        nxGenCpuUserSetting.setTarget("sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuUserSetting.setTitle("NXGEN_DATABASE_CPU_User_Usage");
        nxGenCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        nxGenCpuUserSetting.setYMax("100");
        nxGenCpuUserSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuSystemSetting= new GraphiteGraphSettings();
        nxGenCpuSystemSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        nxGenCpuSystemSetting.setTarget("sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuSystemSetting.setTitle("NXGEN_DATABASE_CPU_System_Usage");
        nxGenCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        nxGenCpuSystemSetting.setYMax("100");
        nxGenCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuWaitSetting= new GraphiteGraphSettings();
        nxGenCpuWaitSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        nxGenCpuWaitSetting.setTarget("sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuWaitSetting.setTitle("NXGEN_DATABASE_CPU_IO_Wait_Usage");
        nxGenCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        nxGenCpuWaitSetting.setYMax("100");
        nxGenCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings nxGenLoadAvgSetting= new GraphiteGraphSettings();
        nxGenLoadAvgSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        nxGenLoadAvgSetting.setTarget("sfly." + env + ".host.oracle-x86_64.*.load.load.*term");
        nxGenLoadAvgSetting.setTitle("NXGEN_DATABASE_Load_Average");
        nxGenLoadAvgSetting.setVerticalTitle("Load_Average");
        nxGenLoadAvgSetting.setYMax("100");
        nxGenLoadAvgSetting.setYMin("0");

        ///
        GraphiteGraphSettings mongoDbCpuUserSetting= new GraphiteGraphSettings();
        mongoDbCpuUserSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mongoDbCpuUserSetting.setTarget("sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuUserSetting.setTitle("MongoDB_CPU_User_Usage");
        mongoDbCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        mongoDbCpuUserSetting.setYMax("100");
        mongoDbCpuUserSetting.setYMin("0");

        GraphiteGraphSettings mongoDbCpuSystemSetting= new GraphiteGraphSettings();
        mongoDbCpuSystemSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mongoDbCpuSystemSetting.setTarget("sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuSystemSetting.setTitle("MongoDB_CPU_System_Usage");
        mongoDbCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        mongoDbCpuSystemSetting.setYMax("100");
        mongoDbCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings mongoDbCpuWaitSetting= new GraphiteGraphSettings();
        mongoDbCpuWaitSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mongoDbCpuWaitSetting.setTarget("sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuWaitSetting.setTitle("MongoDB_CPU_IO_Wait_Usage");
        mongoDbCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mongoDbCpuWaitSetting.setYMax("100");
        mongoDbCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings mongoDbLoadAvgSetting= new GraphiteGraphSettings();
        mongoDbLoadAvgSetting.setHost("http://graphite.internal.shutterfly.com:443/");
        mongoDbLoadAvgSetting.setTarget("sfly." + env + ".host.mongodb.*.load.load.*term");
        mongoDbLoadAvgSetting.setTitle("MongoDB_Load_Average");
        mongoDbLoadAvgSetting.setVerticalTitle("Load_Average");
        mongoDbLoadAvgSetting.setYMax("100");
        mongoDbLoadAvgSetting.setYMin("0");

        expectedSettings.add(gcMarkSweepHeapSetting);
        expectedSettings.add(gcMarkSweepTimeSetting);
        expectedSettings.add(gcParNewHeapSetting);
        expectedSettings.add(gcParNewTimeSetting);
        expectedSettings.add(poolCPUUserSetting);
        expectedSettings.add(poolCPUSystemSetting);
        expectedSettings.add(poolCPUIOWaitSetting);
        expectedSettings.add(poolRamSetting);
        expectedSettings.add(appPoolSwapSetting);

        expectedSettings.add(mspCpuUserSetting);
        expectedSettings.add(mspCpuSystemSetting);
        expectedSettings.add(mspCpuWaitSetting);
        expectedSettings.add(mspLoadAvgSetting);
        expectedSettings.add(nxGenCpuUserSetting);
        expectedSettings.add(nxGenCpuSystemSetting);
        expectedSettings.add(nxGenCpuWaitSetting);
        expectedSettings.add(nxGenLoadAvgSetting);

        expectedSettings.add(mongoDbCpuUserSetting);
        expectedSettings.add(mongoDbCpuSystemSetting);
        expectedSettings.add(mongoDbCpuWaitSetting);
        expectedSettings.add(mongoDbLoadAvgSetting);

        return expectedSettings;
    }

    private List<GraphiteGraphSettings> mergeAndSortGraphSettings(List<GraphiteGraphSettings> a, List<GraphiteGraphSettings> b ) {
        ArrayList<GraphiteGraphSettings> temp = new ArrayList<GraphiteGraphSettings>();
        if(a.size() == b.size()) {
            for(int i = 0; i < a.size(); i++) {
                temp.add(a.get(i));
                if(!temp.contains(b.get(i))){
                    temp.add(b.get(i));
                }
            }
            return temp;
        } else {
            a.addAll(b);
            return a;
        }
    }

    private Calendar getStartTime() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(2014, Calendar.JANUARY, 1, 8, 0, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }

    private Calendar getEndTime() {
        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    private long getDuration() {
        return getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis();
    }

}
