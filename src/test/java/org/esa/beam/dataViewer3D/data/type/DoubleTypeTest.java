/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class DoubleTypeTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.type.DoubleType#DoubleType(java.lang.Double, java.lang.Integer)}.
     */
    @Test
    public void testDoubleType()
    {
        assertNotNull(new DoubleType(0d, null));
        assertNotNull(new DoubleType(1.5d, 10));
        assertNotNull(new DoubleType(-1.5d, -10));
        assertNotNull(new DoubleType(Double.MAX_VALUE, 10));
        assertNotNull(new DoubleType(Double.MIN_VALUE, -10));
        assertNotNull(new DoubleType(Double.NaN, 10));
        assertNotNull(new DoubleType(Double.NEGATIVE_INFINITY, 0));
        assertNotNull(new DoubleType(Double.POSITIVE_INFINITY, 1));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new DoubleType(5d, 0).hashCode(),
                new DoubleType(5d, 0).hashCode());
        assertEquals("hashCode() differs for equal values", new DoubleType(0d, 2).hashCode(),
                new DoubleType(0d, 2).hashCode());
        assertEquals("hashCode() differs for equal values", new DoubleType(Double.MIN_VALUE, 10).hashCode(),
                new DoubleType(Double.MIN_VALUE, 10).hashCode());
        assertEquals("hashCode() differs for equal values", new DoubleType(1.5d, 0).hashCode(),
                new DoubleType(1.7d, 0).hashCode());
        assertEquals("hashCode() differs for equal values", new DoubleType(1.333d, 2).hashCode(), new DoubleType(
                1.334d, 2).hashCode());
        assertEquals("hashCode() differs for equal values", new DoubleType(101d, -1).hashCode(), new DoubleType(102d,
                -1).hashCode());

        assertNotSame("Equal hashcodes for nonequal values found.", new DoubleType(5d, 2).hashCode(), new DoubleType(
                7d, 2).hashCode());
        assertNotSame("Equal hashcodes for nonequal values found.", new DoubleType(Double.MIN_VALUE, 2).hashCode(),
                new DoubleType(Double.MAX_VALUE, 2).hashCode());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new DoubleType((double) 0, 2), new DoubleType((double) 0, 2));
        assertEquals("Equal objects claimed not equal.", new DoubleType(Double.MAX_VALUE, null), new DoubleType(
                Double.MAX_VALUE, null));

        assertEquals("Equal objects claimed not equal.", new DoubleType(1.5d, 0), new DoubleType(1.7d, 0));
        assertEquals("Equal objects claimed not equal.", new DoubleType(1.333d, 2), new DoubleType(1.334d, 2));
        assertEquals("Equal objects claimed not equal.", new DoubleType(101d, -1), new DoubleType(102d, -1));

        assertNotSame("Unequal objects claimed equal.", new DoubleType((double) 0, 2), new DoubleType((double) 1, 2));
        assertNotSame("Unequal objects claimed equal.", new DoubleType((double) 0, -2), new DoubleType((double) -1, -2));
        assertNotSame("Unequal objects claimed equal.", new DoubleType(Double.MIN_VALUE, 0), new DoubleType(
                Double.MAX_VALUE, 0));

        assertNotSame("Unequal objects claimed equal.", new DoubleType(1.5d, 0), new DoubleType(1.4d, 0));
        assertNotSame("Unequal objects claimed equal.", new DoubleType(1.336d, 2), new DoubleType(1.334d, 2));
        assertNotSame("Unequal objects claimed equal.", new DoubleType(94d, -1), new DoubleType(102d, -1));

        assertNotSame("Unequal objects claimed equal.", new DoubleType((double) 0, null), null);
        assertNotSame("Unequal objects claimed equal.", new DoubleType((double) 0, 100), "asd");
        assertNotSame("Unequal objects claimed equal.", new DoubleType((double) 0, 10000), (double) 0);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#getPrecision()}.
     */
    @Test
    public void testGetPrecision()
    {
        assertEquals("getPrecision() returned bad value", (Integer) 2, new DoubleType(0d, 2).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) 0, new DoubleType(100d, 0).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) null,
                new DoubleType(Double.MIN_VALUE, null).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) (-10),
                new DoubleType(Double.MAX_VALUE, -10).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) null, new DoubleType(-10d, null).getPrecision());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Double) 0d, new DoubleType(0d, 2).getNumber());
        assertEquals("getNumber() returned bad value", (Double) 100d, new DoubleType(100d, 0).getNumber());
        assertEquals("getNumber() returned bad value", (Double) Double.MIN_VALUE,
                new DoubleType(Double.MIN_VALUE, null).getNumber());
        assertEquals("getNumber() returned bad value", (Double) Double.MAX_VALUE,
                new DoubleType(Double.MAX_VALUE, -10).getNumber());
        assertEquals("getNumber() returned bad value", (Double) (-10d), new DoubleType(-10d, null).getNumber());
    }

}
