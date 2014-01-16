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

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteMetrics;
import org.junit.Assert;

public class GraphiteMetricsTest {


    @org.junit.Test
    public void testGetParameterWithDefault() {
        GraphiteMetrics graphiteMetricsTester = new GraphiteMetrics();

        Assert.assertEquals(null, graphiteMetricsTester.getParameter("testParameterName"));
        Assert.assertEquals("notNull", graphiteMetricsTester.getParameter("testParameterName", "notNull"));
        Assert.assertEquals(null, graphiteMetricsTester.getParameter("testParameterName"));

    }

    @org.junit.Test
    public void testIsValidParameter() {
        GraphiteMetrics graphiteMetricsTester = new GraphiteMetrics();

        Assert.assertEquals(null, graphiteMetricsTester.getParameter("testParam1"));
        graphiteMetricsTester.addParameter("testParam1", "testValue1");
        Assert.assertEquals("testValue1", graphiteMetricsTester.getParameter("testParam1"));

        Assert.assertEquals(null, graphiteMetricsTester.getParameter(""));
        graphiteMetricsTester.addParameter("", "valueFromNullKey");
        Assert.assertEquals(null, graphiteMetricsTester.getParameter(""));
    }
}
