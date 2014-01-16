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
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.TargetGraphGenerator;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
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
public class TargetGraphGeneratorTest {

    @Mock
    AbstractBuild foxtrotAppBuild;

    @Mock
    AbstractBuild foxtrotNonAppBuild;

    @Mock
    AbstractBuild nonFoxtrotAppBuild;

    @Mock
    AbstractBuild nonFoxtrotNonAppBuild;

    @Mock
    AbstractProject foxtrotAppProject;

    @Mock
    AbstractProject foxtrotNonAppProject;

    @Mock
    AbstractProject nonFoxtrotAppProject;

    @Mock
    AbstractProject nonFoxtrotNonAppProject;


    @Before
    public void setup() {
        foxtrotAppBuild = mock(AbstractBuild.class);
        foxtrotAppProject = mock(AbstractProject.class);
        when(foxtrotAppProject.getName()).thenReturn("Web_Performance_Tests-foxtrot-appserver_SomeSimulation");
        when(foxtrotAppBuild.getProject()).thenReturn(foxtrotAppProject);
        when(foxtrotAppBuild.getTimestamp()).thenReturn(this.getStartTime());
        when(foxtrotAppBuild.getDuration()).thenReturn(this.getDuration());

        foxtrotNonAppBuild = mock(AbstractBuild.class);
        foxtrotNonAppProject = mock(AbstractProject.class);
        when(foxtrotNonAppProject.getName()).thenReturn("Web_Performance_Tests-foxtrot-apiserver_SomeSimulation");
        when(foxtrotNonAppBuild.getProject()).thenReturn(foxtrotNonAppProject);
        when(foxtrotNonAppBuild.getTimestamp()).thenReturn(this.getStartTime());
        when(foxtrotNonAppBuild.getDuration()).thenReturn(this.getDuration());

        nonFoxtrotAppBuild = mock(AbstractBuild.class);
        nonFoxtrotAppProject = mock(AbstractProject.class);
        when(nonFoxtrotAppProject.getName()).thenReturn("Web_Performance_Tests-stage-appserver_SomeSimulation");
        when(nonFoxtrotAppBuild.getProject()).thenReturn(nonFoxtrotAppProject);

        nonFoxtrotNonAppBuild = mock(AbstractBuild.class);
        nonFoxtrotNonAppProject = mock(AbstractProject.class);
        when(nonFoxtrotNonAppProject.getName()).thenReturn("Web_Performance_Tests-stage-apiserver_SomeSimulation");
        when(nonFoxtrotNonAppBuild.getProject()).thenReturn(nonFoxtrotNonAppProject);
        when(nonFoxtrotNonAppBuild.getTimestamp()).thenReturn(this.getStartTime());
        when(nonFoxtrotNonAppBuild.getDuration()).thenReturn(this.getDuration());
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotApp() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        ArrayList<String> graphUrls = testGenerator.getGraphUrls(foxtrotAppBuild);

        // should come back with 4 graphs for GC and 3 for App Pool
        Assert.assertEquals(7, graphUrls.size());

        for(String url: graphUrls) {
            Assert.assertTrue(getListFoxtrotAppUrls().contains(url));
        }
    }

    @org.junit.Test
    public void testGetGraphUrlsForFoxtrotNonApp() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        ArrayList<String> graphUrls = testGenerator.getGraphUrls(foxtrotNonAppBuild);

        // currently, we only support foxtrot environment and app pools
        Assert.assertEquals(0, graphUrls.size());

    }

    @org.junit.Test
    public void testGetGraphUrlsForNonFoxtrotNonApp() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        ArrayList<String> graphUrls = testGenerator.getGraphUrls(nonFoxtrotNonAppBuild);

        // currently, we only support foxtrot environment and app pools
        Assert.assertEquals(0, graphUrls.size());
    }

    @org.junit.Test
    public void testGetCriteriaFromFoxtrotAppBuild() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();
        GraphCriteria generatedCriteria = testGenerator.getCriteriaFromBuild(foxtrotAppBuild);

        GraphCriteria expected = new GraphCriteria();
        expected.setPoolName("appserver");
        expected.setEnvironmentName("foxtrot");
        expected.setDuration(this.getDuration());

        Calendar expectedStartTime = getStartTime();
        expectedStartTime.add(Calendar.MINUTE, testGenerator.getGraphStartBufferTimeMinutes());
        expected.setStartTime(expectedStartTime);

        Calendar expectedEndTime = getEndTime();
        expectedEndTime.add(Calendar.MINUTE, testGenerator.getGraphEndBufferTimeMinutes());
        expected.setEndTime(expectedEndTime);

        Assert.assertEquals(expected.getEnvironmentName(), generatedCriteria.getEnvironmentName());
        Assert.assertEquals(expected.getPoolName(), generatedCriteria.getPoolName());
        Assert.assertEquals(expected.getStartTime(), generatedCriteria.getStartTime());
        Assert.assertEquals(expected.getEndTime(), generatedCriteria.getEndTime());
        Assert.assertEquals(expected.getDuration(), generatedCriteria.getDuration());
    }

    @org.junit.Test
    public void testGetCriteriaFromNonFoxtrotNonAppBuild() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();
        GraphCriteria generatedCriteria = testGenerator.getCriteriaFromBuild(nonFoxtrotNonAppBuild);

        GraphCriteria expected = new GraphCriteria();
        expected.setPoolName("apiserver");
        expected.setEnvironmentName("stage");
        expected.setDuration(this.getDuration());

        Calendar expectedStartTime = getStartTime();
        expectedStartTime.add(Calendar.MINUTE, testGenerator.getGraphStartBufferTimeMinutes());
        expected.setStartTime(expectedStartTime);

        Calendar expectedEndTime = getEndTime();
        expectedEndTime.add(Calendar.MINUTE, testGenerator.getGraphEndBufferTimeMinutes());
        expected.setEndTime(expectedEndTime);

        Assert.assertEquals(expected.getEnvironmentName(), generatedCriteria.getEnvironmentName());
        Assert.assertEquals(expected.getPoolName(), generatedCriteria.getPoolName());
        Assert.assertEquals(expected.getStartTime(), generatedCriteria.getStartTime());
        Assert.assertEquals(expected.getEndTime(), generatedCriteria.getEndTime());
        Assert.assertEquals(expected.getDuration(), generatedCriteria.getDuration());
    }

    @org.junit.Test
    public void testGetFoxtrotEnvFromBuild(){
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        String expectedEnvironment = "foxtrot";
        String generatedEnvironment = testGenerator.getEnvFromBuild(foxtrotAppBuild);
        Assert.assertEquals(expectedEnvironment, generatedEnvironment);
    }

    @org.junit.Test
    public void testGetNonFoxtrotEnvFromBuild(){
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        String expectedEnvironment = "stage";
        String generatedEnvironment = testGenerator.getEnvFromBuild(nonFoxtrotAppBuild);
        Assert.assertEquals(expectedEnvironment, generatedEnvironment);
    }

    @org.junit.Test
    public void testGetFoxtrotPoolFromBuild() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        String expectedPool = "appserver";
        String generatedPool = testGenerator.getPoolFromBuild(foxtrotAppBuild);
        Assert.assertEquals(expectedPool, generatedPool);
    }

    @org.junit.Test
    public void testGetNonFoxtrotPoolFromBuild() {
        TargetGraphGenerator testGenerator = new TargetGraphGenerator();

        String expectedPool = "apiserver";
        String generatedPool = testGenerator.getPoolFromBuild(nonFoxtrotNonAppBuild);

        Assert.assertEquals(expectedPool, generatedPool);

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
        String foxtrotGCMarkSweepHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        String foxtrotGCMarkSweepCollectionUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        String foxtrotGCParNewHeapUsageUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used&fgcolor=000000&bgcolor=FFFFFF&yMin=0&yMax=100&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String foxtrotGCParNewCollectionTimeUrl = "http://tre-stats.internal.shutterfly.com/render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime&vtitle=collection_time_in_ms&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime";
        String foxtrotAppCpuUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle&vtitle=CPU_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_CPU_Usage";
        String foxtrotAppRamUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered&vtitle=ram_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_ram_Usage";
        String foxtrotAppSwapUrl = "http://graphite.internal.shutterfly.com:443//render?width=600&from=07:55_20000101&until=08:50_20000101&height=400&lineMode=connected&target=sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used&vtitle=swap_usage&fgcolor=000000&bgcolor=FFFFFF&_uniq=0.06565146492917762&title=APP_swap_Usage";

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
