package com.excilys.ebi.gatling.jenkins.GraphiteUtilities;

import hudson.model.AbstractBuild;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphiteUTIL {

    public ArrayList<String> getGraphiteUrls(AbstractBuild<?, ?> build){
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

    public String getGraphiteURLGraphOfGCMarkSweepHeapUsage(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage";
        TREGraphiteGraph markSweepHeapUsage = new TREGraphiteGraph();
        return markSweepHeapUsage.getGraphiteGraphForTarget(graphiteTarget, "percent_heap_used", graphiteTarget, "0", "100", startTime, endTime);
    }

    public String getGraphiteURLGraphOfGCMarkSweepCollectionTime(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime";
        TREGraphiteGraph markSweepCollectionTime = new TREGraphiteGraph();
        return markSweepCollectionTime.getGraphiteGraphForTargetNoYMinYMax(graphiteTarget, "collection_time_in_ms", graphiteTarget, startTime, endTime);
    }

    public String getGraphiteURLGraphOfGCParNewHeapUsage(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage&vtitle=percent_heap_used";
        TREGraphiteGraph parNewHeapUsage = new TREGraphiteGraph();
        return parNewHeapUsage.getGraphiteGraphForTarget(graphiteTarget, "percent_heap_used", graphiteTarget, "0", "100", startTime, endTime);
    }

    public String getGraphiteURLGraphOfGCParNewCollectionTime(Calendar startTime, Calendar endTime) {
        String graphiteTarget = "sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime";
        TREGraphiteGraph parNewCollectionTime = new TREGraphiteGraph();
        return parNewCollectionTime.getGraphiteGraphForTargetNoYMinYMax(graphiteTarget, "collection_time_in_ms", graphiteTarget, startTime, endTime);
    }

    public String getGraphiteURLGraphOfAppPoolCpuUsage(Calendar startTime, Calendar endTime) {
         String target = "sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle";
        IOPSGraphiteGraph appPoolCpuUsage = new IOPSGraphiteGraph();
        return appPoolCpuUsage.getGraphiteGraphForTarget(target, "cpu_percent_usage", "APP_Pool_CPU_Usage", "", "", startTime, endTime);
    }

    public String getGraphiteURLGraphOfAppPoolMemmoryUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered";
        IOPSGraphiteGraph appPoolMemoryUsage = new IOPSGraphiteGraph();
        return appPoolMemoryUsage.getGraphiteGraphForTarget(target, "ram_usage", "APP_Pool_RAM_Usage", "", "", startTime, endTime);
    }

    public String getGraphiteURLGraphOfAppPoolSwapUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used";
        IOPSGraphiteGraph appPoolSwapUsage = new IOPSGraphiteGraph();
        return appPoolSwapUsage.getGraphiteGraphForTarget(target, "swap_usage", "APP_Pool_Swap_Usage", "", "", startTime, endTime);
    }

    public String getGraphiteURLGraphOfApiPoolCpuUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.api.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.api.*.aggregation-cpu-average.cpu-idle";
        IOPSGraphiteGraph apiPoolCpuUsage = new IOPSGraphiteGraph();
        return apiPoolCpuUsage.getGraphiteGraphForTarget(target, "cpu_percent_usage", "API_Pool_CPU_Usage", "", "", startTime, endTime);
    }

    public String getGraphiteURLGraphOfApiPoolMemmoryUsage(Calendar startTime, Calendar endTime) {
        String target = "sfly.foxtrot.host.api.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.api.*.memory.memory-buffered";
        IOPSGraphiteGraph apiPoolMemoryUsage = new IOPSGraphiteGraph();
        return apiPoolMemoryUsage.getGraphiteGraphForTarget(target, "ram_usage", "API_Pool_RAM_Usage", "", "", startTime, endTime);
    }

}