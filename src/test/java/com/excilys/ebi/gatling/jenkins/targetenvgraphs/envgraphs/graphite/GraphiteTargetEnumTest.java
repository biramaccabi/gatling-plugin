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

    public static final String SFLYENV = "kappa";
    public static final String TPENV = "lnp";
    public static final String LONG_POOL_FULLNAME = "appserver";
    public static final String LONG_POOL_SHORTNAME = "app";

    public static final String SHORT_POOL_FULLNAME = "wsserver";
    public static final String SHORT_POOL_SHORTNAME = "ws";

    public static final String BRAND_SFLY = "sfly";
    public static final String BRAND_TP = "tp";

    @Test
    public void testGetTargetWithLongPoolFullNameSFLY() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_SFLY, SFLYENV, LONG_POOL_FULLNAME);
        String expectedTarget = BRAND_SFLY + "." + SFLYENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortPoolShortNameSFLY() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_SFLY, SFLYENV, LONG_POOL_SHORTNAME);
        String expectedTarget = BRAND_SFLY + "." + SFLYENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortestPooFulllNameSFLY() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_SFLY, SFLYENV, SHORT_POOL_FULLNAME);
        String expectedTarget = BRAND_SFLY + "." + SFLYENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

    @Test
    public void testGetTargetWithShortestPoolShortNameSFLY() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_SFLY, SFLYENV, SHORT_POOL_SHORTNAME);
        String expectedTarget = BRAND_SFLY + "." + SFLYENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

    @Test
    public void testGetTargetWithLongPoolFullNameTP() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_TP, TPENV, LONG_POOL_FULLNAME);
        String expectedTarget = BRAND_TP + "." + TPENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortPoolShortNameTP() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_TP, TPENV, LONG_POOL_SHORTNAME);
        String expectedTarget = BRAND_TP + "." + TPENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortestPooFulllNameTP() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_TP, TPENV, SHORT_POOL_FULLNAME);
        String expectedTarget = BRAND_TP + "." + TPENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

    @Test
    public void testGetTargetWithShortestPoolShortNameTP() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(BRAND_TP, TPENV, SHORT_POOL_SHORTNAME);
        String expectedTarget = BRAND_TP + "." + TPENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

}
