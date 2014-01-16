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

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteMetricBuilder;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteMetrics;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteTargetEnum;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.GraphCriteria;
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
public class GraphiteMetricBuilderTest {

    @Mock
    GraphCriteria criteriaFoxtrotApp;

    @Mock
    GraphCriteria criteriaFoxtrotNonApp;

    @Before
    public void setup() {
        criteriaFoxtrotApp = mock(GraphCriteria.class);
        when(criteriaFoxtrotApp.getDuration()).thenReturn(getDuration());
        when(criteriaFoxtrotApp.getEndTime()).thenReturn(getEndTime());
        when(criteriaFoxtrotApp.getEnvironmentName()).thenReturn("foxtrot");
        when(criteriaFoxtrotApp.getPoolName()).thenReturn("appserver");
        when(criteriaFoxtrotApp.getStartTime()).thenReturn(getStartTime());

        criteriaFoxtrotNonApp = mock(GraphCriteria.class);
        when(criteriaFoxtrotNonApp.getDuration()).thenReturn(getDuration());
        when(criteriaFoxtrotNonApp.getEndTime()).thenReturn(getEndTime());
        when(criteriaFoxtrotNonApp.getEnvironmentName()).thenReturn("foxtrot");
        when(criteriaFoxtrotNonApp.getPoolName()).thenReturn("apiserver");
        when(criteriaFoxtrotNonApp.getStartTime()).thenReturn(getStartTime());
    }

    @org.junit.Test
    public void testGetAppUsageFoxtrotAppCriteria() {
        GraphiteMetricBuilder testBuilder = new GraphiteMetricBuilder();
        GraphiteMetrics generatedCPU = testBuilder.getAppUsage(criteriaFoxtrotApp, "CPU");

        GraphiteMetrics expected = this.getFoxtrotAppCpuMetrics();
        assertGraphiteMetricsEquality(expected, generatedCPU);

        GraphiteMetrics generatedRAM = testBuilder.getAppUsage(criteriaFoxtrotApp, "ram");
        expected = this.getFoxtrotAppRamMetrics();
        assertGraphiteMetricsEquality(expected, generatedRAM);

        GraphiteMetrics generatedSwap = testBuilder.getAppUsage(criteriaFoxtrotApp, "swap");
        expected = this.getFoxtrotAppSwapMetrics();
        assertGraphiteMetricsEquality(expected, generatedSwap);
    }

    @org.junit.Test
    public void testGetAppUsageFoxtrotNonAppCriteria() {
        GraphiteMetricBuilder testBuilder = new GraphiteMetricBuilder();
        ArrayList<GraphiteMetrics> generated = testBuilder.getAppMetrics(criteriaFoxtrotNonApp);

        // we don't currently support api
        Assert.assertEquals(0, generated.size());
    }

    private void assertGraphiteMetricsEquality(GraphiteMetrics generated, GraphiteMetrics expected) {
        Assert.assertEquals(expected.getHost(), generated.getHost());
        Assert.assertEquals(expected.getTarget(), generated.getTarget());
        Assert.assertEquals(expected.getTitle(), generated.getTitle());
        Assert.assertEquals(expected.getVerticalTitle(), generated.getVerticalTitle());
        Assert.assertEquals(expected.getYMax(), generated.getYMax());
        Assert.assertEquals(expected.getYMin(), generated.getYMin());
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

    public GraphiteMetrics getFoxtrotAppCpuMetrics() {
        GraphiteMetrics appCpu = new GraphiteMetrics();
        appCpu.setHost("http://graphite.internal.shutterfly.com:443/");
        appCpu.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLCPU_USAGE.getTarget());
        appCpu.setYMax("");
        appCpu.setYMin("");
        appCpu.setTitle("APP_CPU_Usage");
        appCpu.setVerticalTitle("CPU_usage");

        return appCpu;
    }

    public GraphiteMetrics getFoxtrotAppRamMetrics() {
        GraphiteMetrics appCpu = new GraphiteMetrics();
        appCpu.setHost("http://graphite.internal.shutterfly.com:443/");
        appCpu.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLMEMORY_USAGE.getTarget());
        appCpu.setYMax("");
        appCpu.setYMin("");
        appCpu.setTitle("APP_ram_Usage");
        appCpu.setVerticalTitle("ram_usage");

        return appCpu;
    }

    public GraphiteMetrics getFoxtrotAppSwapMetrics() {
        GraphiteMetrics appCpu = new GraphiteMetrics();
        appCpu.setHost("http://graphite.internal.shutterfly.com:443/");
        appCpu.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLSWAP_USAGE.getTarget());
        appCpu.setYMax("");
        appCpu.setYMin("");
        appCpu.setTitle("APP_swap_Usage");
        appCpu.setVerticalTitle("swap_usage");

        return appCpu;
    }
}
