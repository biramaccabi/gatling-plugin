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

import com.excilys.ebi.gatling.jenkins.AssertionData;
import com.excilys.ebi.gatling.jenkins.BuildSimulation;
import com.excilys.ebi.gatling.jenkins.GatlingBuildAction;
import com.excilys.ebi.gatling.jenkins.GatlingProjectAction;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@RunWith(Parameterized.class)
public class GatlingProjectActionGraphiteUrlTest {
    private final String expecdtedUrl;
    private final AssertionData assertionData;

    private AbstractProject<?, ?> project;
    private GatlingProjectAction projectAction;

    @SuppressWarnings("UnusedParameters")
    public GatlingProjectActionGraphiteUrlTest(String paramName, AssertionData assertionData, String expectedUrl){
        this.assertionData = assertionData;
        this.expecdtedUrl = expectedUrl;
    }

    @Parameterized.Parameters(name = "{0}")
    public static java.util.Collection<Object[]> data() {
        Object[][] params = new Object[][] {
            param_OAuth2ForApi2Simulation_95th(),
            param_OAuth2ForApi2Simulation_min(),
            param_OAuth2ForApi2Simulation_max(),
            param_OAuth2ForApi2Simulation_mean(),
            param_OAuth2ForApi2Simulation_stddev(),
            param_OAuth2ForApi2Simulation_throughput(),
            param_OAuth2ForApi2Simulation_KO(),
            param_OAuth2ForApi2Simulation_95th_req_and_sim_name_has_spaces(),
            param_OAuth2ForApi2Simulation_global()};
        return Arrays.asList(params);
    }

    private static Object[] param_OAuth2ForApi2Simulation_95th_req_and_sim_name_has_spaces(){
        String title = "param_OAuth2ForApi2Simulation_95th";

        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "95th percentile response time";
        data.expectedValue = "expectedValue";
        data.message = "authorize 95th percentile response time is less than 1000";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize with space";
        data.simulationName = "oauth2forapi2simulation test";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation_test.authorize_with_space.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation_test.authorize_with_space.all.percentiles95%2C" +
                "%2295th+percentile+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize_with_space+-+95th+percentile+response+time";
        return new Object[] {title,
            data, expectedGraphiteUrl } ;
    }
    private static Object[] param_OAuth2ForApi2Simulation_95th(){
        String title = "param_OAuth2ForApi2Simulation_95th";

        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "95th percentile response time";
        data.expectedValue = "expectedValue";
        data.message = "authorize 95th percentile response time is less than 1000";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.percentiles95%2C" +
                "%2295th+percentile+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+95th+percentile+response+time";
        return new Object[] {title,
            data, expectedGraphiteUrl } ;
    }

    private static Object[] param_OAuth2ForApi2Simulation_global(){
        String title = "param_OAuth2ForApi2Simulation_95th";

        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "95th percentile response time";
        data.expectedValue = "expectedValue";
        data.message = "authorize 95th percentile response time is less than 1000";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "Global";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.Global_Information.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.Global_Information.all.percentiles95%2C" +
                "%2295th+percentile+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=Global_Information+-+95th+percentile+response+time";
        return new Object[] {title,
            data, expectedGraphiteUrl } ;
    }
    private static Object[] param_OAuth2ForApi2Simulation_min(){
        String title = "param_OAuth2ForApi2Simulation_min";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "min response time";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.min%2C" +
                "%22min+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+min+response+time";
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }

    private static Object[] param_OAuth2ForApi2Simulation_max(){
        String title = "param_OAuth2ForApi2Simulation_max";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "max response time";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.max%2C" +
                "%22max+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+max+response+time";
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }

       private static Object[] param_OAuth2ForApi2Simulation_mean(){
        String title = "param_OAuth2ForApi2Simulation_mean";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "mean response time";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.mean%2C" +
                "%22mean+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+mean+response+time";
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }

    private static Object[] param_OAuth2ForApi2Simulation_stddev(){
        String title = "param_OAuth2ForApi2Simulation_stddev";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "standard deviation response time";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.stddev%2C" +
                "%22standard+deviation+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+standard+deviation+response+time";
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }

    private static Object[] param_OAuth2ForApi2Simulation_throughput(){
        String title = "param_OAuth2ForApi2Simulation_throughput";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "requests per second";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.throughput%2C" +
                "%22requests+per+second%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=authorize+-+requests+per+second";
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }

    private static Object[] param_OAuth2ForApi2Simulation_KO(){
        String title = "param_OAuth2ForApi2Simulation_KO";
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "percentage of requests KO";
        data.expectedValue = "expectedValue";
        data.message = "blah";
        data.projectName = "Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2forapi2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";

        String expectedGraphiteUrl = null;
        return new Object[] { title,
            data, expectedGraphiteUrl } ;
    }
    @Before
    public void setup(){
        project = mock(AbstractProject.class);
        projectAction = new GatlingProjectAction(project);
    }

    private void prepareWithOneBuild(List<AssertionData> assertDataList) {
        List<AbstractBuild> builds = new ArrayList<AbstractBuild>();
        AbstractBuild build = mock(AbstractBuild.class);
        builds.add(build);
        prepareWithBuilds(builds, assertDataList);
    }

    private void prepareWithBuilds(List<AbstractBuild> builds, List<AssertionData> assertDataList){
        RunList runList = mock(RunList.class);
        projectAction = new GatlingProjectAction(project);
        stub(runList.iterator()).toReturn(builds.iterator());
        //noinspection unchecked
        stub(project.getBuilds()).toReturn(runList);
        if(builds.size() > 0){
            AbstractBuild build = builds.get(0);
            GatlingBuildAction buildAction = new GatlingBuildAction(build,new ArrayList<BuildSimulation>(),assertDataList);
            stub(build.getAction(GatlingBuildAction.class)).toReturn(buildAction);
        }
    }

    @org.junit.Test
    public void test_build_with_action_and_one_performance_assert(){
        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(assertionData);
        prepareWithOneBuild(assertionDataList);

        List<String> graphiteGraphUrls = projectAction.getGraphiteGraphUrls();
        if(expecdtedUrl == null)
            Assert.assertEquals(0, graphiteGraphUrls.size());
        else{
            Assert.assertEquals(1, graphiteGraphUrls.size());
            String graphiteUrl = graphiteGraphUrls.get(0);
            Assert.assertEquals(expecdtedUrl, graphiteUrl);
        }
    }



}