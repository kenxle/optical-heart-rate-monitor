package com.hcii.kennethrstclairiii.heart_rate

/**
 * Created by kennethrstclairiii on 10/19/17.
 */
import org.junit.Test
import org.junit.Assert

class TestProcessing() {

    @Test fun testDemean() {
        Assert.assertEquals("demean",
                listOf(-1.0, 0.0, 1.0),
                listOf(1.0, 2.0, 3.0).demean())
        Assert.assertEquals("demean",
                listOf(-5.0, 0.0, 5.0),
                listOf(0.0, 5.0, 10.0).demean())
    }

    @Test fun testAbsMax() {
        Assert.assertEquals(3.0, listOf(1.0, 2.0, 3.0).absMax(), 0.0)
        Assert.assertEquals(10.0, listOf(0.0, 5.0, -10.0).absMax(), 0.0)
    }

    @Test fun testNormalize() {
        Assert.assertEquals("negative normalize",
                listOf(-1.0, 0.0, 1.0),
                listOf(-10.0, 0.0, 10.0).normalize())
        Assert.assertEquals("positive normalize",
                listOf(0.0, 0.5, 1.0),
                listOf(0.0, 5.0, 10.0).normalize())
    }

    @Test fun testDifferentiate() {
        Assert.assertEquals("positive difference",
                listOf(2.0, 3.0),
                listOf(1.0, 3.0, 6.0).differentiate())
        Assert.assertEquals("negative difference",
                listOf(-6.0, 11.0),
                listOf(5.0, -1.0, 10.0).differentiate())
        Assert.assertEquals("point difference",
                "[]",
                listOf(5.0).differentiate().toString())
    }

    @Test fun testMedian() {
        Assert.assertEquals(2.0, listOf(1.0, 2.0, 3.0).median(), 0.0)
        Assert.assertEquals(2.5, listOf(1.0, 2.0, 3.0, 4.0).median(), 0.0)
    }

    @Test fun testMedianFilter() {
        val orig = listOf(6.0, 1.0, 2.0, 3.0, 4.0, 5.0, 0.0)
        Assert.assertEquals("medianFilter2",
                listOf(3.5, 1.5, 2.5, 3.5, 4.5, 2.5),
                orig.medianFilter(2))
        Assert.assertEquals("orig not changed", listOf(6.0, 1.0, 2.0, 3.0, 4.0, 5.0, 0.0), orig)
        Assert.assertEquals("medianFilter4",
                listOf(2.5, 2.5, 3.5, 3.5),
                orig.medianFilter(4))
        Assert.assertEquals("too high", ArrayList<Double>(), orig.medianFilter(8))
    }

    @Test fun testMeanFilter() {
        val orig = listOf(6.0, 1.0, 2.0, 3.0, 4.0, 5.0, 0.0)
        Assert.assertEquals("meanFilter2",
                listOf(3.5, 1.5, 2.5, 3.5, 4.5, 2.5),
                orig.meanFilter(2))
        Assert.assertEquals("meanFilter4",
                listOf((6+1+2+3)/4.0, (1+2+3+4)/4.0, (2+3+4+5)/4.0, (3+4+5+0)/4.0),
                orig.meanFilter(4))
        Assert.assertEquals("too high", ArrayList<Double>(), orig.meanFilter(8))
    }
    @Test fun innerThreshhold() {
        val orig = listOf(6.0, 1.0, 2.0, 3.0, 4.0, 5.0, 0.0)
        Assert.assertEquals("innerThreshhold2",
                listOf(6.0, 0.0, 2.0, 3.0, 4.0, 5.0, 0.0),
                orig.innerThreshhold(2.0))
        Assert.assertEquals("innerThresshold4",
                listOf(6.0, 0.0, 0.0, 0.0, 4.0, 5.0, 0.0),
                orig.innerThreshhold(4.0))
    }


}