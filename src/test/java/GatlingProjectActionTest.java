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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class GatlingProjectActionTest{
    private AbstractProject<?, ?> project;
    private GatlingProjectAction projectAction;
    private String gatlingReportUrl = "0/charts";

    @Before
    public void setup(){
        project = mock(AbstractProject.class);
        projectAction = new GatlingProjectAction(project, gatlingReportUrl);
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
        projectAction = new GatlingProjectAction(project, gatlingReportUrl);
        stub(runList.iterator()).toReturn(builds.iterator());
        //noinspection unchecked
        stub(project.getBuilds()).toReturn(runList);
        if(builds.size() > 0){
            AbstractBuild build = builds.get(0);
            GatlingBuildAction buildAction = new GatlingBuildAction(build,new ArrayList<BuildSimulation>(),assertDataList);
            stub(build.getAction(GatlingBuildAction.class)).toReturn(buildAction);
        }
    }

}