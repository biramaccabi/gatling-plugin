package com.excilys.ebi.gatling.jenkins; /**
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

import com.excilys.ebi.gatling.jenkins.targetenvgraphs.envgraphs.graphite.TrendGraphBuilder;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class GatlingProjectActionTest{
    private AbstractProject<?, ?> project;
    private GatlingProjectAction projectAction;
    private static Calendar expectedFromTimestamp;
    private TrendGraphBuilder mock_builder;

    @Before
    public void setup(){
        project = mock(AbstractProject.class);
        projectAction = new GatlingProjectAction(project, "0/charts");
        projectAction.trendGraphBuilder = mock_builder = mock(TrendGraphBuilder.class);
    }

    @Test
    public void test_with_build(){
        AssertionData assertionData = new AssertionData();

        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(assertionData);
        prepareWithOneBuild(assertionDataList);
        final String expected_url = "URL";
        stub(mock_builder.getGraphiteUrlForAssertion((Date)anyObject(),
                (AssertionData)anyObject())).toReturn(expected_url);

        List<String> graphiteGraphUrls = projectAction.getGraphiteGraphUrls();

        verify(mock_builder, times(1)).getGraphiteUrlForAssertion(
                eq(expectedFromTimestamp.getTime()), eq(assertionData));
        Assert.assertEquals(1, graphiteGraphUrls.size());
        Assert.assertEquals(expected_url, graphiteGraphUrls.get(0));
    }

    @Test
    public void test_with_build_builderThrowsException(){
        AssertionData assertionData = new AssertionData();

        List<AssertionData> assertionDataList = new ArrayList<AssertionData>();
        assertionDataList.add(assertionData);
        prepareWithOneBuild(assertionDataList);
        stub(mock_builder.getGraphiteUrlForAssertion((Date)anyObject(),
                (AssertionData)anyObject())).toThrow(new RuntimeException("EX"));

        List<String> graphiteGraphUrls = projectAction.getGraphiteGraphUrls();

        verify(mock_builder, times(1)).getGraphiteUrlForAssertion(
                eq(expectedFromTimestamp.getTime()), eq(assertionData));
        Assert.assertEquals(0, graphiteGraphUrls.size());
    }

    @Test
    public void test_no_builds(){
        prepareWithBuilds(new ArrayList<AbstractBuild>(), null);
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    @Test
    public void test_build_with_action_but_null_assert_list(){
        prepareWithOneBuild(null);
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    @Test
    public void test_build_with_action_but_empty_assert_list(){
        prepareWithOneBuild(new ArrayList<AssertionData>());
        Assert.assertEquals(0, projectAction.getGraphiteGraphUrls().size());
    }

    @Test
    public void test_getReportURL(){
        final int build = 3;
        final String simName = "NAME";
        Assert.assertEquals(Integer.toString(build)  + "/" + PluginConstants.URL_NAME +
                        "/report/" + simName,
                projectAction.getReportURL(build, simName));
    }

    private void prepareWithBuilds(List<AbstractBuild> builds, List<AssertionData> assertDataList){
        RunList runList = mock(RunList.class);
        stub(runList.iterator()).toReturn(builds.iterator());
        //noinspection unchecked
        stub(project.getBuilds()).toReturn(runList);
        if(builds.size() > 0){
            AbstractBuild build = builds.get(0);
            GatlingBuildAction buildAction = new GatlingBuildAction(build,
                    new ArrayList<BuildSimulation>(),assertDataList);
            stub(build.getAction(GatlingBuildAction.class)).toReturn(buildAction);
        }
    }

    private void prepareWithOneBuild(List<AssertionData> assertDataList) {
        List<AbstractBuild> builds = new ArrayList<AbstractBuild>();
        AbstractBuild build = mock(AbstractBuild.class);
        if (expectedFromTimestamp == null) {
            expectedFromTimestamp = Calendar.getInstance();
            expectedFromTimestamp.add(Calendar.MONTH, -3);
        }
        stub(build.getTimestamp()).toReturn(expectedFromTimestamp);
        //noinspection unchecked
        stub(project.getFirstBuild()).toReturn(build);
        builds.add(build);
        prepareWithBuilds(builds, assertDataList);
    }



}