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
    BuildInfoForTargetEnvGraph supportedEnvPoolCriteria;

    @Mock
    BuildInfoForTargetEnvGraph unsupportedEnvPoolCriteria;

    @Before
    public void setup() {
        supportedEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(supportedEnvPoolCriteria.getEnvironmentName()).thenReturn("foxtrot");
        when(supportedEnvPoolCriteria.getPoolName()).thenReturn("appserver");

        when(supportedEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(supportedEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());

        unsupportedEnvPoolCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(unsupportedEnvPoolCriteria.getEnvironmentName()).thenReturn("fakeenv");
        when(unsupportedEnvPoolCriteria.getPoolName()).thenReturn("fakeserver");

        when(unsupportedEnvPoolCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(unsupportedEnvPoolCriteria.getGraphStartTime()).thenReturn(getStartTime());
    }

    @org.junit.Test
    public void testGetGraphUrlsForSupportedEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(supportedEnvPoolCriteria);

        String sizeAssertString = "should come back with: GC 4, Pool 5, MSP 4, NXGEN 4, Mongo 4.  21 Total.";
        Assert.assertEquals(sizeAssertString, 21, graphUrls.size());

        ArrayList<String> expectedUrls = getListFoxtrotAppUrls();

        for(String url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }

        for(String expectedURl: expectedUrls) {
            Assert.assertTrue(graphUrls.contains(expectedURl));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForUnsupportedEnvPool() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(unsupportedEnvPoolCriteria);

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

    private ArrayList<String> getListFoxtrotAppUrls() {
        String foxtrotGCMarkSweepHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        String foxtrotGCMarkSweepCollectionUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        String foxtrotGCParNewHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String foxtrotGCParNewCollectionTimeUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime";
        String foxtrotAppCpuUserUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_User_Usage";
        String foxtrotAppCpuSystemUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_System_Usage";
        String foxtrotAppCpuIOWaitUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=APP_POOL_CPU_IO_Wait_Usage";
        String foxtrotAppRamUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered&vtitle=Amount_RAM_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_RAM_Usage";
        String foxtrotAppSwapUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used&vtitle=Amount_SWAP_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_SWAP_Usage";
        String foxtrotMspCpuUserUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_User_Usage";
        String foxtrotMspCpuSystemUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_System_Usage";
        String foxtrotMspCpuWaitUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_CPU_IO_Wait_Usage";
        String foxtrotMspLoadAvg = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MSP_DATABASE_Load_Average";

        String foxtrotNxGenCpuUserUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_User_Usage";
        String foxtrotNxGenCpuSystemUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_System_Usage";
        String foxtrotNxGenCpuWaitUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.foxtrot.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_CPU_IO_Wait_Usage";
        String foxtrotNxGenLoadAvg = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.oracle-x86_64.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=NXGEN_DATABASE_Load_Average";

        String foxtrotMongoDBCpuUserUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_User_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_User_Usage";
        String foxtrotMongoDBCpuSystemUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_System_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_System_Usage";
        String foxtrotMongoDBCpuWaitUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.foxtrot.host.mongodb.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_IO_Wait_Used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_CPU_IO_Wait_Usage";
        String foxtrotMongoDBLoadAvg = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.mongodb.*.load.load.*term&vtitle=Load_Average&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=MongoDB_Load_Average";

        ArrayList<String> list = new ArrayList<String>();
        list.add(foxtrotGCMarkSweepHeapUsageUrl);
        list.add(foxtrotGCMarkSweepCollectionUrl);
        list.add(foxtrotGCParNewCollectionTimeUrl);
        list.add(foxtrotGCParNewHeapUsageUrl);
        list.add(foxtrotAppCpuUserUrl);
        list.add(foxtrotAppCpuSystemUrl);
        list.add(foxtrotAppCpuIOWaitUrl);
        list.add(foxtrotAppRamUrl);
        list.add(foxtrotAppSwapUrl);
        list.add(foxtrotMspCpuUserUrl);
        list.add(foxtrotMspCpuSystemUrl);
        list.add(foxtrotMspCpuWaitUrl);
        list.add(foxtrotMspLoadAvg);
        list.add(foxtrotNxGenCpuUserUrl);
        list.add(foxtrotNxGenCpuSystemUrl);
        list.add(foxtrotNxGenCpuWaitUrl);
        list.add(foxtrotNxGenLoadAvg);
        list.add(foxtrotMongoDBCpuUserUrl);
        list.add(foxtrotMongoDBCpuSystemUrl);
        list.add(foxtrotMongoDBCpuWaitUrl);
        list.add(foxtrotMongoDBLoadAvg);
        return list;
    }


}
