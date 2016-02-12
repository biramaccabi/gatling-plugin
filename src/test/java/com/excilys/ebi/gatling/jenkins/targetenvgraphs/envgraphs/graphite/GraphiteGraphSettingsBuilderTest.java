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


import com.excilys.ebi.gatling.jenkins.targetenvgraphs.Brand;
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
    private static final String SFLY = "sfly";
    private static final String TP = "tp";
    private static final String KAPPA= "kappa";


    private static final int NORMAL_EXPECTED_NUM_GRAPHS = 21;
    private static final int NUMBER_DUPLICATE_GRAPHS = 12;
    private static final int SUPPLEMENTED_EXPECTED_NUM_GRAPHS = (2 * NORMAL_EXPECTED_NUM_GRAPHS) - NUMBER_DUPLICATE_GRAPHS;

    @Test
    public void testGetDefinedSettingsNoSupplementalGraphsSflyAllEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        for (Environment env : Environment.values()) {
            if (env.brand.equals(Brand.SHUTTERFLY)) {
                String envName = env.name;

                BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(Brand.SHUTTERFLY, envName, SUPPORTED_POOL);

                List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

                String assertMessage = "Error getting num for env/pool: " + envName + "/" + SUPPORTED_POOL;
                Assert.assertEquals(assertMessage, NORMAL_EXPECTED_NUM_GRAPHS, generatedSettings.size());

                ArrayList<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings(SFLY, envName, SUPPORTED_POOL);

                Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
                for (int i = 0; i < expectedSettings.size(); i++) {
                    Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
                }
            }
        }
    }

    @Test
    public void testGetDefinedSettingsWithSupplementalGraphsAllSflyEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();
        for (Environment env : Environment.values()) {
            if (env.brand.equals(Brand.SHUTTERFLY)) {
                String envName = env.name;

                BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(Brand.SHUTTERFLY, envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);

                List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

                String assertMessage = "Error getting num for env/pool: " + envName + "/" + SUPPORTED_POOL;
                Assert.assertEquals(assertMessage, SUPPLEMENTED_EXPECTED_NUM_GRAPHS, generatedSettings.size());

                List<GraphiteGraphSettings> expectedSettings = getListGraphiteSettingsPoolWithSupplemental(SFLY, envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);

                Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
                for (int i = 0; i < expectedSettings.size(); i++) {
                    Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
                }
            }
        }
    }

    @Test
    public void testGetDefinedSettingsNoSupplementalGraphsTPAllEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();

        for (Environment env : Environment.values()) {
            if (env.brand.equals(Brand.TINYPRINTS)) {
                String envName = env.name;

                BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(Brand.TINYPRINTS, envName, SUPPORTED_POOL);

                List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

                String assertMessage = "Error getting num for env/pool: " + envName + "/" + SUPPORTED_POOL;
                Assert.assertEquals(assertMessage, NORMAL_EXPECTED_NUM_GRAPHS, generatedSettings.size());

                ArrayList<GraphiteGraphSettings> expectedSettings = getListGraphiteSettings(TP, envName, SUPPORTED_POOL);

                Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
                for (int i = 0; i < expectedSettings.size(); i++) {
                    Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
                }
            }
        }
    }

    @Test
    public void testGetDefinedSettingsWithSupplementalGraphsAllTPEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();
        for (Environment env : Environment.values()) {
            if (env.brand.equals(Brand.TINYPRINTS)) {
                String envName = env.name;

                BuildInfoForTargetEnvGraph inputCriteria = getBuildInfoForEnvPool(Brand.TINYPRINTS, envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);

                List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

                String assertMessage = "Error getting num for env/pool: " + envName + "/" + SUPPORTED_POOL;
                Assert.assertEquals(assertMessage, SUPPLEMENTED_EXPECTED_NUM_GRAPHS, generatedSettings.size());

                List<GraphiteGraphSettings> expectedSettings = getListGraphiteSettingsPoolWithSupplemental(TP, envName, SUPPORTED_POOL_WITH_SUPPLEMENTAL);

                Assert.assertEquals(expectedSettings.size(), generatedSettings.size());
                for (int i = 0; i < expectedSettings.size(); i++) {
                    Assert.assertEquals(expectedSettings.get(i), generatedSettings.get(i));
                }
            }
        }
    }

    @Test
    public void testGetUndefinedSettingsAllSflyEnvironments() {
        GraphiteGraphSettingsBuilder testBuilder = new GraphiteGraphSettingsBuilder();
        for(Environment env: Environment.values()) {
            String envName = env.name;
            String pool = UNSUPPORTED_POOL;

            BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
            inputCriteria.setEnvironmentName(envName);
            inputCriteria.setPoolName(pool);
            inputCriteria.setBuildStartTime(getStartTime());
            inputCriteria.setBuildDuration(getDuration());
            inputCriteria.setBrand(Brand.SHUTTERFLY);

            List<GraphiteGraphSettings> generatedSettings = testBuilder.getGraphiteGraphSettings(inputCriteria);

            Assert.assertEquals(0, generatedSettings.size());
        }
    }

    private BuildInfoForTargetEnvGraph getBuildInfoForEnvPool(Brand brand, String env, String pool) {
        BuildInfoForTargetEnvGraph inputCriteria = new BuildInfoForTargetEnvGraph();
        inputCriteria.setBrand(brand);
        inputCriteria.setEnvironmentName(env);
        inputCriteria.setPoolName(pool);
        inputCriteria.setBuildStartTime(getStartTime());
        inputCriteria.setBuildDuration(getDuration());
        return inputCriteria;
    }

    private ArrayList<GraphiteGraphSettings> getListGraphiteSettings(String brand, String env, String pool) {
        ArrayList<GraphiteGraphSettings> expectedSettings = new ArrayList<GraphiteGraphSettings>();

        String poolShortName = ServerPool.getEnumForPoolName(pool).shortName;

        GraphiteGraphSettings gcMarkSweepHeapSetting = new GraphiteGraphSettings();
        gcMarkSweepHeapSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeapSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSetting.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSetting.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeapSetting.setYMax("100");
        gcMarkSweepHeapSetting.setYMin("0");

        GraphiteGraphSettings gcMarkSweepTimeSetting = new GraphiteGraphSettings();
        gcMarkSweepTimeSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTimeSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSetting.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSetting.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTimeSetting.setYMax("");
        gcMarkSweepTimeSetting.setYMin("");


        GraphiteGraphSettings gcParNewHeapSetting= new GraphiteGraphSettings();
        gcParNewHeapSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeapSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSetting.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSetting.setVerticalTitle("percent_heap_used");
        gcParNewHeapSetting.setYMax("100");
        gcParNewHeapSetting.setYMin("0");

        GraphiteGraphSettings gcParNewTimeSetting= new GraphiteGraphSettings();
        gcParNewTimeSetting.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTimeSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSetting.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSetting.setVerticalTitle("collection_time_in_ms");
        gcParNewTimeSetting.setYMax("");
        gcParNewTimeSetting.setYMin("");

        GraphiteGraphSettings poolCPUUserSetting= new GraphiteGraphSettings();
        poolCPUUserSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUUserSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUUserSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_User_Usage");
        poolCPUUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        poolCPUUserSetting.setYMax("100");
        poolCPUUserSetting.setYMin("0");

        GraphiteGraphSettings poolCPUSystemSetting= new GraphiteGraphSettings();
        poolCPUSystemSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUSystemSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUSystemSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_System_Usage");
        poolCPUSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        poolCPUSystemSetting.setYMax("100");
        poolCPUSystemSetting.setYMin("0");

        GraphiteGraphSettings poolCPUIOWaitSetting = new GraphiteGraphSettings();
        poolCPUIOWaitSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUIOWaitSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUIOWaitSetting.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        poolCPUIOWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        poolCPUIOWaitSetting.setYMax("100");
        poolCPUIOWaitSetting.setYMin("0");

        GraphiteGraphSettings poolRamSetting= new GraphiteGraphSettings();
        poolRamSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolRamSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.memory.memory-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.memory.memory-buffered");
        poolRamSetting.setTitle(poolShortName.toUpperCase() + "_POOL_RAM_Usage");
        poolRamSetting.setVerticalTitle("Amount_RAM_Used");
        poolRamSetting.setYMax("");
        poolRamSetting.setYMin("");

        GraphiteGraphSettings appPoolSwapSetting= new GraphiteGraphSettings();
        appPoolSwapSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        appPoolSwapSetting.setTarget(brand + "." + env + ".host." + poolShortName + ".*.swap.swap-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.swap.swap-used");
        appPoolSwapSetting.setTitle(poolShortName.toUpperCase() + "_POOL_SWAP_Usage");
        appPoolSwapSetting.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwapSetting.setYMax("");
        appPoolSwapSetting.setYMin("");

        GraphiteGraphSettings mspCpuUserSetting= new GraphiteGraphSettings();
        mspCpuUserSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuUserSetting.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuUserSetting.setTitle("MSP_DATABASE_CPU_User_Usage");
        mspCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        mspCpuUserSetting.setYMax("100");
        mspCpuUserSetting.setYMin("0");

        GraphiteGraphSettings mspCpuSystemSetting= new GraphiteGraphSettings();
        mspCpuSystemSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuSystemSetting.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuSystemSetting.setTitle("MSP_DATABASE_CPU_System_Usage");
        mspCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        mspCpuSystemSetting.setYMax("100");
        mspCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings mspCpuWaitSetting= new GraphiteGraphSettings();
        mspCpuWaitSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuWaitSetting.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuWaitSetting.setTitle("MSP_DATABASE_CPU_IO_Wait_Usage");
        mspCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mspCpuWaitSetting.setYMax("100");
        mspCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings mspLoadAvgSetting= new GraphiteGraphSettings();
        mspLoadAvgSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspLoadAvgSetting.setTarget(brand + "." + env + ".host.oracle.*.load.load.*term");
        mspLoadAvgSetting.setTitle("MSP_DATABASE_Load_Average");
        mspLoadAvgSetting.setVerticalTitle("Load_Average");
        mspLoadAvgSetting.setYMax("100");
        mspLoadAvgSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuUserSetting= new GraphiteGraphSettings();
        nxGenCpuUserSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuUserSetting.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuUserSetting.setTitle("NXGEN_DATABASE_CPU_User_Usage");
        nxGenCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        nxGenCpuUserSetting.setYMax("100");
        nxGenCpuUserSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuSystemSetting= new GraphiteGraphSettings();
        nxGenCpuSystemSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuSystemSetting.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuSystemSetting.setTitle("NXGEN_DATABASE_CPU_System_Usage");
        nxGenCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        nxGenCpuSystemSetting.setYMax("100");
        nxGenCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings nxGenCpuWaitSetting= new GraphiteGraphSettings();
        nxGenCpuWaitSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuWaitSetting.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuWaitSetting.setTitle("NXGEN_DATABASE_CPU_IO_Wait_Usage");
        nxGenCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        nxGenCpuWaitSetting.setYMax("100");
        nxGenCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings nxGenLoadAvgSetting= new GraphiteGraphSettings();
        nxGenLoadAvgSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenLoadAvgSetting.setTarget(brand + "." + env + ".host.oracle-x86_64.*.load.load.*term");
        nxGenLoadAvgSetting.setTitle("NXGEN_DATABASE_Load_Average");
        nxGenLoadAvgSetting.setVerticalTitle("Load_Average");
        nxGenLoadAvgSetting.setYMax("100");
        nxGenLoadAvgSetting.setYMin("0");

        GraphiteGraphSettings mongoDbCpuUserSetting= new GraphiteGraphSettings();
        mongoDbCpuUserSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuUserSetting.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuUserSetting.setTitle("MongoDB_CPU_User_Usage");
        mongoDbCpuUserSetting.setVerticalTitle("CPU_Percent_User_Used");
        mongoDbCpuUserSetting.setYMax("100");
        mongoDbCpuUserSetting.setYMin("0");

        GraphiteGraphSettings mongoDbCpuSystemSetting= new GraphiteGraphSettings();
        mongoDbCpuSystemSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuSystemSetting.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuSystemSetting.setTitle("MongoDB_CPU_System_Usage");
        mongoDbCpuSystemSetting.setVerticalTitle("CPU_Percent_System_Used");
        mongoDbCpuSystemSetting.setYMax("100");
        mongoDbCpuSystemSetting.setYMin("0");

        GraphiteGraphSettings mongoDbCpuWaitSetting= new GraphiteGraphSettings();
        mongoDbCpuWaitSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuWaitSetting.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuWaitSetting.setTitle("MongoDB_CPU_IO_Wait_Usage");
        mongoDbCpuWaitSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mongoDbCpuWaitSetting.setYMax("100");
        mongoDbCpuWaitSetting.setYMin("0");

        GraphiteGraphSettings mongoDbLoadAvgSetting= new GraphiteGraphSettings();
        mongoDbLoadAvgSetting.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbLoadAvgSetting.setTarget(brand + "." + env + ".host.mongodb.*.load.load.*term");
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

    private ArrayList<GraphiteGraphSettings> getListGraphiteSettingsPoolWithSupplemental(String brand, String env, String pool) {
        ArrayList<GraphiteGraphSettings> expectedSettings = new ArrayList<GraphiteGraphSettings>();

        String poolShortName = ServerPool.getEnumForPoolName(pool).shortName;
        String supplemntalPoolShortName = ServerPool.getEnumForPoolName(SUPPLMENTAL_POOL).shortName;
        

        GraphiteGraphSettings gcMarkSweepHeapSettingNormal = new GraphiteGraphSettings();
        gcMarkSweepHeapSettingNormal.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeapSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSettingNormal.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSettingNormal.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeapSettingNormal.setYMax("100");
        gcMarkSweepHeapSettingNormal.setYMin("0");

        GraphiteGraphSettings gcMarkSweepHeapSettingSupplemental = new GraphiteGraphSettings();
        gcMarkSweepHeapSettingSupplemental.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepHeapSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSettingSupplemental.setTitle(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage");
        gcMarkSweepHeapSettingSupplemental.setVerticalTitle("percent_heap_used");
        gcMarkSweepHeapSettingSupplemental.setYMax("100");
        gcMarkSweepHeapSettingSupplemental.setYMin("0");

        GraphiteGraphSettings gcMarkSweepTimeSettingNormal = new GraphiteGraphSettings();
        gcMarkSweepTimeSettingNormal.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTimeSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSettingNormal.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSettingNormal.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTimeSettingNormal.setYMax("");
        gcMarkSweepTimeSettingNormal.setYMin("");

        GraphiteGraphSettings gcMarkSweepTimeSettingSupplemental = new GraphiteGraphSettings();
        gcMarkSweepTimeSettingSupplemental.setHost("http://tre-stats.internal.shutterfly.com");
        gcMarkSweepTimeSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSettingSupplemental.setTitle(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime");
        gcMarkSweepTimeSettingSupplemental.setVerticalTitle("collection_time_in_ms");
        gcMarkSweepTimeSettingSupplemental.setYMax("");
        gcMarkSweepTimeSettingSupplemental.setYMin("");


        GraphiteGraphSettings gcParNewHeapSettingNormal= new GraphiteGraphSettings();
        gcParNewHeapSettingNormal.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeapSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSettingNormal.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSettingNormal.setVerticalTitle("percent_heap_used");
        gcParNewHeapSettingNormal.setYMax("100");
        gcParNewHeapSettingNormal.setYMin("0");

        GraphiteGraphSettings gcParNewHeapSettingSupplemental= new GraphiteGraphSettings();
        gcParNewHeapSettingSupplemental.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewHeapSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSettingSupplemental.setTitle(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");
        gcParNewHeapSettingSupplemental.setVerticalTitle("percent_heap_used");
        gcParNewHeapSettingSupplemental.setYMax("100");
        gcParNewHeapSettingSupplemental.setYMin("0");

        GraphiteGraphSettings gcParNewTimeSettingNormal= new GraphiteGraphSettings();
        gcParNewTimeSettingNormal.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTimeSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSettingNormal.setTitle(brand + "." + env + ".host." + poolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSettingNormal.setVerticalTitle("collection_time_in_ms");
        gcParNewTimeSettingNormal.setYMax("");
        gcParNewTimeSettingNormal.setYMin("");

        GraphiteGraphSettings gcParNewTimeSettingSupplemental= new GraphiteGraphSettings();
        gcParNewTimeSettingSupplemental.setHost("http://tre-stats.internal.shutterfly.com");
        gcParNewTimeSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSettingSupplemental.setTitle(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.GarbageCollectorSentinel.ParNew.collectionTime");
        gcParNewTimeSettingSupplemental.setVerticalTitle("collection_time_in_ms");
        gcParNewTimeSettingSupplemental.setYMax("");
        gcParNewTimeSettingSupplemental.setYMin("");

        GraphiteGraphSettings poolCPUUserSettingNormal= new GraphiteGraphSettings();
        poolCPUUserSettingNormal.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUUserSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUUserSettingNormal.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_User_Usage");
        poolCPUUserSettingNormal.setVerticalTitle("CPU_Percent_User_Used");
        poolCPUUserSettingNormal.setYMax("100");
        poolCPUUserSettingNormal.setYMin("0");

        GraphiteGraphSettings poolCPUUserSettingSupplemental= new GraphiteGraphSettings();
        poolCPUUserSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUUserSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUUserSettingSupplemental.setTitle(supplemntalPoolShortName.toUpperCase() + "_POOL_CPU_User_Usage");
        poolCPUUserSettingSupplemental.setVerticalTitle("CPU_Percent_User_Used");
        poolCPUUserSettingSupplemental.setYMax("100");
        poolCPUUserSettingSupplemental.setYMin("0");

        GraphiteGraphSettings poolCPUSystemSettingNormal = new GraphiteGraphSettings();
        poolCPUSystemSettingNormal.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUSystemSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUSystemSettingNormal.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_System_Usage");
        poolCPUSystemSettingNormal.setVerticalTitle("CPU_Percent_System_Used");
        poolCPUSystemSettingNormal.setYMax("100");
        poolCPUSystemSettingNormal.setYMin("0");

        GraphiteGraphSettings poolCPUSystemSettingSupplemental = new GraphiteGraphSettings();
        poolCPUSystemSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUSystemSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUSystemSettingSupplemental.setTitle(supplemntalPoolShortName.toUpperCase() + "_POOL_CPU_System_Usage");
        poolCPUSystemSettingSupplemental.setVerticalTitle("CPU_Percent_System_Used");
        poolCPUSystemSettingSupplemental.setYMax("100");
        poolCPUSystemSettingSupplemental.setYMin("0");

        GraphiteGraphSettings poolCPUIOWaitSettingNormal = new GraphiteGraphSettings();
        poolCPUIOWaitSettingNormal.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUIOWaitSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUIOWaitSettingNormal.setTitle(poolShortName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        poolCPUIOWaitSettingNormal.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        poolCPUIOWaitSettingNormal.setYMax("100");
        poolCPUIOWaitSettingNormal.setYMin("0");

        GraphiteGraphSettings poolCPUIOWaitSettingSupplemental = new GraphiteGraphSettings();
        poolCPUIOWaitSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolCPUIOWaitSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + supplemntalPoolShortName + ".*.aggregation-cpu-average.cpu-idle");
        poolCPUIOWaitSettingSupplemental.setTitle(supplemntalPoolShortName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        poolCPUIOWaitSettingSupplemental.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        poolCPUIOWaitSettingSupplemental.setYMax("100");
        poolCPUIOWaitSettingSupplemental.setYMin("0");

        GraphiteGraphSettings poolRamSettingNormal= new GraphiteGraphSettings();
        poolRamSettingNormal.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolRamSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.memory.memory-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.memory.memory-buffered");
        poolRamSettingNormal.setTitle(poolShortName.toUpperCase() + "_POOL_RAM_Usage");
        poolRamSettingNormal.setVerticalTitle("Amount_RAM_Used");
        poolRamSettingNormal.setYMax("");
        poolRamSettingNormal.setYMin("");

        GraphiteGraphSettings poolRamSettingSupplemental= new GraphiteGraphSettings();
        poolRamSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        poolRamSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.memory.memory-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + supplemntalPoolShortName + ".*.memory.memory-buffered");
        poolRamSettingSupplemental.setTitle(supplemntalPoolShortName.toUpperCase() + "_POOL_RAM_Usage");
        poolRamSettingSupplemental.setVerticalTitle("Amount_RAM_Used");
        poolRamSettingSupplemental.setYMax("");
        poolRamSettingSupplemental.setYMin("");

        GraphiteGraphSettings appPoolSwapSettingNormal= new GraphiteGraphSettings();
        appPoolSwapSettingNormal.setHost("http://graphite-web.internal.shutterfly.com:443/");
        appPoolSwapSettingNormal.setTarget(brand + "." + env + ".host." + poolShortName + ".*.swap.swap-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + poolShortName + ".*.swap.swap-used");
        appPoolSwapSettingNormal.setTitle(poolShortName.toUpperCase() + "_POOL_SWAP_Usage");
        appPoolSwapSettingNormal.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwapSettingNormal.setYMax("");
        appPoolSwapSettingNormal.setYMin("");

        GraphiteGraphSettings appPoolSwapSettingSupplemental= new GraphiteGraphSettings();
        appPoolSwapSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        appPoolSwapSettingSupplemental.setTarget(brand + "." + env + ".host." + supplemntalPoolShortName + ".*.swap.swap-{used%2C}.value%2Ccolor%28"+brand+"." + env + ".host." + supplemntalPoolShortName + ".*.swap.swap-used");
        appPoolSwapSettingSupplemental.setTitle(supplemntalPoolShortName.toUpperCase() + "_POOL_SWAP_Usage");
        appPoolSwapSettingSupplemental.setVerticalTitle("Amount_SWAP_Used");
        appPoolSwapSettingSupplemental.setYMax("");
        appPoolSwapSettingSupplemental.setYMin("");

        GraphiteGraphSettings mspCpuUserSettingSupplemental= new GraphiteGraphSettings();
        mspCpuUserSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuUserSettingSupplemental.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuUserSettingSupplemental.setTitle("MSP_DATABASE_CPU_User_Usage");
        mspCpuUserSettingSupplemental.setVerticalTitle("CPU_Percent_User_Used");
        mspCpuUserSettingSupplemental.setYMax("100");
        mspCpuUserSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mspCpuSystemSettingSupplemental = new GraphiteGraphSettings();
        mspCpuSystemSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuSystemSettingSupplemental.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuSystemSettingSupplemental.setTitle("MSP_DATABASE_CPU_System_Usage");
        mspCpuSystemSettingSupplemental.setVerticalTitle("CPU_Percent_System_Used");
        mspCpuSystemSettingSupplemental.setYMax("100");
        mspCpuSystemSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mspCpuWaitSettingSupplemental= new GraphiteGraphSettings();
        mspCpuWaitSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspCpuWaitSettingSupplemental.setTarget(brand + "." + env + ".host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle.*.aggregation-cpu-average.cpu-idle");
        mspCpuWaitSettingSupplemental.setTitle("MSP_DATABASE_CPU_IO_Wait_Usage");
        mspCpuWaitSettingSupplemental.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mspCpuWaitSettingSupplemental.setYMax("100");
        mspCpuWaitSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mspLoadAvgSettingSupplemental= new GraphiteGraphSettings();
        mspLoadAvgSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mspLoadAvgSettingSupplemental.setTarget(brand + "." + env + ".host.oracle.*.load.load.*term");
        mspLoadAvgSettingSupplemental.setTitle("MSP_DATABASE_Load_Average");
        mspLoadAvgSettingSupplemental.setVerticalTitle("Load_Average");
        mspLoadAvgSettingSupplemental.setYMax("100");
        mspLoadAvgSettingSupplemental.setYMin("0");

        GraphiteGraphSettings nxGenCpuUserSettingSupplemental= new GraphiteGraphSettings();
        nxGenCpuUserSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuUserSettingSupplemental.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuUserSettingSupplemental.setTitle("NXGEN_DATABASE_CPU_User_Usage");
        nxGenCpuUserSettingSupplemental.setVerticalTitle("CPU_Percent_User_Used");
        nxGenCpuUserSettingSupplemental.setYMax("100");
        nxGenCpuUserSettingSupplemental.setYMin("0");

        GraphiteGraphSettings nxGenCpuSystemSettingSupplemental= new GraphiteGraphSettings();
        nxGenCpuSystemSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuSystemSettingSupplemental.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuSystemSettingSupplemental.setTitle("NXGEN_DATABASE_CPU_System_Usage");
        nxGenCpuSystemSettingSupplemental.setVerticalTitle("CPU_Percent_System_Used");
        nxGenCpuSystemSettingSupplemental.setYMax("100");
        nxGenCpuSystemSettingSupplemental.setYMin("0");

        GraphiteGraphSettings nxGenCpuWaitSettingSupplemental= new GraphiteGraphSettings();
        nxGenCpuWaitSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenCpuWaitSettingSupplemental.setTarget(brand + "." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle");
        nxGenCpuWaitSettingSupplemental.setTitle("NXGEN_DATABASE_CPU_IO_Wait_Usage");
        nxGenCpuWaitSettingSupplemental.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        nxGenCpuWaitSettingSupplemental.setYMax("100");
        nxGenCpuWaitSettingSupplemental.setYMin("0");

        GraphiteGraphSettings nxGenLoadAvgSettingSupplemental= new GraphiteGraphSettings();
        nxGenLoadAvgSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        nxGenLoadAvgSettingSupplemental.setTarget(brand + "." + env + ".host.oracle-x86_64.*.load.load.*term");
        nxGenLoadAvgSettingSupplemental.setTitle("NXGEN_DATABASE_Load_Average");
        nxGenLoadAvgSettingSupplemental.setVerticalTitle("Load_Average");
        nxGenLoadAvgSettingSupplemental.setYMax("100");
        nxGenLoadAvgSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mongoDbCpuUserSettingSupplemental= new GraphiteGraphSettings();
        mongoDbCpuUserSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuUserSettingSupplemental.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuUserSettingSupplemental.setTitle("MongoDB_CPU_User_Usage");
        mongoDbCpuUserSettingSupplemental.setVerticalTitle("CPU_Percent_User_Used");
        mongoDbCpuUserSettingSupplemental.setYMax("100");
        mongoDbCpuUserSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mongoDbCpuSystemSettingSupplemental= new GraphiteGraphSettings();
        mongoDbCpuSystemSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuSystemSettingSupplemental.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuSystemSettingSupplemental.setTitle("MongoDB_CPU_System_Usage");
        mongoDbCpuSystemSettingSupplemental.setVerticalTitle("CPU_Percent_System_Used");
        mongoDbCpuSystemSettingSupplemental.setYMax("100");
        mongoDbCpuSystemSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mongoDbCpuWaitSettingSupplemental = new GraphiteGraphSettings();
        mongoDbCpuWaitSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbCpuWaitSettingSupplemental.setTarget(brand + "." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"." + env + ".host.mongodb.*.aggregation-cpu-average.cpu-idle");
        mongoDbCpuWaitSettingSupplemental.setTitle("MongoDB_CPU_IO_Wait_Usage");
        mongoDbCpuWaitSettingSupplemental.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        mongoDbCpuWaitSettingSupplemental.setYMax("100");
        mongoDbCpuWaitSettingSupplemental.setYMin("0");

        GraphiteGraphSettings mongoDbLoadAvgSettingSupplemental= new GraphiteGraphSettings();
        mongoDbLoadAvgSettingSupplemental.setHost("http://graphite-web.internal.shutterfly.com:443/");
        mongoDbLoadAvgSettingSupplemental.setTarget(brand + "." + env + ".host.mongodb.*.load.load.*term");
        mongoDbLoadAvgSettingSupplemental.setTitle("MongoDB_Load_Average");
        mongoDbLoadAvgSettingSupplemental.setVerticalTitle("Load_Average");
        mongoDbLoadAvgSettingSupplemental.setYMax("100");
        mongoDbLoadAvgSettingSupplemental.setYMin("0");

        expectedSettings.add(gcMarkSweepHeapSettingNormal);
        expectedSettings.add(gcMarkSweepHeapSettingSupplemental);

        expectedSettings.add(gcMarkSweepTimeSettingNormal);
        expectedSettings.add(gcMarkSweepTimeSettingSupplemental);

        expectedSettings.add(gcParNewHeapSettingNormal);
        expectedSettings.add(gcParNewHeapSettingSupplemental);

        expectedSettings.add(gcParNewTimeSettingNormal);
        expectedSettings.add(gcParNewTimeSettingSupplemental);

        expectedSettings.add(poolCPUUserSettingNormal);
        expectedSettings.add(poolCPUUserSettingSupplemental);

        expectedSettings.add(poolCPUSystemSettingNormal);
        expectedSettings.add(poolCPUSystemSettingSupplemental);

        expectedSettings.add(poolCPUIOWaitSettingNormal);
        expectedSettings.add(poolCPUIOWaitSettingSupplemental);

        expectedSettings.add(poolRamSettingNormal);
        expectedSettings.add(poolRamSettingSupplemental);

        expectedSettings.add(appPoolSwapSettingNormal);
        expectedSettings.add(appPoolSwapSettingSupplemental);

        expectedSettings.add(mspCpuUserSettingSupplemental);
        expectedSettings.add(mspCpuSystemSettingSupplemental);
        expectedSettings.add(mspCpuWaitSettingSupplemental);
        expectedSettings.add(mspLoadAvgSettingSupplemental);

        expectedSettings.add(nxGenCpuUserSettingSupplemental);
        expectedSettings.add(nxGenCpuSystemSettingSupplemental);
        expectedSettings.add(nxGenCpuWaitSettingSupplemental);
        expectedSettings.add(nxGenLoadAvgSettingSupplemental);

        expectedSettings.add(mongoDbCpuUserSettingSupplemental);
        expectedSettings.add(mongoDbCpuSystemSettingSupplemental);
        expectedSettings.add(mongoDbCpuWaitSettingSupplemental);
        expectedSettings.add(mongoDbLoadAvgSettingSupplemental);

        return expectedSettings;
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
