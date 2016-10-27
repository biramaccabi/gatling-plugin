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
package io.gatling.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class AssertionData {

    @JsonIgnore
    public String projectName;

    @JsonIgnore
    public String simulationName;

    @JsonIgnore
    public String scenarioName;

    @JsonProperty("path")
    public String requestName;

    public String message;

    @JsonProperty("target")
    public String assertionType;

    @JsonIgnore
    public String expectedValue;

    @JsonIgnore
    public String actualValue;

    @JsonProperty("result")
    public boolean status;

    public ArrayList<Integer> conditionValues;

    public ArrayList<Integer> values;

}
