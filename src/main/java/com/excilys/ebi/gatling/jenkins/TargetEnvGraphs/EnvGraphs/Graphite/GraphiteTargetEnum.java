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
package com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite;

public enum GraphiteTargetEnum {
    FOXTROT_APP_POOLCPU_USAGE("sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-{user%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.aggregation-cpu-average.cpu-idle"),
    FOXTROT_APP_POOLMEMORY_USAGE("sfly.foxtrot.host.app.*.memory.memory-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.memory.memory-buffered"),
    FOXTROT_APP_POOLSWAP_USAGE("sfly.foxtrot.host.app.*.swap.swap-{used%2C}.value%2Ccolor%28sfly.foxtrot.host.app.*.swap.swap-used"),

    FOXTROT_GC_MARK_SWEEP_HEAP_USAGE("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.heapUsagePercentage"),
    FOXTROT_GC_MARK_SWEEP_COLLECTION_TIME("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ConcurrentMarkSweep.collectionTime"),

    FOXTROT_GC_PAR_NEW_HEAP_USAGE("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.heapUsagePercentage"),
    FOXTROT_GC_PAR_NEW_COLLECTION_TIME("sfly.foxtrot.host.app.*.GarbageCollectorSentinel.ParNew.collectionTime")
    ;

    private final String target;

    private GraphiteTargetEnum(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
