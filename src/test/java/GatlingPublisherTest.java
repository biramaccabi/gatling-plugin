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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class GatlingPublisherTest {

	private GatlingPublisher gatlingpublisher;
	private AssertionData koassertiondata;
	private AssertionData performanceassertiondata;
	private AssertionData unrecognizeassertiondata;
	private AssertionData trueassertiondata;


	@Before
	public void setUp(){
		gatlingpublisher = new GatlingPublisher(true);
		koassertiondata = new AssertionData();
		koassertiondata.projectName = "unittest mock project KO";
		koassertiondata.simulationName = "unittest mock simulation KO";
		koassertiondata.scenarioName = "unittest mock scenario KO";
		koassertiondata.requestName = "Global";
		koassertiondata.message = "Global percentage of failed requests is less than 1";
		koassertiondata.assertionType = "percentage of failed requests";
		koassertiondata.actualValue = "100";
		koassertiondata.expectedValue = "1";
		koassertiondata.status = false;
		performanceassertiondata = new AssertionData();
		performanceassertiondata.projectName = "unittest mock project performance";
		performanceassertiondata.simulationName = "unittest mock simulation performance";
		performanceassertiondata.scenarioName = "unittest mock scenario performance";
		performanceassertiondata.requestName = "Get Catalog Pricing";
		performanceassertiondata.message = "Get Catalog Pricing mean requests per second is greater than 2000";
		performanceassertiondata.assertionType = "mean requests per second";
		performanceassertiondata.actualValue = "200";
		performanceassertiondata.expectedValue = "2000";
		performanceassertiondata.status = false;
		unrecognizeassertiondata = new AssertionData();
		unrecognizeassertiondata.projectName = "unittest mock project unrecognize";
		unrecognizeassertiondata.simulationName = "unittest mock simulation unrecognize";
		unrecognizeassertiondata.scenarioName = "unittest mock scenario unrecognize";
		unrecognizeassertiondata.requestName = "Get Catalog Pricing unrecognize";
		unrecognizeassertiondata.message = "Get Catalog Pricing unrecognize per second is equal to 2000";
		unrecognizeassertiondata.assertionType = "unknown";
		unrecognizeassertiondata.actualValue = "200";
		unrecognizeassertiondata.expectedValue = "2000";
		unrecognizeassertiondata.status = false;
		trueassertiondata = new AssertionData();
		trueassertiondata.projectName = "unittest mock project true";
		trueassertiondata.simulationName = "unittest mock simulation true";
		trueassertiondata.scenarioName = "unittest mock scenario true";
		trueassertiondata.requestName = "Get Catalog Pricing";
		trueassertiondata.message = "Get Catalog Pricing mean requests per second is greater than 200";
		trueassertiondata.assertionType = "mean requests per second";
		trueassertiondata.actualValue = "300";
		trueassertiondata.expectedValue = "200";
		trueassertiondata.status = true;

	}

	@Test
	public void testKOBuildDescription(){
		List<AssertionData> koassertionlist = new ArrayList<AssertionData>();
		koassertionlist.add(koassertiondata);
		koassertionlist.add(koassertiondata);
		koassertionlist.add(trueassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(koassertionlist);
		String expect = "<b>KO</b><br>Global&nbsp;KO%=100,&nbsp;expect<1;<br>Global&nbsp;KO%=100,&nbsp;expect<1;<br>";
		assertEquals(generate, expect);
	}

	@Test
	public void testPerformanceBuildDescription(){
		List<AssertionData> performanceassertionlist = new ArrayList<AssertionData>();
		performanceassertionlist.add(performanceassertiondata);
		performanceassertionlist.add(performanceassertiondata);
		performanceassertionlist.add(trueassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(performanceassertionlist);
		String expect = "<b>PERFORMANCE</b><br>Get&nbsp;Catalog&nbsp;Pricing&nbsp;req/s=200,&nbsp;expect>2000;<br>Get&nbsp;Catalog&nbsp;Pricing&nbsp;req/s=200,&nbsp;expect>2000;<br>";
		assertEquals(expect, generate);
	}

	@Test
	public void testKOandPerformanceBuildDescription(){
		List<AssertionData> koandperformanceassertionlist = new ArrayList<AssertionData>();
		koandperformanceassertionlist.add(performanceassertiondata);
		koandperformanceassertionlist.add(koassertiondata);
		koandperformanceassertionlist.add(performanceassertiondata);
		koandperformanceassertionlist.add(koassertiondata);
		koandperformanceassertionlist.add(trueassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(koandperformanceassertionlist);
		String expect = "<b>KO AND PERFORMANCE</b><br>Get&nbsp;Catalog&nbsp;Pricing&nbsp;req/s=200,&nbsp;expect>2000;<br>Global&nbsp;KO%=100,&nbsp;expect<1;<br>" +
			"Get&nbsp;Catalog&nbsp;Pricing&nbsp;req/s=200,&nbsp;expect>2000;<br>Global&nbsp;KO%=100,&nbsp;expect<1;<br>";
		assertEquals(expect, generate);
	}

	@Test
	public void testTrueBuildDescription(){
		List<AssertionData> trueassertionlist = new ArrayList<AssertionData>();
		trueassertionlist.add(trueassertiondata);
		trueassertionlist.add(trueassertiondata);
		String generate = gatlingpublisher.generateBuildDescriptionFromAssertionData(trueassertionlist);
		String expect = "";
		assertEquals(expect, generate);
	}

	@Test
	public void testCommandExtraction(){
		String command = "-Pperformance-test clean verify -Dgatling.simulationClass=appserver.catalog.CatalogPriceSimulation";
		Pattern pattern = Pattern.compile(".*-Dgatling\\.simulationClass='?([a-zA-Z0-9\\.]+).*");
		String generate = gatlingpublisher.hasMatchSimulationClass(command,pattern);
		String expect = "appserver.catalog.CatalogPriceSimulation";
		assertEquals(expect, generate);
	}

	@Test
	public void testCommandExtractionWithQuote(){
		String command = "-Pperformance-test clean verify -Dgatling.simulationClass=&apos;appserver.catalog.CatalogPriceSimulation&apos;";
		Pattern pattern = Pattern.compile(".*-Dgatling\\.simulationClass='?([a-zA-Z0-9\\.]+).*");
		String generate = gatlingpublisher.hasMatchSimulationClass(command,pattern);
		String expect = "appserver.catalog.CatalogPriceSimulation";
		assertEquals(expect, generate);
	}

	@Test
	public void testCommandExtractionWithQuoteChar(){
		String command = "-Pperformance-test clean verify -Dgatling.simulationClass='appserver.catalog.CatalogPriceSimulation'";
		Pattern pattern = Pattern.compile(".*-Dgatling\\.simulationClass='?([a-zA-Z0-9\\.]+).*");
		String generate = gatlingpublisher.hasMatchSimulationClass(command,pattern);
		String expect = "appserver.catalog.CatalogPriceSimulation";
		assertEquals(expect, generate);
	}



}