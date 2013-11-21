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

import org.junit.Before;
import org.junit.Test;
import hudson.model.Result;
import com.excilys.ebi.gatling.jenkins.LastBuildColumn;
import static org.junit.Assert.assertEquals;

public class LastBuildColumnTest {

	private LastBuildColumn lastbuildcol;

	@Before
	public void setUp(){
		lastbuildcol = new LastBuildColumn();
	}

	@Test
	public void testPassCase(){
		String generate = lastbuildcol.getLastBuildDescription("",Result.SUCCESS);
		String expect = "";
		assertEquals(generate, expect);
	}

	@Test
	public void testFailureCase(){
		String generate = lastbuildcol.getLastBuildDescription("",Result.FAILURE);
		String expect = "FAILURE";
		assertEquals(generate, expect);
	}

	@Test
	public void testAbortedCase(){
		String generate = lastbuildcol.getLastBuildDescription("",Result.ABORTED);
		String expect = "ABORTED";
		assertEquals(generate, expect);
	}

}