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
import org.junit.Test;

public class GraphiteTargetEnumTest {

    public static final String ENV = "foxtrot";
    public static final String LONGPOOLFULLNAME = "appserver";
    public static final String LONGPOOLSHORTNAME = "app";

    public static final String SHORTESTPOOLFULLNAME = "wsserver";
    public static final String SHORTESTPOOLSHORTNAME = "ws";

    @Test
    public void testGetTargetWithLongPoolname() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, LONGPOOLFULLNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ LONGPOOLSHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortPoolName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, LONGPOOLSHORTNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ LONGPOOLSHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortestPoolName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, SHORTESTPOOLFULLNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ SHORTESTPOOLSHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }
}
