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
    BuildInfoForTargetEnvGraph foxtrotAppCriteria;

    @Mock
    BuildInfoForTargetEnvGraph foxtrotNonAppCriteria;

    @Before
    public void setup() {
        foxtrotAppCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(foxtrotAppCriteria.getEnvironmentName()).thenReturn("foxtrot");
        when(foxtrotAppCriteria.getPoolName()).thenReturn("appserver");

        when(foxtrotAppCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(foxtrotAppCriteria.getGraphStartTime()).thenReturn(getStartTime());

        foxtrotNonAppCriteria = mock(BuildInfoForTargetEnvGraph.class);
        when(foxtrotNonAppCriteria.getEnvironmentName()).thenReturn("foxtrot");
        when(foxtrotNonAppCriteria.getPoolName()).thenReturn("apiserver");

        when(foxtrotNonAppCriteria.getGraphEndTime()).thenReturn(getEndTime());
        when(foxtrotNonAppCriteria.getGraphStartTime()).thenReturn(getStartTime());
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotApp() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(foxtrotAppCriteria);

        Assert.assertEquals("should come back with 4 graphs for GC and 3 for App Pool", 7, graphUrls.size());

        ArrayList<String> expectedUrls = getListFoxtrotAppUrls();
        for(String url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotNonApp() {
        BuildInfoBasedUrlGenerator testGenerator = new BuildInfoBasedUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(foxtrotNonAppCriteria);

        Assert.assertEquals("should come back with 0, we only support foxtrot/app pool", 0, graphUrls.size());
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
        String foxtrotAppCpuUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_Percent_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_CPU_Usage";
        String foxtrotAppRamUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered&vtitle=Amount_RAM_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_RAM_Usage";
        String foxtrotAppSwapUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used&vtitle=Amount_SWAP_Used&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_POOL_SWAP_Usage";

        ArrayList<String> list = new ArrayList<String>();
        list.add(foxtrotGCMarkSweepHeapUsageUrl);
        list.add(foxtrotGCMarkSweepCollectionUrl);
        list.add(foxtrotGCParNewCollectionTimeUrl);
        list.add(foxtrotGCParNewHeapUsageUrl);
        list.add(foxtrotAppCpuUrl);
        list.add(foxtrotAppRamUrl);
        list.add(foxtrotAppSwapUrl);
        return list;
    }


}
