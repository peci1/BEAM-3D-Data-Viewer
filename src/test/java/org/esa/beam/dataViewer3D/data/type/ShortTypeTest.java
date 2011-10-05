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
public class ShortTypeTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.ShortType#ShortType(java.lang.Short)}.
     */
    @Test
    public void testShortType()
    {
        assertNotNull(new ShortType((short) 0));
        assertNotNull(new ShortType(Short.MAX_VALUE));
        assertNotNull(new ShortType(Short.MIN_VALUE));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new ShortType((short) 5).hashCode(), new ShortType(
                (short) 5).hashCode());
        assertEquals("hashCode() differs for equal values", new ShortType((short) 0).hashCode(), new ShortType(
                (short) 0).hashCode());
        assertEquals("hashCode() differs for equal values", new ShortType(Short.MIN_VALUE).hashCode(), new ShortType(
                Short.MIN_VALUE).hashCode());

        // short is smaller than int, so we can demand that the hash values are different
        assertNotSame("Equal hashcodes for nonequal values found.", new ShortType((short) 5).hashCode(), new ShortType(
                (short) 7).hashCode());
        assertNotSame("Equal hashcodes for nonequal values found.", new ShortType(Short.MIN_VALUE).hashCode(),
                new ShortType(Short.MAX_VALUE).hashCode());

        final int testSize = 255;

        Random rand = new Random();
        short rnd1, rnd2;
        for (int i = 0; i < testSize; i++) {
            rnd1 = (short) ((Short.MAX_VALUE - Short.MIN_VALUE) * rand.nextDouble() + Short.MIN_VALUE);
            rnd2 = (short) ((Short.MAX_VALUE - Short.MIN_VALUE) * rand.nextDouble() + Short.MIN_VALUE);
            if (rnd1 != rnd2)
                assertNotSame("Equal hashcodes for nonequal values found.", new ShortType(rnd1).hashCode(),
                        new ShortType(rnd2).hashCode());
        }
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Short) (short) 0, new ShortType((short) 0).getNumber());
        assertEquals("getNumber() returned bad value", (Short) (short) 100, new ShortType((short) 100).getNumber());
        assertEquals("getNumber() returned bad value", (Short) Short.MIN_VALUE,
                new ShortType(Short.MIN_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Short) Short.MAX_VALUE,
                new ShortType(Short.MAX_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Short) (short) -10, new ShortType((short) -10).getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new ShortType((short) 0), new ShortType((short) 0));
        assertEquals("Equal objects claimed not equal.", new ShortType(Short.MAX_VALUE), new ShortType(Short.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new ShortType((short) 0), new ShortType((short) 1));
        assertNotSame("Unequal objects claimed equal.", new ShortType((short) 0), new ShortType((short) -1));
        assertNotSame("Unequal objects claimed equal.", new ShortType(Short.MIN_VALUE), new ShortType(Short.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new ShortType((short) 0), null);
        assertNotSame("Unequal objects claimed equal.", new ShortType((short) 0), "asd");
        assertNotSame("Unequal objects claimed equal.", new ShortType((short) 0), (short) 0);
    }

}
