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

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.GraphCriteria;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphiteMetricBuilder {

    private static final Logger logger = Logger.getLogger(GraphiteMetricBuilder.class.getName());

    private final String foxtrot = "foxtrot";
    private final String stage = "stage";
    private final String prod = "prod";
    private final String production = "production";
    private final String beta = "beta";

    private final String appPool = "appserver";
    private final String wsPool = "wsserver";
    private final String apiPool = "apiserver";

    private final String treHost = "http://tre-stats.internal.shutterfly.com";
    private final String iopsHost = "http://graphite.internal.shutterfly.com:443/";

    private final ArrayList<String> poolUsageTypes;
    private final String cpu = "CPU";
    private final String ram = "ram";
    private final String swap = "swap";

    private final ArrayList<String> gcUsages;
    private final String gcMarkSweepHeap = "markSweepHeap";
    private final String gcMarkSweepCollection = "markSweepCollectionTime";
    private final String gcParNewHeap = "parNewHeap";
    private final String gcParNewCollection = "parNewCollectionTime";

    public GraphiteMetricBuilder() {
        poolUsageTypes = new ArrayList<String>();
        poolUsageTypes.add(cpu);
        poolUsageTypes.add(ram);
        poolUsageTypes.add(swap);

        gcUsages = new ArrayList<String>();
        gcUsages.add(gcMarkSweepHeap);
        gcUsages.add(gcMarkSweepCollection);
        gcUsages.add(gcParNewHeap);
        gcUsages.add(gcParNewCollection);
    }

    public ArrayList<GraphiteMetrics> getMetrics(GraphCriteria criteria) {
        ArrayList<GraphiteMetrics> metrics = new ArrayList<GraphiteMetrics>();
        metrics.addAll(getGarbageCollectionMetrics(criteria));
        metrics.addAll(getAppMetrics(criteria));
        metrics.addAll(getApiMetrics(criteria));
        metrics.addAll(getWsMetrics(criteria));
        return metrics;
    }

    public ArrayList<GraphiteMetrics> getGarbageCollectionMetrics(GraphCriteria criteria) {
        ArrayList<GraphiteMetrics> gcGraphs = new ArrayList<GraphiteMetrics>();

        for(String gcUsageType: gcUsages) {
            GraphiteMetrics gcUsage = getGCMetricsForCriteriaType(criteria, gcUsageType);
            if(null != gcUsage) {
                gcGraphs.add(gcUsage);
            }
        }
        return gcGraphs;
    }

    public ArrayList<GraphiteMetrics> getAppMetrics(GraphCriteria criteria) {
        ArrayList<GraphiteMetrics> appGraphs = new ArrayList<GraphiteMetrics>();

        if(criteria.getPoolName().equalsIgnoreCase(appPool)) {
            for(String appUsageType: poolUsageTypes) {
                GraphiteMetrics appUsage = getAppUsage(criteria, appUsageType);
                if(null != appUsage) {
                    appGraphs.add(appUsage);
                }
            }
        }

        return appGraphs;
    }

    public ArrayList<GraphiteMetrics> getApiMetrics(GraphCriteria criteria) {
        ArrayList<GraphiteMetrics> apiGraphs = new ArrayList<GraphiteMetrics>();

        if(criteria.getPoolName().equalsIgnoreCase(apiPool)) {
            logger.log(Level.INFO, "API Pool currently not supported");
        }
        return apiGraphs;
    }

    public ArrayList<GraphiteMetrics> getWsMetrics(GraphCriteria criteria) {
        ArrayList<GraphiteMetrics> wsGraphs = new ArrayList<GraphiteMetrics>();

        if(criteria.getPoolName().equalsIgnoreCase(wsPool)) {
            logger.log(Level.INFO, "WS Pool currently not supported");
        }
        return wsGraphs;
    }

    public GraphiteMetrics getGCMetricsForCriteriaType(GraphCriteria criteria, String usageType) {
        GraphiteMetrics result = new GraphiteMetrics();
        String environment = criteria.getEnvironmentName();
        String pool = criteria.getPoolName();

        if( environment.equalsIgnoreCase(foxtrot)) {
            if(pool.equals(appPool)) {
                if(usageType.equals(gcMarkSweepHeap)) {
                    result.setTarget(GraphiteTargetEnum.FOXTROT_GC_MARK_SWEEP_HEAP_USAGE.getTarget());
                } else if(usageType.equals(gcMarkSweepCollection)) {
                    result.setTarget(GraphiteTargetEnum.FOXTROT_GC_MARK_SWEEP_COLLECTION_TIME.getTarget());
                } else if(usageType.equals(gcParNewHeap)) {
                    result.setTarget(GraphiteTargetEnum.FOXTROT_GC_PAR_NEW_HEAP_USAGE.getTarget());
                } else if(usageType.equals(gcParNewCollection)) {
                    result.setTarget(GraphiteTargetEnum.FOXTROT_GC_PAR_NEW_COLLECTION_TIME.getTarget());
                } else {
                    logger.log(Level.SEVERE, "Requested a garbage collection measurement that does not exist: " + usageType);
                    return null;
                }
                result.setHost(treHost);
            } else {
                logger.log(Level.INFO, "GC graphing is not available for " + pool);
                return null;
            }
        } else if(environment.equalsIgnoreCase(stage)) {
            logger.log(Level.INFO, "Stage is not currently configured for Stage GC Usage");
            return null;
        } else if(environment.equalsIgnoreCase(prod) || environment.equalsIgnoreCase(production)){
            logger.log(Level.INFO, "Production is not currently configured for Prod GC Usage");
            return null;
        } else if(environment.equalsIgnoreCase(beta) ){
            logger.log(Level.INFO, "Beta is not currently configured for Beta GC Usage");
            return null;
        } else {
            logger.log(Level.INFO, "Environment not detected.  No graphs being returned.");
            return null;
        }

        if(usageType.equals(gcMarkSweepHeap) || usageType.equals(gcParNewHeap)) {
            result.setYMax("100");
            result.setYMin("0");
        } else {
            result.setYMax("");
            result.setYMin("");
        }

        if(usageType.contains("CollectionTime")) {
            result.setVerticalTitle("collection_time_in_ms");
        } else {
            result.setVerticalTitle("percent_heap_used");
        }
        result.setTitle(result.getTarget());
        return result;
    }

    public GraphiteMetrics getAppUsage(GraphCriteria criteria, String usageType) {
        GraphiteMetrics result = new GraphiteMetrics();
        String environment = criteria.getEnvironmentName();
        if( environment.equalsIgnoreCase(foxtrot)) {
            if(usageType.equals(cpu)) {
                result.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLCPU_USAGE.getTarget());
            } else if(usageType.equals(ram)) {
                result.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLMEMORY_USAGE.getTarget());
            } else if(usageType.equals(swap)) {
                result.setTarget(GraphiteTargetEnum.FOXTROT_APP_POOLSWAP_USAGE.getTarget());
            } else {
                logger.log(Level.SEVERE, "Requested a pool measurement that does not exist: " + usageType);
                return null;
            }
            result.setHost(iopsHost);

        } else if(environment.equalsIgnoreCase(stage)) {
            logger.log(Level.INFO, "Stage is not currently configured for App Pool CPU Usage");
            return null;
        } else if(environment.equalsIgnoreCase(prod) || environment.equalsIgnoreCase(production)){
            logger.log(Level.INFO, "Production is not currently configured for App Pool CPU Usage");
            return null;
        } else if(environment.equalsIgnoreCase(beta) ){
            logger.log(Level.INFO, "Beta is not currently configured for App Pool CPU Usage");
            return null;
        } else {
            logger.log(Level.INFO, "Environment not detected.  No graphs being returned.");
            return null;
        }
        result.setYMax("");
        result.setYMin("");
        result.setTitle("APP_" + usageType + "_Usage");
        result.setVerticalTitle(usageType + "_usage");
        return result;
    }

}
