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
import org.junit.Assert;
import org.junit.Before;
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
    BuildInfoForTargetEnvGraph supportedSflyEnvPoolCriteria;
    @Mock
    BuildInfoForTargetEnvGraph supportedTPEnvPoolCriteria;

    @Mock
    BuildInfoForTargetEnvGraph unsupportedSflyEnvPoolCriteria;
    @Mock
    BuildInfoForTargetEnvGraph unsupportedTPEnvPoolCriteria;

    @Before
    public void setup() {
        supportedSflyEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(supportedSflyEnvPoolCriteria.getEnvironmentName()).thenReturn("kappa");
        when(supportedSflyEnvPoolCriteria.getPoolName()).thenReturn("appserver");
        when(supportedSflyEnvPoolCriteria.getBrandName()).thenReturn("sfly");
        when(supportedSflyEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(supportedSflyEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());

        supportedTPEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(supportedTPEnvPoolCriteria.getEnvironmentName()).thenReturn("lnp");
        when(supportedTPEnvPoolCriteria.getPoolName()).thenReturn("appserver");
        when(supportedTPEnvPoolCriteria.getBrandName()).thenReturn("tp");
        when(supportedTPEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(supportedTPEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());


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

    @org.junit.Test
    public void testGetGraphUrlsForSupportedSflyEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(supportedSflyEnvPoolCriteria);

        String sizeAssertString = "should come back with: GC 4, Pool 5, MSP 4, NXGEN 4, Mongo 4. 21 Total.";
        int expectedGraphUrlCount = 4 + 5 + 4 + 4 + 4;
        Assert.assertEquals(sizeAssertString, expectedGraphUrlCount, graphUrls.size());

        ArrayList<String> expectedUrls = getListAppUrls("sfly","kappa","kapp");

        for(String url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }

        for(String expectedURl: expectedUrls) {
            Assert.assertTrue(graphUrls.contains(expectedURl));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForSupportedTPEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(supportedTPEnvPoolCriteria);

        String sizeAssertString = "should come back with: GC 4, Pool 5, MSP 4, NXGEN 4, Mongo 4. 21 Total.";
        int expectedGraphUrlCount = 4 + 5 + 4 + 4 + 4;
        Assert.assertEquals(sizeAssertString, expectedGraphUrlCount, graphUrls.size());

        ArrayList<String> expectedUrls = getListAppUrls("tp", "lnp", "app");

        for(String url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }

        for(String expectedURl: expectedUrls) {
            Assert.assertTrue(graphUrls.contains(expectedURl));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForUnsupportedSflyEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(unsupportedSflyEnvPoolCriteria);

        Assert.assertEquals("should come back with 0, when looking for an unsupported env/pool combo", 0, graphUrls.size());
    }

    @org.junit.Test
    public void testGetGraphUrlsForUnsupportedTPEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(unsupportedSflyEnvPoolCriteria);

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
        // set date to Jan 1, 2000 at 8:30 am
        endTime.set(2000, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    private ArrayList<String> getListAppUrls(String brand, String env, String pool) {
        String gcMarkSweepHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        String gcMarkSweepCollectionUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        String gcParNewHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String gcParNewCollectionTimeUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ParNew.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title="+brand+"."+env+".host."+pool+".*.GarbageCollectorSentinel.ParNew.collectionTime";
        String poolCpuUserUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_User_Usage";
        String poolCpuSystemUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_System_Usage";
        String poolCpuIOWaitUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"."+env+".host."+pool+".*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_IO_Wait_Usage";
        String poolRamUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.memory.memory-{used%2C}.value%2Ccolor%28"+brand+"."+env+".host."+pool+".*.memory.memory-buffered&vtitle=Amount_RAM_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_RAM_Usage";
        String poolSwapUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host."+pool+".*.swap.swap-{used%2C}.value%2Ccolor%28"+brand+"."+env+".host."+pool+".*.swap.swap-used&vtitle=Amount_SWAP_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_SWAP_Usage";
        String mspCpuUserUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_User_Usage";
        String mspCpuSystemUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_System_Usage";
        String mspCpuWaitUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_IO_Wait_Usage";
        String mspLoadAvg = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_Load_Average";

        String nxgenCpuUserUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_User_Usage";
        String nxgenCpuSystemUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_System_Usage";
        String nxgenCpuWaitUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"."+env+".host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_IO_Wait_Usage";
        String nxgenLoadAvg = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.oracle-x86_64.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_Load_Average";

        String mongoDBCpuUserUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28"+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_User_Usage";
        String mongoDBCpuSystemUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28"+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_System_Usage";
        String mongoDBCpuWaitUrl = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28"+brand+"."+env+".host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_IO_Wait_Usage";
        String mongoDBLoadAvg = "http://graphite-web.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target="+brand+"."+env+".host.mongodb.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_Load_Average";

        ArrayList<String> list = new ArrayList<String>();
        list.add(gcMarkSweepHeapUsageUrl);
        list.add(gcMarkSweepCollectionUrl);
        list.add(gcParNewCollectionTimeUrl);
        list.add(gcParNewHeapUsageUrl);
        list.add(poolCpuUserUrl);
        list.add(poolCpuSystemUrl);
        list.add(poolCpuIOWaitUrl);
        list.add(poolRamUrl);
        list.add(poolSwapUrl);
        list.add(mspCpuUserUrl);
        list.add(mspCpuSystemUrl);
        list.add(mspCpuWaitUrl);
        list.add(mspLoadAvg);
        list.add(nxgenCpuUserUrl);
        list.add(nxgenCpuSystemUrl);
        list.add(nxgenCpuWaitUrl);
        list.add(nxgenLoadAvg);
        list.add(mongoDBCpuUserUrl);
        list.add(mongoDBCpuSystemUrl);
        list.add(mongoDBCpuWaitUrl);
        list.add(mongoDBLoadAvg);
        return list;
    }


}
