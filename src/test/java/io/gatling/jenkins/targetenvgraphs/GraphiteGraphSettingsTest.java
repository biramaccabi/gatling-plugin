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
package io.gatling.jenkins.targetenvgraphs;

import io.gatling.jenkins.targetenvgraphs.envgraphs.graphite.GraphiteGraphSettings;
import org.junit.Assert;

public class GraphiteGraphSettingsTest {


    @org.junit.Test
    public void testGetParameterWithDefault() {
        GraphiteGraphSettings graphiteGraphSettingsTester = new GraphiteGraphSettings();

        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter("testParameterName"));
        Assert.assertEquals("notNull", graphiteGraphSettingsTester.getParameter("testParameterName", "notNull"));
        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter("testParameterName"));

    }

    @org.junit.Test
    public void testIsValidParameter() {
        GraphiteGraphSettings graphiteGraphSettingsTester = new GraphiteGraphSettings();

        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter("testParam1"));
        graphiteGraphSettingsTester.addParameter("testParam1", "testValue1");
        Assert.assertEquals("testValue1", graphiteGraphSettingsTester.getParameter("testParam1"));

        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter(""));
        graphiteGraphSettingsTester.addParameter("", "valueFromEmptyStringKey");
        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter(""));

        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter(""));
        graphiteGraphSettingsTester.addParameter(null, "valueFromNullKey");
        Assert.assertEquals(null, graphiteGraphSettingsTester.getParameter(""));
    }

    @org.junit.Test
    public void testEquals() {
        String host = "http://tre-stats.internal.shutterfly.com";
        String target = "sfly.kappa.host.app.*.app.app.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String title = "sfly.kappa.host.app.*.app.app.GarbageCollectorSentinel.ParNew.heapUsagePercentage";
        String percent_heap_used = "percent_heap_used";
        String yMax = "100";
        String yMin = "0";

        GraphiteGraphSettings expected= new GraphiteGraphSettings();
        expected.setHost(host);
        expected.setTarget(target);
        expected.setTitle(title);
        expected.setVerticalTitle(percent_heap_used);
        expected.setYMax(yMax);
        expected.setYMin(yMin);


        GraphiteGraphSettings actual= new GraphiteGraphSettings();
        actual.setHost(host);
        actual.setTarget(target);
        actual.setTitle(title);
        actual.setVerticalTitle(percent_heap_used);
        actual.setYMax(yMax);
        actual.setYMin(yMin);

        Assert.assertTrue(actual.equals(expected));
        Assert.assertEquals(actual.hashCode(), expected.hashCode());

    }
}
