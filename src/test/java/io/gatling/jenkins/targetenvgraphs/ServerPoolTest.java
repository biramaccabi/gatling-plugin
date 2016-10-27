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
package io.gatling.jenkins.targetenvgraphs;

import org.junit.Assert;
import org.junit.Test;

public class ServerPoolTest {

    @Test
    public void testGetEnumForPoolNameValidPoolName() {
        ServerPool expected = ServerPool.APISERVER;
        ServerPool retrieved = ServerPool.getEnumForPoolName(ServerPool.APISERVER.longName);

        Assert.assertEquals(expected, retrieved);
    }

    @Test
    public void testGetEnumForPoolNameInvalidPoolName() {
        ServerPool expected = null;
        ServerPool retrieved = ServerPool.getEnumForPoolName("This Is Not A Real Pool");

        Assert.assertEquals(expected, retrieved);
    }
}
