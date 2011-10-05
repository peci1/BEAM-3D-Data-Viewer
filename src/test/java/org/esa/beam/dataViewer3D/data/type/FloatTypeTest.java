/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class FloatTypeTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.type.FloatType#FloatType(java.lang.Float, java.lang.Integer)}.
     */
    @Test
    public void testFloatType()
    {
        assertNotNull(new FloatType(0f, null));
        assertNotNull(new FloatType(1.5f, 10));
        assertNotNull(new FloatType(-1.5f, -10));
        assertNotNull(new FloatType(Float.MAX_VALUE, 10));
        assertNotNull(new FloatType(Float.MIN_VALUE, -10));
        assertNotNull(new FloatType(Float.NaN, 10));
        assertNotNull(new FloatType(Float.NEGATIVE_INFINITY, 0));
        assertNotNull(new FloatType(Float.POSITIVE_INFINITY, 1));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new FloatType(5f, 0).hashCode(),
                new FloatType(5f, 0).hashCode());
        assertEquals("hashCode() differs for equal values", new FloatType(0f, 2).hashCode(),
                new FloatType(0f, 2).hashCode());
        assertEquals("hashCode() differs for equal values", new FloatType(Float.MIN_VALUE, 10).hashCode(),
                new FloatType(Float.MIN_VALUE, 10).hashCode());
        assertEquals("hashCode() differs for equal values", new FloatType(1.5f, 0).hashCode(),
                new FloatType(1.7f, 0).hashCode());
        assertEquals("hashCode() differs for equal values", new FloatType(1.333f, 2).hashCode(), new FloatType(1.334f,
                2).hashCode());
        assertEquals("hashCode() differs for equal values", new FloatType(101f, -1).hashCode(),
                new FloatType(102f, -1).hashCode());

        assertNotSame("Equal hashcodes for nonequal values found.", new FloatType(5f, 2).hashCode(), new FloatType(7f,
                2).hashCode());
        assertNotSame("Equal hashcodes for nonequal values found.", new FloatType(Float.MIN_VALUE, 2).hashCode(),
                new FloatType(Float.MAX_VALUE, 2).hashCode());

        // the value space of hashCode() of a null-precision FloatType is equal to the int space, so we can demand all
        // the values different
        final float testSize = 4096;

        Random rand = new Random();
        float rnd1, rnd2;
        for (float i = 0; i < testSize; i++) {
            rnd1 = Float.intBitsToFloat(rand.nextInt());
            rnd2 = Float.intBitsToFloat(rand.nextInt());
            if (rnd1 != rnd2)
                assertNotSame("Equal hashcodes for nonequal values found.", new FloatType(rnd1, null).hashCode(),
                        new FloatType(rnd2, null).hashCode());
        }
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new FloatType((float) 0, 2), new FloatType((float) 0, 2));
        assertEquals("Equal objects claimed not equal.", new FloatType(Float.MAX_VALUE, null), new FloatType(
                Float.MAX_VALUE, null));

        assertEquals("Equal objects claimed not equal.", new FloatType(1.5f, 0), new FloatType(1.7f, 0));
        assertEquals("Equal objects claimed not equal.", new FloatType(1.333f, 2), new FloatType(1.334f, 2));
        assertEquals("Equal objects claimed not equal.", new FloatType(101f, -1), new FloatType(102f, -1));

        assertNotSame("Unequal objects claimed equal.", new FloatType((float) 0, 2), new FloatType((float) 1, 2));
        assertNotSame("Unequal objects claimed equal.", new FloatType((float) 0, -2), new FloatType((float) -1, -2));
        assertNotSame("Unequal objects claimed equal.", new FloatType(Float.MIN_VALUE, 0), new FloatType(
                Float.MAX_VALUE, 0));

        assertNotSame("Unequal objects claimed equal.", new FloatType(1.5f, 0), new FloatType(1.4f, 0));
        assertNotSame("Unequal objects claimed equal.", new FloatType(1.336f, 2), new FloatType(1.334f, 2));
        assertNotSame("Unequal objects claimed equal.", new FloatType(94f, -1), new FloatType(102f, -1));

        assertNotSame("Unequal objects claimed equal.", new FloatType((float) 0, null), null);
        assertNotSame("Unequal objects claimed equal.", new FloatType((float) 0, 100), "asd");
        assertNotSame("Unequal objects claimed equal.", new FloatType((float) 0, 10000), (float) 0);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#getPrecision()}.
     */
    @Test
    public void testGetPrecision()
    {
        assertEquals("getPrecision() returned bad value", (Integer) 2, new FloatType(0f, 2).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) 0, new FloatType(100f, 0).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) null,
                new FloatType(Float.MIN_VALUE, null).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) (-10),
                new FloatType(Float.MAX_VALUE, -10).getPrecision());
        assertEquals("getPrecision() returned bad value", (Integer) null, new FloatType(-10f, null).getPrecision());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Float) 0f, new FloatType(0f, 2).getNumber());
        assertEquals("getNumber() returned bad value", (Float) 100f, new FloatType(100f, 0).getNumber());
        assertEquals("getNumber() returned bad value", (Float) Float.MIN_VALUE,
                new FloatType(Float.MIN_VALUE, null).getNumber());
        assertEquals("getNumber() returned bad value", (Float) Float.MAX_VALUE,
                new FloatType(Float.MAX_VALUE, -10).getNumber());
        assertEquals("getNumber() returned bad value", (Float) (-10f), new FloatType(-10f, null).getNumber());
    }

}
