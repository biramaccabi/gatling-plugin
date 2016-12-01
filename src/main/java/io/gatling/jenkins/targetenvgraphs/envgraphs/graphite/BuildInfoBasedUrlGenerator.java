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


import io.gatling.jenkins.targetenvgraphs.Brand;
import io.gatling.jenkins.targetenvgraphs.BuildInfoForTargetEnvGraph;
import io.gatling.jenkins.targetenvgraphs.Environment;
import io.gatling.jenkins.targetenvgraphs.ServerPool;

import java.util.TimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class BuildInfoBasedUrlGenerator {

    public BuildInfoBasedUrlGenerator() {}
    private final String SFLY_GRAFANA_SERVERS = "https://grafana.internal.shutterfly.com/dashboard/db/sfly-servers";
    private final String SFLY_GRAFANA_SQUID = "https://grafana.internal.shutterfly.com/dashboard/db/sfly-squid-statistics";
    private final String SFLY_GRAFANA_VERTEX = "https://grafana.internal.shutterfly.com/dashboard/db/sfly-vertex-tax-service-dashboard";

    private final String GRAFANA_DASHBOARD_FOR = "Grafana Dashboard for ";
    private final String VERTEX_DASHBOARD = "Vertex Tax Service Dashboard";
    private final String SQUID_DASHBOARD = "Squid Statistics Dashboard";

    public ArrayList<GrafanaUrl> getUrlsForCriteria(BuildInfoForTargetEnvGraph buildInfo) {

        ArrayList<GrafanaUrl> urlList = new ArrayList<GrafanaUrl>();
        ServerPool pool = ServerPool.getEnumForPoolName(buildInfo.getPoolName());
        Brand brand = Brand.getBrandFromName(buildInfo.getBrandName());
        String envName = buildInfo.getEnvironmentName();

        if (brandHasEnv(brand, envName) && (pool != null)) {
            String grafanaLinkName = GRAFANA_DASHBOARD_FOR + envName + " " + pool.shortName + " pool";
            GrafanaUrl urlAndLinkName= new GrafanaUrl(getGrafanaEnvPoolURLForCriteria(buildInfo), grafanaLinkName);
            urlList.add(urlAndLinkName);
            if(brand.equals(Brand.SHUTTERFLY) && pool.equals(ServerPool.APISERVER))
            {
                //add the ws server for api server information
                BuildInfoForTargetEnvGraph wsBuildInfo = buildInfo;
                wsBuildInfo.setPoolName("wsserver");
                grafanaLinkName = GRAFANA_DASHBOARD_FOR + envName + " " + "ws" + " pool";
                GrafanaUrl urlAndLinkNameAPI= new GrafanaUrl(getGrafanaEnvPoolURLForCriteria(wsBuildInfo), grafanaLinkName);
                urlList.add(urlAndLinkNameAPI);
            }

            if(brand.equals(Brand.SHUTTERFLY) && pool.equals(ServerPool.WSSERVER))
            {
                //add the squid information for ws pool
                GrafanaUrl urlAndLinkNameWS = new GrafanaUrl(
                        getGrafanaEnvOnlyURLForCriteria(buildInfo, SFLY_GRAFANA_SQUID), SQUID_DASHBOARD);
                urlList.add(urlAndLinkNameWS);
            }

            if (brand.equals(Brand.SHUTTERFLY) && pool.equals(ServerPool.VERTEXSERVER))
            {
                //add the squid information for ws pool
                GrafanaUrl urlAndLinkNameVertex = new GrafanaUrl(
                        getGrafanaEnvOnlyURLForCriteria(buildInfo, SFLY_GRAFANA_VERTEX), VERTEX_DASHBOARD);
                urlList.add(urlAndLinkNameVertex);
            }
        }

        return urlList;
    }



    //This only includes the supported brand/env combinations for load testing
    private boolean brandHasEnv(Brand brand, String env) {
        if (brand.equals(Brand.SHUTTERFLY)) {
            return env.equals(Environment.KAPPA.name)
                    || env.equals(Environment.STAGE.name)
                    || env.equals(Environment.PROD.name);
        } else {
            //Tinyprints will be supported again when they get a load test env
            //return brand.equals(Brand.TINYPRINTS) && env.equals(Environment.TPLNP);
            return false;
        }
    }

    private String getStartAndEndTimes(BuildInfoForTargetEnvGraph criteria)
    {
        StringBuilder result = new StringBuilder();
        //20160915T050001
        SimpleDateFormat graphiteFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        //graphiteFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startTimeString = graphiteFormat.format(criteria.getGraphStartTime().getTime());
        String endTimeString = graphiteFormat.format(criteria.getGraphEndTime().getTime());
        result.append("&from=").append(startTimeString);
        result.append("&to=").append(endTimeString);
        return result.toString();
    }


    private String getGrafanaEnvPoolURLForCriteria(BuildInfoForTargetEnvGraph criteria) {
        StringBuilder result = new StringBuilder();

        result.append(SFLY_GRAFANA_SERVERS);
        result.append("?");
        result.append(GraphiteTargetEnum.SERVERS_CHART_ENV_POOL.getTarget(criteria.getBrandName(), criteria.getEnvironmentName(), criteria.getPoolName()));
        result.append(getStartAndEndTimes(criteria));

        return result.toString();
    }

    private String getGrafanaEnvOnlyURLForCriteria(BuildInfoForTargetEnvGraph criteria, String baseURL) {
        StringBuilder result = new StringBuilder();

        result.append(baseURL);
        result.append("?");
        result.append(GraphiteTargetEnum.SERVERS_CHART_ENV.getTarget(criteria.getBrandName(), criteria.getEnvironmentName(), criteria.getPoolName()));
        result.append(getStartAndEndTimes(criteria));

        return result.toString();
    }
}
