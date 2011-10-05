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
public class ByteTypeTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.ByteType#ByteType(java.lang.Byte)}.
     */
    @Test
    public void testByteType()
    {
        assertNotNull(new ByteType((byte) 0));
        assertNotNull(new ByteType(Byte.MAX_VALUE));
        assertNotNull(new ByteType(Byte.MIN_VALUE));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new ByteType((byte) 5).hashCode(),
                new ByteType((byte) 5).hashCode());
        assertEquals("hashCode() differs for equal values", new ByteType((byte) 0).hashCode(),
                new ByteType((byte) 0).hashCode());
        assertEquals("hashCode() differs for equal values", new ByteType(Byte.MIN_VALUE).hashCode(), new ByteType(
                Byte.MIN_VALUE).hashCode());

        // byte is smaller than int, so we can demand that the hash values are different
        assertNotSame("Equal hashcodes for nonequal values found.", new ByteType((byte) 5).hashCode(), new ByteType(
                (byte) 7).hashCode());
        assertNotSame("Equal hashcodes for nonequal values found.", new ByteType(Byte.MIN_VALUE).hashCode(),
                new ByteType(Byte.MAX_VALUE).hashCode());

        final int testSize = 255;

        Random rand = new Random();
        byte[] rndBytes1 = new byte[testSize];
        byte[] rndBytes2 = new byte[testSize];
        rand.nextBytes(rndBytes1);
        rand.nextBytes(rndBytes2);

        for (int i = 0; i < testSize; i++) {
            if (rndBytes1[i] != rndBytes2[i])
                assertNotSame("Equal hashcodes for nonequal values found.", new ByteType(rndBytes1[i]).hashCode(),
                        new ByteType(rndBytes2[i]).hashCode());
        }
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Byte) (byte) 0, new ByteType((byte) 0).getNumber());
        assertEquals("getNumber() returned bad value", (Byte) (byte) 100, new ByteType((byte) 100).getNumber());
        assertEquals("getNumber() returned bad value", (Byte) Byte.MIN_VALUE, new ByteType(Byte.MIN_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Byte) Byte.MAX_VALUE, new ByteType(Byte.MAX_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Byte) (byte) -10, new ByteType((byte) -10).getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new ByteType((byte) 0), new ByteType((byte) 0));
        assertEquals("Equal objects claimed not equal.", new ByteType(Byte.MAX_VALUE), new ByteType(Byte.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new ByteType((byte) 0), new ByteType((byte) 1));
        assertNotSame("Unequal objects claimed equal.", new ByteType((byte) 0), new ByteType((byte) -1));
        assertNotSame("Unequal objects claimed equal.", new ByteType(Byte.MIN_VALUE), new ByteType(Byte.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new ByteType((byte) 0), null);
        assertNotSame("Unequal objects claimed equal.", new ByteType((byte) 0), "asd");
        assertNotSame("Unequal objects claimed equal.", new ByteType((byte) 0), (byte) 0);
    }

}
