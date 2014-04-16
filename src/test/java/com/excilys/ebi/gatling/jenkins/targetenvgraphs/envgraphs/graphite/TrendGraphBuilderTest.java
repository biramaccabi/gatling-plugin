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
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing this class via it's public and protected methods.  I prefer to only just the public
 * interface, but in this case, I think this is a cleaner approach and will allow us to change the
 * implementation without making lots of changes to the tests. -VITO
 */
public class TrendGraphBuilderTest {
    private TrendGraphBuilder trendGraphBuilder;
    private AssertionData assertionData;
    private Date expectedFromDate;
    private String expectedFromTime;
    private String expectedGraphiteUrl;

    @Before
    public void setup() throws UnsupportedEncodingException {
        trendGraphBuilder = new TrendGraphBuilder();
        Calendar expectedFromCalendar = Calendar.getInstance();
        expectedFromCalendar.add(Calendar.MONTH, -3);
        expectedFromDate = expectedFromCalendar.getTime();
        SimpleDateFormat graphiteFormat = new SimpleDateFormat("HH:mm_yyyyMMdd");
        expectedFromTime = URLEncoder.encode(graphiteFormat.format(expectedFromDate), "UTF-8");

        assertionData = new AssertionData();
        assertionData.actualValue = "actualValue";
        assertionData.assertionType = "95th percentile response time";
        assertionData.expectedValue = "expectedValue";
        assertionData.message = "authorize 95th percentile response time is less than 1000";
        assertionData.projectName =
                "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        assertionData.requestName = "authorize";
        assertionData.simulationName = "oauth2forapi2simulation";
        assertionData.scenerioName = "scenerioName";
        assertionData.status = "false";

        String expectedRootUrl = "http://tre-stats.internal.shutterfly.com/render?";
        String expectedKOTarget =
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation." +
                "authorize.ko.percent)%2C%22red%22)%2C%22percent%20KOs%22)";
        String expectedPerformanceStatTarget = "target=alias(load.summary.foxtrot." +
                "oauth2forapi2simulation.authorize.all." +
                "percentiles95%2C%2295th+percentile+response+time%22)";
        String expectedPerformanceAssertThresholdTarget =
                "target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all." +
                "expected.percentiles95%2C%22performance+assert+threshold%22)";
        String expectedReleaseBranchTarget =
                "target=alias(color(lineWidth(drawAsInfinite(integral(" +
                "sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C%22" +
                "Release%20Branch%20Created%22)";
        String expectedRenderOptions =
                "width=586&height=308&lineMode=connected&from=" + expectedFromTime +
                "&title=authorize+-+95th+percentile+response+time" +
                "&vtitle=Response_Time_in_ms&vtitleRight=Percentage_KOs" +
                "&bgcolor=FFFFFF&fgcolor=000000&yMaxRight=100" +
                "&yMinRight=0&hideLegend=false&uniqueLegend=true";
        expectedGraphiteUrl = expectedRootUrl + expectedKOTarget + "&" +
                expectedPerformanceStatTarget + "&" + expectedPerformanceAssertThresholdTarget +
                "&" + expectedReleaseBranchTarget + "&" + expectedRenderOptions;


    }

    @Test
    public void test_getGraphiteUrlForAssertion_generates_expected_url(){
        assertEquals(expectedGraphiteUrl,
                trendGraphBuilder.getGraphiteUrlForAssertion(expectedFromDate, assertionData));
    }

    @Test
    public void test_getGraphiteUrlForAssertion_combines_parts_correctly(){

        String graphiteUrl =
                trendGraphBuilder.getGraphiteUrlForAssertion(
                        expectedFromDate,assertionData);
        final String expectedTemplate = trendGraphBuilder.getRootUrl() +
                trendGraphBuilder.getKOTarget() + "&" +
                trendGraphBuilder.getPerformanceStatTarget() + "&" +
                trendGraphBuilder.getPerformanceAssertThresholdTarget() + "&" +
                trendGraphBuilder.getReleaseBranchTarget() + "&" +
                trendGraphBuilder.getRenderOptions();
        Map<String,String> values = trendGraphBuilder.buildValuesForTemplate(
                expectedFromDate,
                assertionData);
        String expectedGraphiteUrl =
                trendGraphBuilder.fillInTemplate(expectedTemplate, values);
        assertEquals(expectedGraphiteUrl, graphiteUrl);
    }

    @Test
    public void test_getRootUrl(){
        assertEquals(TrendGraphBuilder.ROOT_GRAPHITE_URL, trendGraphBuilder.getRootUrl());
    }

    @Test
    public void test_getKOTarget(){
        assertEquals(TrendGraphBuilder.KO_TARGET, trendGraphBuilder.getKOTarget());
    }

    @Test
    public void test_getPerformanceStatTarget(){
        assertEquals(TrendGraphBuilder.PERFORMANCE_STAT_TARGET,
                trendGraphBuilder.getPerformanceStatTarget());
    }

    @Test
    public void test_getPerformanceAssertThresholdTarget(){
        assertEquals(TrendGraphBuilder.PERFORMANCE_ASSERT_THRESHOLD_TARGET,
                trendGraphBuilder.getPerformanceAssertThresholdTarget());
    }

    @Test
    public void test_getReleaseBranchTarget(){
        assertEquals(TrendGraphBuilder.RELEASE_BRANCH_TARGET,
                trendGraphBuilder.getReleaseBranchTarget());
    }

    @Test
    public void test_getRenderOptions(){
        assertEquals(TrendGraphBuilder.RENDER_OPTIONS,
                trendGraphBuilder.getRenderOptions());
    }

    @Test
    public void test_buildValuesForTemplate_malformed_project_name(){
        assertionData.projectName = "malformed";
        assertNull(trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData));
    }

    @Test
    public void test_buildValuesForTemplate_not_performance_assert(){
        assertionData.assertionType = "percentage of requests KO";
        assertNull(trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData));
    }

    @Test
    public void test_buildValuesForTemplate_performanceMetricLabel_throughput(){
        assertionData.assertionType = "requests per second";
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(TrendGraphBuilder.PERFORMANCE_METRIC_LABEL_THROUGHPUT,
                values.get("performanceMetricLabel"));
    }

    @Test
    public void test_buildValuesForTemplate_performanceMetricLabel_not_throughput(){
        assertionData.assertionType = "95th percentile response time";
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(TrendGraphBuilder.PERFORMANCE_METRIC_LABEL_RESPONSE_TIME,
                values.get("performanceMetricLabel"));
    }

    @Test
    public void test_buildValuesForTemplate_env(){
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals("foxtrot", values.get("env"));
    }

    @Test
    public void test_buildValuesForTemplate_simName(){
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(assertionData.simulationName, values.get("simName"));
    }

    @Test
    public void test_buildValuesForTemplate_simName_graphite_sanitize(){
        assertionData.simulationName = "the+name!is";
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals("the_name_is", values.get("simName"));
    }

    @Test
    public void test_buildValuesForTemplate_reqName(){
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(assertionData.requestName, values.get("reqName"));
    }

    @Test
    public void test_buildValuesForTemplate_reqName_global(){
        assertionData.requestName = "Global";
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals("Global_Information", values.get("reqName"));
    }

    @Test
    public void test_buildValuesForTemplate_reqName_graphite_sanitize(){
        assertionData.requestName = "funky+name";
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals("funky_name", values.get("reqName"));
    }

    @Test
    public void test_buildValuesForTemplate_assertName(){
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(TrendGraphBuilder.GRAPHITE_ASSERT_TYPE.percentiles95.name(),
                values.get("assertName"));
    }

    @Test
    public void test_buildValuesForTemplate_assertDescr() throws UnsupportedEncodingException {
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(URLEncoder.encode(assertionData.assertionType, "UTF-8"),
                values.get("assertDescr"));
    }

    @Test
    public void test_buildValuesForTemplate_projName() throws UnsupportedEncodingException {
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(URLEncoder.encode(assertionData.projectName, "UTF-8"),
                values.get("projName"));
    }

    @Test
    public void test_buildValuesForTemplate_fromDateTime() throws UnsupportedEncodingException {
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(expectedFromDate, assertionData);
        assertEquals(expectedFromTime,
                values.get("fromDateTime"));
    }

    @Test
    public void test_buildValuesForTemplate_fromDateTime_nullDate()
            throws UnsupportedEncodingException {
        final Map<String, String> values =
                trendGraphBuilder.buildValuesForTemplate(null, assertionData);
        assertEquals("-1months", values.get("fromDateTime"));
    }
}