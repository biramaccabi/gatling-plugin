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

import com.excilys.ebi.gatling.jenkins.AssertionData;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrendGraphBuilder {

    protected static final String ROOT_GRAPHITE_URL =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&";
    protected static final String KO_TARGET = "target=alias(color(secondYAxis(" +
            "load.summary.${env}.${simName}.${reqName}.ko.percent" +
            ")%2C%22red%22)%2C%22percent%20KOs%22)";
    protected static final String PERFORMANCE_STAT_TARGET =
            "target=alias(load.summary.${env}.${simName}.${reqName}" +
            ".all.${assertName}%2C%22${assertDescr}%22)";
    protected static final String PERFORMANCE_ASSERT_THRESHOLD_TARGET =
            "target=alias(load.summary.${env}.${simName}.${reqName}." +
            "all.expected.${assertName}%2C%22" +
            "performance+assert+threshold%22" +
            ")";
    protected static final String RELEASE_BRANCH_TARGET =
            "target=alias(color(lineWidth(drawAsInfinite(integral(" +
            "sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C%22Release%20Branch%20Created%22" +
            ")";
    protected static final String RENDER_OPTIONS =
            "width=586&height=308&lineMode=connected&from=${fromDateTime}&" +
            "title=${reqName}+-+${assertDescr}" +
            "&vtitle=${performanceMetricLabel}&vtitleRight=Percentage_KOs" +
            "&bgcolor=FFFFFF&fgcolor=000000&yMaxRight=100&yMinRight=0&hideLegend=false&" +
            "uniqueLegend=true";
    protected static final String PERFORMANCE_METRIC_LABEL_THROUGHPUT = "Requests_per_second";
    public static final String PERFORMANCE_METRIC_LABEL_RESPONSE_TIME = "Response_Time_in_ms";
    private SimpleDateFormat graphiteFromFormat = new SimpleDateFormat("HH:mm_yyyyMMdd");
    private final Pattern envPattern = Pattern.compile("^[^-]+-([^-]+)-.*?$");
    private static final Logger logger = Logger.getLogger(TrendGraphBuilder.class.getName());

    public enum GRAPHITE_ASSERT_TYPE {
        percentiles95,
        mean,
        min,
        max,
        stddev,
        throughput,
        ko;

        public static GRAPHITE_ASSERT_TYPE fromGatlingAssertType(String assertionType){
            if(assertionType.contains("95th")){
                return percentiles95;
            } else if(assertionType.contains("mean")){
                return mean;
            } else if(assertionType.contains("KO")){
                return ko;
            } else if(assertionType.contains("min")){
                return min;
            } else if(assertionType.contains("max")){
                return max;
            } else if(assertionType.contains("standard deviation")){
                return stddev;
            } else if(assertionType.contains("requests per second")){
                return throughput;
            }
            throw new IllegalArgumentException("Unexpected gatling type: " + assertionType);
        }
    }

    public String getGraphiteUrlForAssertion(Date fromDate, AssertionData assertionData) {
        final Map<String, String> values = buildValuesForTemplate(fromDate, assertionData);
        return fillInTemplate(getRootUrl() +
                getKOTarget() +
                "&" + getPerformanceStatTarget() +
                "&" + getPerformanceAssertThresholdTarget() +
                "&" + getReleaseBranchTarget() +
                "&" + getRenderOptions(), values);
    }

    protected String getRootUrl() {
        return ROOT_GRAPHITE_URL;
    }

    protected String getKOTarget() {
        return KO_TARGET;
    }

    protected String getPerformanceStatTarget() {
        return PERFORMANCE_STAT_TARGET;
    }

    protected String getPerformanceAssertThresholdTarget() {
        return PERFORMANCE_ASSERT_THRESHOLD_TARGET;

    }

    protected String getReleaseBranchTarget() {
        return RELEASE_BRANCH_TARGET;
    }

    protected String getRenderOptions() {
        return RENDER_OPTIONS;
    }

    protected String fillInTemplate(String template,  Map<String,String> values){
        if(values == null)
            return null;
        StrSubstitutor sub = new StrSubstitutor(values);
        return sub.replace(template);
    }

    protected Map<String,String> buildValuesForTemplate(
            Date fromDate, AssertionData assertionData){
        try{
            Map<String,String> values = new HashMap<String,String>();
            String env = getEnvFromProjectName(assertionData.projectName);
            if(env != null){
                GRAPHITE_ASSERT_TYPE graphiteAssertionType =
                        convertAssertionTypeFromGatlingToGraphite(assertionData.assertionType);
                String performanceMetricLabel = PERFORMANCE_METRIC_LABEL_RESPONSE_TIME;
                if(isPerformanceAssert(graphiteAssertionType)){
                    if(graphiteAssertionType == GRAPHITE_ASSERT_TYPE.throughput){
                        performanceMetricLabel = PERFORMANCE_METRIC_LABEL_THROUGHPUT;
                    }
                    setUrlEncodedValue(values, "env", env);
                    setUrlEncodedValue(values, "simName",
                            graphiteSanitize(assertionData.simulationName));
                    setUrlEncodedValue(values, "reqName",
                            graphiteSanitize(gatlingRequestNameToGraphiteRequestName(
                            assertionData.requestName)));
                    setUrlEncodedValue(values, "assertName", graphiteAssertionType.name());
                    setUrlEncodedValue(values, "assertDescr", assertionData.assertionType);
                    setUrlEncodedValue(values, "projName", assertionData.projectName);
                    setUrlEncodedValue(values, "performanceMetricLabel", performanceMetricLabel);
                    setUrlEncodedValue(values, "fromDateTime", convertDateToFromValue(fromDate));
                    return values;
                }
            }
        }catch(UnsupportedEncodingException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }

    private void setUrlEncodedValue(
            Map<String, String> values, String valueName,
            String value) throws UnsupportedEncodingException {
        values.put(valueName, URLEncoder.encode(value, "UTF-8"));
    }

    private boolean isPerformanceAssert(GRAPHITE_ASSERT_TYPE graphiteAssertionType) {
        return (graphiteAssertionType != null) &&
                (graphiteAssertionType != GRAPHITE_ASSERT_TYPE.ko);
    }

    private String convertDateToFromValue(Date inputDate) {
        try {
            return graphiteFromFormat.format(inputDate);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Failed to find date/time of oldest build for project.  " +
                            "Defaulting range to -1 month", e);
            return "-1months";
        }
    }

    private String getEnvFromProjectName(String projectName) {
        Matcher matcher = envPattern.matcher(projectName);
        if(matcher.find()){
            return matcher.group(1).toLowerCase();
        }
        return null;
    }


    private String gatlingRequestNameToGraphiteRequestName(String requestName) {
        if(requestName.compareTo("Global") == 0){
            return "Global_Information";
        }
        return requestName;
    }

    private String graphiteSanitize(String data) {
        return data.replaceAll("[^\\w\\.\\-_]", "_");
    }

    private GRAPHITE_ASSERT_TYPE convertAssertionTypeFromGatlingToGraphite(String assertionType) {
        if(assertionType == null)
            return null;
        try{
            return GRAPHITE_ASSERT_TYPE.fromGatlingAssertType(assertionType);
        }catch(Exception ex){
            logger.log(Level.WARNING, "Failed to convert gatling type " + assertionType +
                    " to graphite type.", ex);
        }
        return null;
    }

}
