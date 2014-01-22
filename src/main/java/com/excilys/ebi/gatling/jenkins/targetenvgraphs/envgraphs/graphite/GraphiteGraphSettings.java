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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;

public class GraphiteGraphSettings {

    private String host;
    private String target;
    private String title;
    private String verticalTitle;
    private String yMin;
    private String yMax;

    public HashMap<String, String> parameters = new HashMap<String, String>();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVerticalTitle() {
        return verticalTitle;
    }

    public void setVerticalTitle(String verticalTitle) {
        this.verticalTitle = verticalTitle;
    }

    public String getYMin() {
        return yMin;
    }

    public void setYMin(String yMin) {
        this.yMin = yMin;
    }

    public String getYMax() {
        return yMax;
    }

    public void setYMax(String yMax) {
        this.yMax = yMax;
    }


    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void addParameter(String name, String value) {
        if(isValidParamName(value)) {
            this.getParameters().put(name, value);
        }
    }

    public String getParameter(String paramName) {
        if(isValidParamName(paramName)) {
            return this.getParameters().get(paramName);
        } else {
            return null;
        }
    }

    public String getParameter(String name, String defaultValue) {
        String result = this.getParameters().get(name);
        if(null == result) {
            return defaultValue;
        } else {
            return result;
        }
    }

    private boolean isValidParamName(String s) {
        return (null != s && s.trim().length() > 0);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GraphiteGraphSettings) {
            GraphiteGraphSettings other = (GraphiteGraphSettings) obj;
            return new EqualsBuilder()
                    .append(host, other.host)
                    .append(parameters, other.parameters)
                    .append(target, other.target)
                    .append(title, other.title)
                    .append(verticalTitle, other.verticalTitle)
                    .append(yMax, other.yMax)
                    .append(yMin, other.yMin)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(host)
                .append(target)
                .append(title)
                .append(verticalTitle)
                .append(yMin)
                .append(yMax)
                .append(parameters)
                .toHashCode();
    }
}
