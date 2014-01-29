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

import com.excilys.ebi.gatling.jenkins.targetenvgraphs.BuildInfoForTargetEnvGraph;
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.Environment;
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.ServerPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphiteGraphSettingsBuilder {

    private final String TRE_HOST = "http://tre-stats.internal.shutterfly.com";
    private final String IOPS_HOST = "http://graphite.internal.shutterfly.com:443/";

    Map<String, Map<String, List<GraphiteGraphSettings>>> envPoolSettings;

    public GraphiteGraphSettingsBuilder() {
        envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
        for(ServerPool serverEnum: ServerPool.values()) {
            addEnvPoolSetting(Environment.FOXTROT.name, serverEnum.longName);
        }
    }

    public List<GraphiteGraphSettings> getGraphiteGraphSettings(BuildInfoForTargetEnvGraph criteria) {
        List<GraphiteGraphSettings> result = getGraphiteGraphSettings(criteria.getEnvironmentName(), criteria.getPoolName());
        if(null != result) {
            return result;
        } else {
            return new ArrayList<GraphiteGraphSettings>();
        }
    }

    private List<GraphiteGraphSettings> getGraphiteGraphSettings(String env, String pool) {
        Map<String, List<GraphiteGraphSettings>> poolsForEnvironments = envPoolSettings.get(env);
        if(null != poolsForEnvironments) {
            List<GraphiteGraphSettings> result = poolsForEnvironments.get(pool);
            if(null != result) {
                return result;
            }
        }
        return null;
    }

    private void addEnvPoolSetting(String environment, String pool) {
        GraphiteGraphSettings madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(environment, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("percent_heap_used");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_MARK_SWEEP_COLLECTION_TIME.getTarget(environment, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("collection_time_in_ms");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_PAR_NEW_HEAP_USAGE.getTarget(environment, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("percent_heap_used");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_PAR_NEW_COLLECTION_TIME.getTarget(environment, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("collection_time_in_ms");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_USER_USAGE.getTarget(environment, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_User_Used");
        madeSetting.setTitle("APP_POOL_CPU_User_Usage");
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_SYSTEM_USAGE.getTarget(environment, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_System_Used");
        madeSetting.setTitle("APP_POOL_CPU_System_Usage");
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_IOWAIT_USAGE.getTarget(environment, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        madeSetting.setTitle("APP_POOL_CPU_IO_Wait_Usage");
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_RAM_USAGE.getTarget(environment, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("Amount_RAM_Used");
        madeSetting.setTitle("APP_POOL_RAM_Usage");
        addSetting(environment, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_SWAP_USAGE.getTarget(environment, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("Amount_SWAP_Used");
        madeSetting.setTitle("APP_POOL_SWAP_Usage");
        addSetting(environment, pool, madeSetting);

    }

    private void addSetting(String env, String pool, GraphiteGraphSettings setting) {
        if(null == envPoolSettings) {
            envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
        }

        Map<String, List<GraphiteGraphSettings>> poolMapForEnv = envPoolSettings.get(env);

        if(null == poolMapForEnv) {
            Map<String, List<GraphiteGraphSettings>> newPoolSettingsForEnv = new HashMap<String, List<GraphiteGraphSettings>>();
            newPoolSettingsForEnv.put(pool, new ArrayList<GraphiteGraphSettings>());
            envPoolSettings.put(env, newPoolSettingsForEnv);
            poolMapForEnv = envPoolSettings.get(env);
        }

        if(null == envPoolSettings.get(env) || !poolMapForEnv.containsKey(pool)){
            poolMapForEnv.put(pool, new ArrayList<GraphiteGraphSettings>());
        }
        envPoolSettings.get(env).get(pool).add(setting);
    }


}
