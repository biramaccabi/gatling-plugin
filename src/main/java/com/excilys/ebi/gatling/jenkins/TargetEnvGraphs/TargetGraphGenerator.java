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
package com.excilys.ebi.gatling.jenkins.TargetEnvGraphs;

import hudson.model.AbstractBuild;

import java.util.ArrayList;
import java.util.Calendar;

public class TargetGraphGenerator {

    public ArrayList<String> getGraphitUrls(AbstractBuild<?, ?> build){
        ArrayList<String> result = new ArrayList<String>();
        int buildNumber = build.getNumber();

        Calendar startTime = build.getTimestamp();
        Calendar endTime = build.getTimestamp();

        endTime.setTimeInMillis(endTime.getTimeInMillis() + build.getDuration());

        startTime.add(Calendar.MINUTE, -5);
        endTime.add(Calendar.MINUTE, 5);

        result.add(getGraphiteURLGraphOfGCMarkSweepHeapUsage(startTime, endTime));
        result.add(getGraphiteURLGraphOfGCMarkSweepCollectionTime(startTime,endTime));
        result.add(getGraphiteURLGraphOfGCParNewHeapUsage(startTime, endTime));
        result.add(getGraphiteURLGraphOfGCParNewCollectionTime(startTime, endTime));
        result.add(getGraphiteURLGraphOfAppPoolCpuUsage(startTime, endTime));
        result.add(getGraphiteURLGraphOfAppPoolMemmoryUsage(startTime,endTime));
        result.add(getGraphiteURLGraphOfAppPoolSwapUsage(startTime, endTime));
        // No API data collected for foxtrot
        //result.add(getGraphiteURLGraphOfApiPoolCpuUsage(startTime, endTime));
        //result.add(getGraphiteURLGraphOfApiPoolMemmoryUsage(startTime, endTime));

        return result;
    }

    private String getGraphiteURLGraphOfGCMarkSweepHeapUsage(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        TREGraphiteGraph markSweepHeapUsage = new TREGraphiteGraph();
        return markSweepHeapUsage.getGraphiteGraphForTarget(graphiteTarget, "percent_heap_used", graphiteTarget, "0", "100", startTime, endTime);
    }

    private String getGraphiteURLGraphOfGCMarkSweepCollectionTime(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        TREGraphiteGraph markSweepCollectionTime = new TREGraphiteGraph();
        return markSweepCollectionTime.getGraphiteGraphForTargetNoYMinYMax(graphiteTarget, "collection_time_in_ms", graphiteTarget, startTime, endTime);
    }

    private String getGraphiteURLGraphOfGCParNewHeapUsage(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used";
        TREGraphiteGraph parNewHeapUsage = new TREGraphiteGraph();
        return parNewHeapUsage.getGraphiteGraphForTarget(graphiteTarget, "percent_heap_used", graphiteTarget, "0", "100", startTime, endTime);
    }

    private String getGraphiteURLGraphOfGCParNewCollectionTime(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime";
        TREGraphiteGraph parNewCollectionTime = new TREGraphiteGraph();
        return parNewCollectionTime.getGraphiteGraphForTargetNoYMinYMax(graphiteTarget, "collection_time_in_ms", graphiteTarget, startTime, endTime);
    }

    private String getGraphiteURLGraphOfAppPoolCpuUsage(Calendar startTime, Calendar endTime) {
         String target = "sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle";
        IOPSGraphiteGraph appPoolCpuUsage = new IOPSGraphiteGraph();
        return appPoolCpuUsage.getGraphiteGraphForTarget(target, "cpu_percent_usage", "APP_Pool_CPU_Usage", "", "", startTime, endTime);
    }

    private String getGraphiteURLGraphOfAppPoolMemmoryUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered";
        IOPSGraphiteGraph appPoolMemoryUsage = new IOPSGraphiteGraph();
        return appPoolMemoryUsage.getGraphiteGraphForTarget(target, "ram_usage", "APP_Pool_RAM_Usage", "", "", startTime, endTime);
    }

    private String getGraphiteURLGraphOfAppPoolSwapUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used";
        IOPSGraphiteGraph appPoolSwapUsage = new IOPSGraphiteGraph();
        return appPoolSwapUsage.getGraphiteGraphForTarget(target, "swap_usage", "APP_Pool_Swap_Usage", "", "", startTime, endTime);
    }

    private String getGraphiteURLGraphOfApiPoolCpuUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.api.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.api.*.aggregation-cpu-average.cpu-idle";
        IOPSGraphiteGraph apiPoolCpuUsage = new IOPSGraphiteGraph();
        return apiPoolCpuUsage.getGraphiteGraphForTarget(target, "cpu_percent_usage", "API_Pool_CPU_Usage", "", "", startTime, endTime);
    }

    private String getGraphiteURLGraphOfApiPoolMemmoryUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.api.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.api.*.memory.memory-buffered";
        IOPSGraphiteGraph apiPoolMemoryUsage = new IOPSGraphiteGraph();
        return apiPoolMemoryUsage.getGraphiteGraphForTarget(target, "ram_usage", "API_Pool_RAM_Usage", "", "", startTime, endTime);
    }

}