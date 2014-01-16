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
package com.excilys.ebi.gatling.jenkins.TargetEnvGraphs;

import java.util.Calendar;

public class GraphCriteria {
    public String environmentName;
    public String poolName;
    public Calendar startTime;
    public Calendar endTime;
    public Long duration;

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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /*
        I'm not including the start/end time or duration
        I'm considering two criteria to be equal if they are the same pool+environment combination
     */
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if( o instanceof GraphCriteria) {
            GraphCriteria compareTo = (GraphCriteria) o;
            if(this.getEnvironmentName().equals(compareTo.getEnvironmentName())) {
                if(this.getPoolName().equals(compareTo.getPoolName())){
                    return true;
                }
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        String mashUp = this.getEnvironmentName() + this.getPoolName();
        return mashUp.hashCode();
    }

}
