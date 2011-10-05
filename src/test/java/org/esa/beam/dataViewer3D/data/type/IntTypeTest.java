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
public class IntTypeTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.IntType#IntType(java.lang.Integer)}.
     */
    @Test
    public void testIntType()
    {
        assertNotNull(new IntType(0));
        assertNotNull(new IntType(Integer.MAX_VALUE));
        assertNotNull(new IntType(Integer.MIN_VALUE));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new IntType(5).hashCode(), new IntType(5).hashCode());
        assertEquals("hashCode() differs for equal values", new IntType(0).hashCode(), new IntType(0).hashCode());
        assertEquals("hashCode() differs for equal values", new IntType(Integer.MIN_VALUE).hashCode(), new IntType(
                Integer.MIN_VALUE).hashCode());

        // the value space of hashCode() is equal to the int space, so we can demand all the values different
        assertNotSame("Equal hashcodes for nonequal values found.", new IntType(5).hashCode(),
                new IntType(7).hashCode());
        assertNotSame("Equal hashcodes for nonequal values found.", new IntType(Integer.MIN_VALUE).hashCode(),
                new IntType(Integer.MAX_VALUE).hashCode());

        final int testSize = 4096;

        Random rand = new Random();
        int rnd1, rnd2;
        for (int i = 0; i < testSize; i++) {
            rnd1 = rand.nextInt();
            rnd2 = rand.nextInt();
            if (rnd1 != rnd2)
                assertNotSame("Equal hashcodes for nonequal values found.", new IntType(rnd1).hashCode(), new IntType(
                        rnd2).hashCode());
        }
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Integer) 0, new IntType(0).getNumber());
        assertEquals("getNumber() returned bad value", (Integer) 100, new IntType(100).getNumber());
        assertEquals("getNumber() returned bad value", (Integer) Integer.MIN_VALUE,
                new IntType(Integer.MIN_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Integer) Integer.MAX_VALUE,
                new IntType(Integer.MAX_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Integer) (-10), new IntType(-10).getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new IntType(0), new IntType(0));
        assertEquals("Equal objects claimed not equal.", new IntType(Integer.MAX_VALUE), new IntType(Integer.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new IntType(0), new IntType(1));
        assertNotSame("Unequal objects claimed equal.", new IntType(0), new IntType(-1));
        assertNotSame("Unequal objects claimed equal.", new IntType(Integer.MIN_VALUE), new IntType(Integer.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new IntType(0), null);
        assertNotSame("Unequal objects claimed equal.", new IntType(0), "asd");
        assertNotSame("Unequal objects claimed equal.", new IntType(0), 0);
    }

}
