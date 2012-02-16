/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class LongTypeTest extends NumericTypeTestCommon<Long>
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.LongType#LongType(java.lang.Long)}.
     */
    @Test
    public void testLongType()
    {
        testConstructor(0L, Long.MAX_VALUE, Long.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(false, 5L, 0L, Long.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber(0L, 100L, Long.MIN_VALUE, Long.MAX_VALUE, -10L);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Long[] { 0L, Long.MAX_VALUE }, new Long[] { 0L, 1L, 0L, (long) -1, Long.MIN_VALUE,
                Long.MAX_VALUE }, new Object[] { 0L, null, 0L, "asd", 0L, 0L });
    }

    @Override
    protected NumericType<Long> getInstance(Long value, Integer precision)
    {
        return new LongType(value);
    }

    @Override
    protected Long[] getRandomValues(int count)
    {
        Random rand = new Random();
        Long[] result = new Long[count];
        for (int i = 0; i < count; i++)
            result[i] = rand.nextLong();
        return result;
    }

}
