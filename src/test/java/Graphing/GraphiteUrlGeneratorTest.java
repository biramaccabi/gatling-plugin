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
package Graphing;

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.GraphCriteria;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteUrlGenerator;
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
public class GraphiteUrlGeneratorTest {

    @Mock
    GraphCriteria foxtrotAppCriteria;

    @Mock
    GraphCriteria foxtrotNonAppCriteria;

    @Before
    public void setup() {
        foxtrotAppCriteria = mock(GraphCriteria.class);
        when(foxtrotAppCriteria.getEnvironmentName()).thenReturn("foxtrot");
        when(foxtrotAppCriteria.getPoolName()).thenReturn("appserver");
        when(foxtrotAppCriteria.getDuration()).thenReturn(getDuration());
        when(foxtrotAppCriteria.getEndTime()).thenReturn(getEndTime());
        when(foxtrotAppCriteria.getStartTime()).thenReturn(getStartTime());

        foxtrotNonAppCriteria = mock(GraphCriteria.class);
        when(foxtrotNonAppCriteria.getEnvironmentName()).thenReturn("foxtrot");
        when(foxtrotNonAppCriteria.getPoolName()).thenReturn("apiserver");
        when(foxtrotNonAppCriteria.getDuration()).thenReturn(getDuration());
        when(foxtrotNonAppCriteria.getEndTime()).thenReturn(getEndTime());
        when(foxtrotNonAppCriteria.getStartTime()).thenReturn(getStartTime());
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotApp() {
        GraphiteUrlGenerator testGenerator = new GraphiteUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(foxtrotAppCriteria);

        // should come back with 4 graphs for GC and 3 for App Pool
        Assert.assertEquals(7, graphUrls.size());

        ArrayList<String> expectedUrls = getListFoxtrotAppUrls();
        for(String url: graphUrls) {
            Assert.assertTrue(expectedUrls.contains(url));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotNonApp() {
        GraphiteUrlGenerator testGenerator = new GraphiteUrlGenerator();

        ArrayList<String> graphUrls = testGenerator.getUrlsForCriteria(foxtrotNonAppCriteria);

        // should come back with 0, we only support foxtrot/app pool
        Assert.assertEquals(0, graphUrls.size());
    }

    public Calendar getStartTime() {
        Calendar startTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:00 am
        startTime.set(2000, Calendar.JANUARY, 1, 8, 0, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }

    public Calendar getEndTime() {
        Calendar endTime = Calendar.getInstance();
        // set date to Jan 1, 2000 at 8:30 am
        endTime.set(2000, Calendar.JANUARY, 1, 8, 45, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    public long getDuration() {
        return getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis();
    }

    private ArrayList<String> getListFoxtrotAppUrls() {
        String foxtrotGCMarkSweepHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        String foxtrotGCMarkSweepCollectionUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        String foxtrotGCParNewHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String foxtrotGCParNewCollectionTimeUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime";
        String foxtrotAppCpuUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_CPU_Usage";
        String foxtrotAppRamUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered&vtitle=ram_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_ram_Usage";
        String foxtrotAppSwapUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=08:00_20000101&until=08:45_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used&vtitle=swap_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_swap_Usage";

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
