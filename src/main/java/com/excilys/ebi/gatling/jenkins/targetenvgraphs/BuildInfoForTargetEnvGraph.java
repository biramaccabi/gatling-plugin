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
package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Calendar;

public class BuildInfoForTargetEnvGraph {
    private String envName;
    private String poolName;
    private Brand brand;

    private long buildDuration;
    private Calendar buildStartTime;

    static final int GRAPH_START_BUFFER_TIME_IN_MINUTES = -5;
    static final int GRAPH_END_BUFFER_TIME_IN_MINUTES = 5;

    public String getEnvironmentName() {
        return envName;
    }

    public void setEnvironmentName(String environmentName) {
        this.envName = environmentName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getBrandName() {
        return brand.name;
    }

    public void setBuildDuration(Long duration) {
        this.buildDuration = duration;
    }

    public void setBuildStartTime(Calendar startTime) {
        this.buildStartTime = startTime;
    }

    public Calendar getGraphStartTime() {
        Calendar graphStartTime = (Calendar)getBuildStartTime().clone();
        graphStartTime.add(Calendar.MINUTE, GRAPH_START_BUFFER_TIME_IN_MINUTES);
        return graphStartTime;
    }

    public Calendar getGraphEndTime() {
        Calendar endTime = (Calendar)this.getBuildStartTime().clone();
        endTime.setTimeInMillis(endTime.getTimeInMillis() + this.getBuildDuration());
        endTime.add(Calendar.MINUTE, BuildInfoForTargetEnvGraph.GRAPH_END_BUFFER_TIME_IN_MINUTES);

        return endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else if(obj instanceof BuildInfoForTargetEnvGraph) {
            BuildInfoForTargetEnvGraph other = (BuildInfoForTargetEnvGraph) obj;
            return new EqualsBuilder()
                    .append(buildDuration, other.buildDuration)
                    .append(buildStartTime,other.buildStartTime)
                    .append(envName, other.envName)
                    .append(poolName, other.poolName)
                    .append(brand, other.brand)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(envName)
                .append(poolName)
                .append(buildDuration)
                .append(buildStartTime)
                .append(brand)
                .toHashCode();
    }

    private long getBuildDuration() {
        return buildDuration;
    }

    private Calendar getBuildStartTime() {
        return buildStartTime;
    }
}
