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
    public static final String LONG_POOL_FULLNAME = "appserver";
    public static final String LONG_POOL_SHORTNAME = "app";

    public static final String SHORT_POOL_FULLNAME = "wsserver";
    public static final String SHORT_POOL_SHORTNAME = "ws";

    @Test
    public void testGetTargetWithLongPoolFullName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, LONG_POOL_FULLNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortPoolShortName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, LONG_POOL_SHORTNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ LONG_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);

    }

    @Test
    public void testGetTargetWithShortestPooFulllName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, SHORT_POOL_FULLNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

    @Test
    public void testGetTargetWithShortestPoolShortName() {
        String getTarget = GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(ENV, SHORT_POOL_SHORTNAME);
        String expectedTarget = "sfly."+ ENV +".host."+ SHORT_POOL_SHORTNAME +".*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        Assert.assertEquals(expectedTarget, getTarget);
    }

    @Test
    public void testGetTitleWithLongPoolFulllName() {
        String getTitle = GraphiteTargetEnum.POOL_RAM_USAGE.getTitle(ENV, LONG_POOL_FULLNAME);
        String expectedTitle = LONG_POOL_SHORTNAME +"_POOL_RAM_Usage";
        Assert.assertEquals(expectedTitle, getTitle);
    }

    @Test
    public void testGetTitleWithLongPoolShortName() {
        String getTitle = GraphiteTargetEnum.POOL_RAM_USAGE.getTitle(ENV, LONG_POOL_SHORTNAME);
        String expectedTitle = LONG_POOL_SHORTNAME +"_POOL_RAM_Usage";
        Assert.assertEquals(expectedTitle, getTitle);
    }

    @Test
    public void testGetTitleWithShortestPooFulllName() {
        String getTitle = GraphiteTargetEnum.POOL_RAM_USAGE.getTitle(ENV, SHORT_POOL_FULLNAME);
        String expectedTitle = SHORT_POOL_SHORTNAME +"_POOL_RAM_Usage";
        Assert.assertEquals(expectedTitle, getTitle);
    }

    @Test
    public void testGetTitleWithShortestPooShortlName() {
        String getTitle = GraphiteTargetEnum.POOL_RAM_USAGE.getTitle(ENV, SHORT_POOL_SHORTNAME);
        String expectedTitle = SHORT_POOL_SHORTNAME +"_POOL_RAM_Usage";
        Assert.assertEquals(expectedTitle, getTitle);
    }
}
