package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

import org.junit.Assert;
import org.junit.Test;

public class BrandTest {

    @Test
    public void testGetBrandFromNameValidNames() {
        Assert.assertEquals(Brand.SHUTTERFLY, Brand.getBrandFromName("sfly"));
        Assert.assertEquals(Brand.SHUTTERFLY, Brand.getBrandFromName("SFLY"));
        Assert.assertEquals(Brand.TINYPRINTS, Brand.getBrandFromName("tp"));
        Assert.assertEquals(Brand.TINYPRINTS, Brand.getBrandFromName("TP"));
    }

    @Test
    public void testGetBrandFromNameInValidNames() {
        Assert.assertNull(Brand.getBrandFromName(""));
        Assert.assertNull(Brand.getBrandFromName(" "));
        Assert.assertNull(Brand.getBrandFromName(null));
        Assert.assertNull(Brand.getBrandFromName(" some other value "));
    }
}
