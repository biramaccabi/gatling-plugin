/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.excilys.ebi.gatling.jenkins.AssertionData;
import com.excilys.ebi.gatling.jenkins.GatlingPublisher;
import hudson.model.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GatlingPublisherTest {

	private GatlingPublisher gatlingpublisher;
	private AssertionData koassertiondata;
	private AssertionData performanceassertiondata;


	@Before
	public void setUp(){
		gatlingpublisher = new GatlingPublisher(true);
		koassertiondata = new AssertionData();
		koassertiondata.projectName = "unittest mock project KO";
		koassertiondata.simulationName = "unittest mock simulation KO";
		koassertiondata.scenerioName = "unittest mock scenario KO";
		koassertiondata.requestName = "unittest mock request KO";
		koassertiondata.message = "Global percentage of requests KO is less than 1";
		koassertiondata.assertionType = "percentage of requests KO";
		koassertiondata.actualValue = "100";
		koassertiondata.expectedValue = "1";
		koassertiondata.status = "false";
		performanceassertiondata = new AssertionData();
		performanceassertiondata.projectName = "unittest mock project performance";
		performanceassertiondata.simulationName = "unittest mock simulation performance";
		performanceassertiondata.scenerioName = "unittest mock scenario performance";
		performanceassertiondata.requestName = "unittest mock request performance";
		performanceassertiondata.message = "Get Catalog Pricing requests per second is greater than 2000";
		performanceassertiondata.assertionType = "requests per second";
		performanceassertiondata.actualValue = "200";
		performanceassertiondata.expectedValue = "2000";
		performanceassertiondata.status = "false";

	}

	@Test
	public void testKOBuildDescription(){
		List<AssertionData> koassertionlist = new ArrayList<AssertionData>();
		koassertionlist.add(koassertiondata);
		koassertionlist.add(koassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(koassertionlist);
		String expect = "KO<br>Global percentage of requests KO is less than 1 : false - Actual Value : 100<br>Global percentage of requests KO is less than 1 : false - Actual Value : 100<br>";
		assertEquals(generate, expect);
	}

	@Test
	public void testPerformanceBuildDescription(){
		List<AssertionData> performanceassertionlist = new ArrayList<AssertionData>();
		performanceassertionlist.add(performanceassertiondata);
		performanceassertionlist.add(performanceassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(performanceassertionlist);
		String expect = "PERFORMANCE<br>Get Catalog Pricing requests per second is greater than 2000 : false - Actual Value : 200<br>Get Catalog Pricing requests per second is greater than 2000 : false - Actual Value : 200<br>";
		System.out.println(generate);
		assertEquals(expect, generate);
	}

	@Test
	public void testKOandPerformanceBuildDescription(){
		List<AssertionData> koandperformanceassertionlist = new ArrayList<AssertionData>();
		koandperformanceassertionlist.add(performanceassertiondata);
		koandperformanceassertionlist.add(koassertiondata);
		koandperformanceassertionlist.add(performanceassertiondata);
		koandperformanceassertionlist.add(koassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(koandperformanceassertionlist);
		String expect = "KO AND PERFORMANCE<br>Get Catalog Pricing requests per second is greater than 2000 : false - Actual Value : 200<br>Global percentage of requests KO is less than 1 : false - Actual Value : 100<br>" +
			"Get Catalog Pricing requests per second is greater than 2000 : false - Actual Value : 200<br>Global percentage of requests KO is less than 1 : false - Actual Value : 100<br>";
		assertEquals(expect, generate);
	}



}