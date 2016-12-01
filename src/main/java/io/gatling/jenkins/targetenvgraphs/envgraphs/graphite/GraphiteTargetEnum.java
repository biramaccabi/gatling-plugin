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
package io.gatling.jenkins.targetenvgraphs.envgraphs.graphite;


import io.gatling.jenkins.targetenvgraphs.ServerPool;

public enum GraphiteTargetEnum {
    SERVERS_CHART_ENV_POOL("var-env={$env}&var-pool={$pool}"),
    SERVERS_CHART_ENV("var-env={$env}");

    private final String target;

    private GraphiteTargetEnum(String target) {
        this.target = target;
    }

    public String getTarget(String brand, String env, String pool) {
        return target.replace("{$env}", env).replace("{$pool}", getPoolShortNameFromPoolLongName(pool)).replace("{$brand}",brand);
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
