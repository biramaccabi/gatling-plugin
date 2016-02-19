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
import com.excilys.ebi.gatling.jenkins.targetenvgraphs.Brand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphiteGraphSettingsBuilder {

    private final String TRE_HOST = "http://tre-stats.internal.shutterfly.com";
    private final String IOPS_HOST = "http://graphite-web.internal.shutterfly.com:443/";

    Map<String, Map<String, Map<String, List<GraphiteGraphSettings>>>> brandEnvPoolSettings;

    public GraphiteGraphSettingsBuilder() {
        brandEnvPoolSettings = new HashMap<String, Map<String, Map<String, List<GraphiteGraphSettings>>>>();
        for (Brand brand : Brand.values()) {
            for (ServerPool serverEnum : ServerPool.values()) {
                for (Environment env : Environment.values()) {
                    if(brandHasEnv(brand, env) ) {
                        addEnvPoolSetting(brand, env, serverEnum);
                    }
                }
            }
        }
    }

    private boolean brandHasEnv(Brand brand, Environment env) {
        if (brand.equals(Brand.SHUTTERFLY)) {
            return env.equals(Environment.KAPPA)
                    || env.equals(Environment.BETA)
                    || env.equals(Environment.STAGE)
                    || env.equals(Environment.PROD);
        } else {
            return brand.equals(Brand.TINYPRINTS) && env.equals(Environment.TPLNP);
        }
    }

    public List<GraphiteGraphSettings> getGraphiteGraphSettings(BuildInfoForTargetEnvGraph criteria) {
        List<GraphiteGraphSettings> result = getGraphiteGraphSettings(criteria.getBrandName(), criteria.getEnvironmentName(), criteria.getPoolName());
        if(null != result) {
            return result;
        } else {
            return new ArrayList<GraphiteGraphSettings>();
        }
    }

    private List<GraphiteGraphSettings> getGraphiteGraphSettings(String brand, String env, String pool) {
        Map<String, Map<String, List<GraphiteGraphSettings>>> environmentsForBrand = brandEnvPoolSettings.get(brand);
        if (null != environmentsForBrand) {
            Map<String, List<GraphiteGraphSettings>> poolsForEnvironments = environmentsForBrand.get(env);
            if (null != poolsForEnvironments) {
                List<GraphiteGraphSettings> result = poolsForEnvironments.get(pool);
                result = getSupplementalGraphsIfNeeded(pool, poolsForEnvironments, result);
                return result;
            }
        }
        return null;
    }

    private List<GraphiteGraphSettings> getSupplementalGraphsIfNeeded(String pool, Map<String, List<GraphiteGraphSettings>> poolsForEnvironments, List<GraphiteGraphSettings> result) {
        if(shouldAlsoIncludeWSPool(pool)) {
            List<GraphiteGraphSettings> supplementalResults = poolsForEnvironments.get(ServerPool.WSSERVER.longName);
            if(null != result) {
                result = mergeAndSortGraphSettings(result, supplementalResults);
            } else {
                result = supplementalResults;
            }
        }
        return result;
    }

    private List<GraphiteGraphSettings> mergeAndSortGraphSettings(List<GraphiteGraphSettings> a, List<GraphiteGraphSettings> b ) {
        if(a.size() == b.size()) {
            ArrayList<GraphiteGraphSettings> temp = new ArrayList<GraphiteGraphSettings>();
            for(int i = 0; i < a.size(); i++) {
                temp.add(a.get(i));
                if(!temp.contains(b.get(i))){
                    temp.add(b.get(i));
                }
            }
            return temp;
        } else {
            a.addAll(b);
            return a;
        }
    }

    private boolean shouldAlsoIncludeWSPool(String pool) {
        return pool.equalsIgnoreCase(ServerPool.APISERVER.longName);
    }

    private void addEnvPoolSetting(Brand brand, Environment environment, ServerPool serverPool) {
        String brandName = brand.name;
        String env = environment.name;
        String pool = serverPool.longName;
        String shortPoolName = serverPool.shortName;

        GraphiteGraphSettings madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_MARK_SWEEP_HEAP_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("percent_heap_used");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_MARK_SWEEP_COLLECTION_TIME.getTarget(brandName,env, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("collection_time_in_ms");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_PAR_NEW_HEAP_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("percent_heap_used");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.GC_PAR_NEW_COLLECTION_TIME.getTarget(brandName,env, pool));
        madeSetting.setHost(TRE_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("collection_time_in_ms");
        madeSetting.setTitle(madeSetting.getTarget());
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_USER_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_User_Used");
        madeSetting.setTitle(shortPoolName.toUpperCase() + "_POOL_CPU_User_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_SYSTEM_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_System_Used");
        madeSetting.setTitle(shortPoolName.toUpperCase() + "_POOL_CPU_System_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_CPU_IOWAIT_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        madeSetting.setTitle(shortPoolName.toUpperCase() + "_POOL_CPU_IO_Wait_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_RAM_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("Amount_RAM_Used");
        madeSetting.setTitle(shortPoolName.toUpperCase() + "_POOL_RAM_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.POOL_SWAP_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("");
        madeSetting.setYMin("");
        madeSetting.setVerticalTitle("Amount_SWAP_Used");
        madeSetting.setTitle(shortPoolName.toUpperCase() + "_POOL_SWAP_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MSP_CPU_USER_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_User_Used");
        madeSetting.setTitle("MSP_DATABASE_CPU_User_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MSP_CPU_SYSTEM_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_System_Used");
        madeSetting.setTitle("MSP_DATABASE_CPU_System_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MSP_CPU_WAIT_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        madeSetting.setTitle("MSP_DATABASE_CPU_IO_Wait_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MSP_LOAD_AVG.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("Load_Average");
        madeSetting.setTitle("MSP_DATABASE_Load_Average");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MONGODB_CPU_USER_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_User_Used");
        madeSetting.setTitle("MongoDB_CPU_User_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MONGODB_CPU_SYSTEM_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_System_Used");
        madeSetting.setTitle("MongoDB_CPU_System_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MONGODB_CPU_WAIT_USAGE.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("CPU_Percent_IO_Wait_Used");
        madeSetting.setTitle("MongoDB_CPU_IO_Wait_Usage");
        addSetting(brandName, env, pool, madeSetting);

        madeSetting = new GraphiteGraphSettings();
        madeSetting.setTarget(GraphiteTargetEnum.MONGODB_LOAD_AVG.getTarget(brandName,env, pool));
        madeSetting.setHost(IOPS_HOST);
        madeSetting.setYMax("100");
        madeSetting.setYMin("0");
        madeSetting.setVerticalTitle("Load_Average");
        madeSetting.setTitle("MongoDB_Load_Average");
        addSetting(brandName, env, pool, madeSetting);
    }

    private void addSetting(String brand, String env, String pool, GraphiteGraphSettings setting) {
        if(null == brandEnvPoolSettings) {
            brandEnvPoolSettings = new HashMap<String, Map<String, Map<String, List<GraphiteGraphSettings>>>>();
        }

        Map<String, Map<String, List<GraphiteGraphSettings>>> envPoolSettings = brandEnvPoolSettings.get(brand);
        if(null == envPoolSettings) {
            envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
            brandEnvPoolSettings.put(brand, envPoolSettings);
        }

        Map<String, List<GraphiteGraphSettings>> poolMapForEnv = brandEnvPoolSettings.get(brand).get(env);

        if(null == poolMapForEnv) {
            Map<String, List<GraphiteGraphSettings>> newPoolSettingsForEnv = new HashMap<String, List<GraphiteGraphSettings>>();
            newPoolSettingsForEnv.put(pool, new ArrayList<GraphiteGraphSettings>());
            brandEnvPoolSettings.get(brand).put(env, newPoolSettingsForEnv);
            poolMapForEnv = brandEnvPoolSettings.get(brand).get(env);
        }

        if(!poolMapForEnv.containsKey(pool)){
            poolMapForEnv.put(pool, new ArrayList<GraphiteGraphSettings>());
        }
        brandEnvPoolSettings.get(brand).get(env).get(pool).add(setting);
    }
}
