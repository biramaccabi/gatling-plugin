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

import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.EnvGraphGenerator;
import com.excilys.ebi.gatling.jenkins.TargetEnvGraphs.EnvGraphs.Graphite.GraphiteUrlGenerator;
import hudson.model.AbstractBuild;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TargetGraphGenerator {

    private ArrayList<EnvGraphGenerator> graphGenerators;
    private int graphStartBufferTimeMinutes;
    private int graphEndBufferTimeMinutes;

    public TargetGraphGenerator() {
        graphGenerators = new ArrayList<EnvGraphGenerator>();
        graphGenerators.add(new GraphiteUrlGenerator());
        graphEndBufferTimeMinutes = 5;
        graphStartBufferTimeMinutes = -5;
    }

    public ArrayList<String> getGraphUrls(AbstractBuild<?, ?> build){
        ArrayList<String> graphUrls = new ArrayList<String>();

        GraphCriteria criteria = getCriteriaFromBuild(build);
        for(EnvGraphGenerator graphGenerator: graphGenerators) {
            graphUrls.addAll(graphGenerator.getUrlsForCriteria(criteria));
        }

        return graphUrls;
    }

    public GraphCriteria getCriteriaFromBuild(AbstractBuild build){
        GraphCriteria result = new GraphCriteria();

        result.setEnvironmentName(getEnvFromBuild(build));
        result.setPoolName(getPoolFromBuild(build));
        result.setStartTime(getStartTime(build));
        result.setEndTime(getEndTime(build));
        result.setDuration(build.getDuration());

        return result;
    }

    public Calendar getStartTime(AbstractBuild build) {
        Calendar startTime = (Calendar)build.getTimestamp().clone();

        startTime.add(Calendar.MINUTE, getGraphStartBufferTimeMinutes());

        return startTime;
    }

    public Calendar getEndTime(AbstractBuild build) {
        Calendar endTime = (Calendar)build.getTimestamp().clone();

        endTime.setTimeInMillis(endTime.getTimeInMillis() + build.getDuration());
        endTime.add(Calendar.MINUTE, getGraphEndBufferTimeMinutes());

        return endTime;
    }

    public String getEnvFromBuild(AbstractBuild build) {
        String projectName = build.getProject().getName();
        Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-.*?$");
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(1).toLowerCase();
        }
        return "";
    }

    public String getPoolFromBuild(AbstractBuild build) {
        String projectName = build.getProject().getName();
        Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-+([^_]+).*?$");
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(2).toLowerCase();
        }
        return "";
    }

    public int getGraphStartBufferTimeMinutes() {
        return graphStartBufferTimeMinutes;
    }

    public int getGraphEndBufferTimeMinutes() {
        return graphEndBufferTimeMinutes;
    }
}