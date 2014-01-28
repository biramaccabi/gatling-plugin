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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphiteGraphSettingsBuilder {

    private static final String FOXTROT_ENV = "foxtrot";
    private static final String STAGE_ENV = "stage";
    private static final String PROD_ENV = "prod";
    private static final String BETA_ENV = "beta";

    private static final String APISERVER_POOL = "apiserver";
    private static final String APPSERVER_POOL = "appserver";
    private static final String GIMSERVER_POOL = "gimserver";
    private static final String GRFSERVER_POOL = "grfserver";
    private static final String IMSERVER_POOL = "imserver";
    private static final String UPLOADSERVER_POOL = "upserver";
    private static final String WSSERVER_POOL = "wsserver";


    Map<String, Map<String, List<GraphiteGraphSettings>>> envPoolSettings;

    public GraphiteGraphSettingsBuilder() {
        envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
    }

    public List<GraphiteGraphSettings> getGraphiteGraphSettings(BuildInfoForTargetEnvGraph criteria) {
        configureSettingsForEnvPool(criteria);
        List<GraphiteGraphSettings> result = envPoolSettings.get(criteria.getEnvironmentName()).get(criteria.getPoolName());

        if(null != result) {
            return result;
        } else {
            return new ArrayList<GraphiteGraphSettings>();
        }
    }

    private void configureSettingsForEnvPool(BuildInfoForTargetEnvGraph criteria) {
        String env = criteria.getEnvironmentName();
        String pool = criteria.getPoolName();
        GraphiteGraphSettings madeSetting = new GraphiteGraphSettings();
        if(dataExistsForEnvPool(env, pool)) {
            for(GraphiteTargetEnum graphiteTarget: GraphiteTargetEnum.values()) {
                madeSetting.setTarget(graphiteTarget.getTarget(env, pool));
                madeSetting.setHost(graphiteTarget.host);
                madeSetting.setYMax(graphiteTarget.yMax);
                madeSetting.setYMin(graphiteTarget.yMin);
                madeSetting.setVerticalTitle(graphiteTarget.vTitle);
                madeSetting.setTitle(graphiteTarget.getTitle(env, pool));
                addSetting(env, pool, madeSetting);
            }
        } else {
            addSetting(env, pool, null);
        }
    }

    private boolean dataExistsForEnvPool(String env, String pool) {

        List<String> supportedPools = new ArrayList<String>();
        supportedPools.add(APISERVER_POOL);
        supportedPools.add(APPSERVER_POOL);
        supportedPools.add(GIMSERVER_POOL);
        supportedPools.add(GRFSERVER_POOL);
        supportedPools.add(IMSERVER_POOL);
        supportedPools.add(UPLOADSERVER_POOL);
        supportedPools.add(WSSERVER_POOL);

        List<String> supportedEnvs = new ArrayList<String>();
        supportedEnvs.add(FOXTROT_ENV);
        supportedEnvs.add(STAGE_ENV);
        supportedEnvs.add(PROD_ENV);
        supportedEnvs.add(BETA_ENV);

        return supportedEnvs.contains(env) && supportedPools.contains(pool);

    }

    private void addSetting(String env, String pool, GraphiteGraphSettings setting) {
        if(null == envPoolSettings) {
            envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
        }

        Map<String, List<GraphiteGraphSettings>> poolMapForEnv = envPoolSettings.get(env);

        if(null == poolMapForEnv || !poolMapForEnv.containsKey(pool)) {
            Map<String, List<GraphiteGraphSettings>> newPoolSettingsForEnv = new HashMap<String, List<GraphiteGraphSettings>>();
            newPoolSettingsForEnv.put(pool, new ArrayList<GraphiteGraphSettings>());
            envPoolSettings.put(env, newPoolSettingsForEnv);
        }
        if(null != setting) {
            envPoolSettings.get(env).get(pool).add(setting);
        }
    }


}
