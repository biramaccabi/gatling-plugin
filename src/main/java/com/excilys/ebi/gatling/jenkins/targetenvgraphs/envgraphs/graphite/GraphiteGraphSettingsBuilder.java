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
    private static final String APPSERVER_POOL = "appserver";
    private static final String WSSERVER_POOL = "wsserver";
    private static final String GIMSERVER_POOL = "gimserver";
    private static final String IMSERVER_POOL = "imserver";



    Map<String, Map<String, List<GraphiteGraphSettings>>> envPoolSettings;

    public GraphiteGraphSettingsBuilder() {
        envPoolSettings = new HashMap<String, Map<String, List<GraphiteGraphSettings>>>();
        addEnvPoolSetting(FOXTROT_ENV, APPSERVER_POOL);

    }

    public List<GraphiteGraphSettings> getGraphiteGraphSettings(BuildInfoForTargetEnvGraph criteria) {
        List<GraphiteGraphSettings> result = envPoolSettings.get(criteria.getEnvironmentName()).get(criteria.getPoolName());

        if(null != result) {
            return result;
        } else {
            return new ArrayList<GraphiteGraphSettings>();
        }

    }

    private void addEnvPoolSetting(String env, String pool) {
        GraphiteGraphSettings madeSetting = new GraphiteGraphSettings();
        for(GraphiteTargetEnum graphiteTarget: GraphiteTargetEnum.values()) {
            if(dataExistsForEnvPool(env, pool, graphiteTarget)) {
                madeSetting.setTarget(graphiteTarget.getTarget(env, pool));
                madeSetting.setHost(graphiteTarget.host);
                madeSetting.setYMax(graphiteTarget.yMax);
                madeSetting.setYMin(graphiteTarget.yMin);
                madeSetting.setVerticalTitle(graphiteTarget.vTitle);
                madeSetting.setTitle(graphiteTarget.getTitle(env, pool));
                addSetting(env, pool, madeSetting);
            }
        }
    }

    private boolean dataExistsForEnvPool(String env, String pool, GraphiteTargetEnum targetEnum) {
        boolean result = false;

        List<String> foxtrotPools = new ArrayList<String>();
        foxtrotPools.add(APPSERVER_POOL);
        foxtrotPools.add(WSSERVER_POOL);
        foxtrotPools.add(GIMSERVER_POOL);
        foxtrotPools.add(IMSERVER_POOL);

        if(env.equals(FOXTROT_ENV)) {
            result = foxtrotPools.contains(pool);
        }

        return result;
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
        }
        envPoolSettings.get(env).get(pool).add(setting);
    }


}
