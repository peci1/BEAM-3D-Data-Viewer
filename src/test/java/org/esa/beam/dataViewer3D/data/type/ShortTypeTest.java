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
public class ShortTypeTest extends NumericTypeTestCommon<Short>
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.ShortType#ShortType(java.lang.Short)}.
     */
    @Test
    public void testShortType()
    {
        testConstructor((short) 0, Short.MAX_VALUE, Short.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(true, (short) 5, (short) 0, Short.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber((short) 0, (short) 100, Short.MIN_VALUE, Short.MAX_VALUE, (short) -10);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Short[] { (short) 0, Short.MAX_VALUE }, new Short[] { (short) 0, (short) 1, (short) 0,
                (short) -1, Short.MIN_VALUE, Short.MAX_VALUE }, new Object[] { (short) 0, null, (short) 0, "asd",
                (short) 0, (short) 0 });
    }

    @Override
    protected NumericType<Short> getInstance(Short value, Integer precision)
    {
        return new ShortType(value);
    }

    @Override
    protected Short[] getRandomValues(int count)
    {
        Random rand = new Random();
        Short[] result = new Short[count];
        for (int i = 0; i < count; i++)
            result[i] = (short) rand.nextInt();
        return result;
    }

}
