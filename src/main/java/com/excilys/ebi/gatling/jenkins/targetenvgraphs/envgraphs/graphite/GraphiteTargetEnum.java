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


import com.excilys.ebi.gatling.jenkins.targetenvgraphs.ServerPool;

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
    GC_PAR_NEW_HEAP_USAGE("sfly.{$env}.host.{$pool}.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage"),

    MSP_CPU_USER_USAGE("sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-idle"),
    MSP_CPU_SYSTEM_USAGE("sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-idle"),
    MSP_CPU_WAIT_USAGE("sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.{$env}.host.oracle.*.aggregation-cpu-average.cpu-idle"),

    MSP_LOAD_AVG("sfly.{$env}.host.oracle.*.load.load.*term"),

    NXGEN_CPU_USER_USAGE("sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle"),
    NXGEN_CPU_SYSTEM_USAGE("sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle"),
    NXGEN_CPU_WAIT_USAGE("sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.{$env}.host.oracle-x86_64.*.aggregation-cpu-average.cpu-idle"),

    NXGEN_LOAD_AVG("sfly.{$env}.host.oracle-x86_64.*.load.load.*term"),

    MONGODB_CPU_USER_USAGE("sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-idle"),
    MONGODB_CPU_SYSTEM_USAGE("sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-{system%2C}.value%2Ccolor%28sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-idle"),
    MONGODB_CPU_WAIT_USAGE("sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-{wait%2C}.value%2Ccolor%28sfly.{$env}.host.mongodb.*.aggregation-cpu-average.cpu-idle"),

    MONGODB_LOAD_AVG("sfly.{$env}.host.mongodb.*.load.load.*term");

    private final String target;

    private GraphiteTargetEnum(String target) {
        this.target = target;
    }

    public String getTarget(String env, String pool) {
        return target.replace("{$env}", env).replace("{$pool}", getPoolShortNameFromPoolLongName(pool));
    }

    private String getPoolShortNameFromPoolLongName(String bigPool) {
        String result = bigPool;
        ServerPool serverPool = ServerPool.getEnumForPoolName(bigPool.toLowerCase());
        if(null != serverPool) {
            result = serverPool.shortName;
        }
        return  result;
    }
}
