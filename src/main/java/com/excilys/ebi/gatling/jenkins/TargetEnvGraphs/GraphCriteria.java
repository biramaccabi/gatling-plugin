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

import java.util.Calendar;

public class GraphCriteria {
    public String environmentName;
    public String poolName;

    public Long buildDuration;
    private Calendar buildStartTime;

    public static final int graphStartBufferTimeMinutes = -5;
    public static final int graphEndBufferTimeMinutes = 5;

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setBuildDuration(Long duration) {
        this.buildDuration = duration;
    }

    public void setBuildStartTime(Calendar startTime) {
        this.buildStartTime = startTime;
    }

    public Calendar getGraphStartTime() {
        Calendar graphStartTime = (Calendar)getBuildStartTime().clone();
        graphStartTime.add(Calendar.MINUTE, graphStartBufferTimeMinutes);
        return graphStartTime;
    }

    public Calendar getGraphEndTime() {
        Calendar endTime = (Calendar)this.getBuildStartTime().clone();
        endTime.setTimeInMillis(endTime.getTimeInMillis() + this.getBuildDuration());
        endTime.add(Calendar.MINUTE, GraphCriteria.graphEndBufferTimeMinutes);

        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GraphCriteria criteria = (GraphCriteria) o;

        if (buildDuration != null ? !buildDuration.equals(criteria.buildDuration) : criteria.buildDuration != null) {
            return false;
        }
        if (buildStartTime != null ? !buildStartTime.equals(criteria.buildStartTime) : criteria.buildStartTime != null) {
            return false;
        }
        if (environmentName != null ? !environmentName.equals(criteria.environmentName) : criteria.environmentName != null) {
            return false;
        }
        if (poolName != null ? !poolName.equals(criteria.poolName) : criteria.poolName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = environmentName != null ? environmentName.hashCode() : 0;
        result = 31 * result + (poolName != null ? poolName.hashCode() : 0);
        result = 31 * result + (buildDuration != null ? buildDuration.hashCode() : 0);
        result = 31 * result + (buildStartTime != null ? buildStartTime.hashCode() : 0);
        return result;
    }

    private long getBuildDuration() {
        return buildDuration;
    }

    private Calendar getBuildStartTime() {
        return buildStartTime;
    }
}
