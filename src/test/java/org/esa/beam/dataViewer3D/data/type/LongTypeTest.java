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
public class LongTypeTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.LongType#LongType(java.lang.Long)}.
     */
    @Test
    public void testLongType()
    {
        assertNotNull(new LongType((long) 0));
        assertNotNull(new LongType(Long.MAX_VALUE));
        assertNotNull(new LongType(Long.MIN_VALUE));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        assertEquals("hashCode() differs for equal values", new LongType((long) 5).hashCode(),
                new LongType((long) 5).hashCode());
        assertEquals("hashCode() differs for equal values", new LongType((long) 0).hashCode(),
                new LongType((long) 0).hashCode());
        assertEquals("hashCode() differs for equal values", new LongType(Long.MIN_VALUE).hashCode(), new LongType(
                Long.MIN_VALUE).hashCode());

        // long is larger than int, so we cannot demand that the hash values are different
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        assertEquals("getNumber() returned bad value", (Long) (long) 0, new LongType((long) 0).getNumber());
        assertEquals("getNumber() returned bad value", (Long) (long) 100, new LongType((long) 100).getNumber());
        assertEquals("getNumber() returned bad value", (Long) Long.MIN_VALUE, new LongType(Long.MIN_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Long) Long.MAX_VALUE, new LongType(Long.MAX_VALUE).getNumber());
        assertEquals("getNumber() returned bad value", (Long) (long) -10, new LongType((long) -10).getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        assertEquals("Equal objects claimed not equal.", new LongType((long) 0), new LongType((long) 0));
        assertEquals("Equal objects claimed not equal.", new LongType(Long.MAX_VALUE), new LongType(Long.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new LongType((long) 0), new LongType((long) 1));
        assertNotSame("Unequal objects claimed equal.", new LongType((long) 0), new LongType((long) -1));
        assertNotSame("Unequal objects claimed equal.", new LongType(Long.MIN_VALUE), new LongType(Long.MAX_VALUE));
        assertNotSame("Unequal objects claimed equal.", new LongType((long) 0), null);
        assertNotSame("Unequal objects claimed equal.", new LongType((long) 0), "asd");
        assertNotSame("Unequal objects claimed equal.", new LongType((long) 0), (long) 0);
    }

}
