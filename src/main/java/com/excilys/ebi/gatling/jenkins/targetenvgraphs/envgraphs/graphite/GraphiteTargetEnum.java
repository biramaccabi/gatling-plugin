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


public enum GraphiteTargetEnum {
    POOL_CPU_USER_USAGE("sfly.{$env}.host.{$pool}.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.{$env}." +
            "host.{$pool}.*.aggregation-cpu-average.cpu-idle"),
    POOL_CPU_SYSTEM_USAGE("sfly.{$env}.host.{$pool}.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.{$env}." +
            "host.{$pool}.*.aggregation-cpu-average.cpu-idle"),
    POOL_CPU_IOWAIT_USAGE("sfly.{$env}.host.{$pool}.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.{$env}." +
            "host.{$pool}.*.aggregation-cpu-average.cpu-idle"),
    POOL_RAM_USAGE("sfly.{$env}.host.{$pool}.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.{$env}.host.{$pool}.*." +
            "memory.memory-buffered"),
    POOL_SWAP_USAGE("sfly.{$env}.host.{$pool}.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.{$env}.host.{$pool}.*." +
            "swap.swap-used"),

    GC_MARK_SWEEP_HEAP_USAGE("sfly.{$env}.host.{$pool}.*.GarbageCollectorSentinel.ConcurrentMarkSweep." +
            "heapUsagePercentage"),
    GC_MARK_SWEEP_COLLECTION_TIME("sfly.{$env}.host.{$pool}.*.GarbageCollectorSentinel.ConcurrentMarkSweep." +
            "collectionTime"),

    GC_PAR_NEW_COLLECTION_TIME("sfly.{$env}.host.{$pool}.*.GarbageCollectorSentinel.ParNew.collectionTime"),
    GC_PAR_NEW_HEAP_USAGE("sfly.{$env}.host.{$pool}.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage");



    private final String target;

    private GraphiteTargetEnum(String target) {
        this.target = target;
    }

    public String getTarget(String env, String pool) {
        return target.replace("{$env}", env).replace("{$pool}", getPoolShortNameFromPoolLongName(pool));
    }

    private String getPoolShortNameFromPoolLongName(String bigPool) {
        String result = bigPool;
        if(bigPool.equalsIgnoreCase("appserver")) {
            result = "app";
        } else if(bigPool.equalsIgnoreCase("apiserver")) {
            result = "api";
        } else if(bigPool.equalsIgnoreCase("appserver")) {
            result = "app";
        } else if(bigPool.equalsIgnoreCase("gimserver")) {
            result = "gim";
        } else if(bigPool.equalsIgnoreCase("grfserver")) {
            result = "grf";
        } else if(bigPool.equalsIgnoreCase("imserver")) {
            result = "im";
        } else if(bigPool.equalsIgnoreCase("upload")) {
            result = "up";
        } else if(bigPool.equalsIgnoreCase("wsserver")) {
            result = "ws";
        }

        return  result;
    }
}
