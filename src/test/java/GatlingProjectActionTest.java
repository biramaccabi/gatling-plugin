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
import junit.framework.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class GatlingProjectActionTest{
    AbstractProject<?, ?> project;
    GatlingProjectAction projectAction;

    @Before
    public void setup(){
        project = mock(AbstractProject.class);
        projectAction = new GatlingProjectAction(project);
    }

    @org.junit.Test
    public void test_no_builds(){
        prepareWithNoBuilds();
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    private void prepareWithNoBuilds() {
        prepareWithBuilds(new ArrayList<AbstractBuild>(), null);
    }

    @org.junit.Test
    public void test_build_with_action_but_null_assert_list(){
        prepareWithNullAssertList();
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    private void prepareWithNullAssertList() {
        prepareWithOneBuild();
    }

    @org.junit.Test
    public void test_build_with_action_but_empty_assert_list(){
        prepareWithEmptyAssertList();
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    private void prepareWithEmptyAssertList() {
        prepareWithOneBuild(new ArrayList<AssertionData>());
    }

    private void prepareWithOneBuild() {
        prepareWithOneBuild(null);
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
        stub(project.getBuilds()).toReturn(runList);
        if(builds.size() > 0){
            AbstractBuild build = builds.get(0);
            GatlingBuildAction buildAction = new GatlingBuildAction(build,new ArrayList<BuildSimulation>(),assertDataList);
            stub(build.getAction(GatlingBuildAction.class)).toReturn(buildAction);
        }
    }

    @org.junit.Test
    public void test_build_with_action_and_one_performance_assert(){
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
        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(data);

        prepareWithOneBuild(assertionDataList);
        List<String> graphiteGraphUrls = projectAction.getGraphiteGraphUrls();
        Assert.assertEquals(1, graphiteGraphUrls.size());
        String graphiteUrl = graphiteGraphUrls.get(0);
        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2forapi2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2forapi2simulation.authorize.all.percentiles95%2C" +
                "%2295th+percentile+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=%22authorize%20Web_Performance_Tests-foxtrot-apiserver_OAuth2ForApi2Simulation%22";
        Assert.assertEquals(expectedGraphiteUrl, graphiteUrl);

    }

    @org.junit.Test
    public void test_build_with_action_and_one_performance_assert_2(){
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "95th percentile response time";
        data.expectedValue = "expectedValue";
        data.message = "authorize 95th percentile response time is less than 1000";
        data.projectName = "VPAINE_Web_Performance_Tests-foxtrot-apiserver.OAuth2Simulation";
        data.requestName = "authorize";
        data.simulationName = "oauth2simulation";
        data.scenerioName = "scenerioName";
        data.status = "false";
        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(data);

        prepareWithOneBuild(assertionDataList);
        List<String> graphiteGraphUrls = projectAction.getGraphiteGraphUrls();
        Assert.assertEquals(1, graphiteGraphUrls.size());
        String graphiteUrl = graphiteGraphUrls.get(0);
        String expectedGraphiteUrl =
            "http://tre-stats.internal.shutterfly.com/render/?_salt=1384804572.787&" +
                "target=alias(color(secondYAxis(load.summary.foxtrot.oauth2simulation.authorize.ko.percent)%2C" +
                "%22red%22)%2C%22percent%20KOs%22)" +
                "&target=alias(load.summary.foxtrot.oauth2simulation.authorize.all.percentiles95%2C" +
                "%2295th+percentile+response+time%22)" +
                "&target=alias(color(lineWidth(drawAsInfinite(maxSeries(sfly.releng.branch.*))%2C1)%2C%22yellow%22)%2C"+
                "%22Release%20Branch%20Created%22)&width=586&height=308&lineMode=connected" +
                "&from=-1months" +
                "&title=%22authorize%20VPAINE_Web_Performance_Tests-foxtrot-apiserver.OAuth2Simulation%22";
        Assert.assertEquals(expectedGraphiteUrl, graphiteUrl);

    }


    @org.junit.Test
    public void test_build_with_action_and_one_performance_assert_not_foxtrot(){
        AssertionData data = new AssertionData();
        data.actualValue = "actualValue";
        data.assertionType = "95th percentile response time";
        data.expectedValue = "expectedValue";
        data.message = "message";
        data.projectName = "Web_Performance_Tests-beta-apiserver_OAuth2ForApi2Simulation";
        data.requestName = "authorize";
        data.scenerioName = "scenerioName";
        data.status = "false";
        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(data);

        prepareWithOneBuild(assertionDataList);
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }


}