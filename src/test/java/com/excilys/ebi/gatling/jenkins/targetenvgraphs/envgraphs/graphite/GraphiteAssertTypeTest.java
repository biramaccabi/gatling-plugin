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

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite.
        TrendGraphBuilder.GRAPHITE_ASSERT_TYPE;

@RunWith(Parameterized.class)
public class GraphiteAssertTypeTest {

    private final GRAPHITE_ASSERT_TYPE graphiteAssertType;
    private final String gatlingAssertType;

    public GraphiteAssertTypeTest(
            GRAPHITE_ASSERT_TYPE graphiteAssertType,
            String gatlingAssertType){
        this.graphiteAssertType = graphiteAssertType;
        this.gatlingAssertType = gatlingAssertType;
    }

    @org.junit.Test
    public void test_fromGatlingAssertType(){
        Assert.assertEquals(graphiteAssertType,
                GRAPHITE_ASSERT_TYPE.fromGatlingAssertType(gatlingAssertType));
    }

    @Parameterized.Parameters(name = "{0}")
    public static java.util.Collection<Object[]> data() {
        Object[][] params = new Object[][] {
                {GRAPHITE_ASSERT_TYPE.ko, "percentage of requests KO"},
                {GRAPHITE_ASSERT_TYPE.max, "max response time"},
                {GRAPHITE_ASSERT_TYPE.mean, "mean response time"},
                {GRAPHITE_ASSERT_TYPE.min, "min response time"},
                {GRAPHITE_ASSERT_TYPE.percentiles95, "95th percentile response time"},
                {GRAPHITE_ASSERT_TYPE.stddev, "standard deviation response time"},
                {GRAPHITE_ASSERT_TYPE.throughput, "requests per second"},
        };
        return Arrays.asList(params);
    }



}
