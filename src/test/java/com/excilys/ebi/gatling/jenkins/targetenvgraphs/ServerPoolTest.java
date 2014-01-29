package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

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
