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
public class IntTypeTest extends NumericTypeTestCommon<Integer>
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.IntType#IntType(java.lang.Integer)}.
     */
    @Test
    public void testIntegerType()
    {
        testConstructor(0, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(true, 5, 0, Integer.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber(0, 100, Integer.MIN_VALUE, Integer.MAX_VALUE, -10);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Integer[] { (int) 0, Integer.MAX_VALUE }, new Integer[] { (int) 0, (int) 1, (int) 0,
                (int) -1, Integer.MIN_VALUE, Integer.MAX_VALUE }, new Object[] { (int) 0, null, (int) 0, "asd",
                (int) 0, (int) 0 });
    }

    @Override
    protected NumericType<Integer> getInstance(Integer value, Integer precision)
    {
        return new IntType(value);
    }

    @Override
    protected Integer[] getRandomValues(int count)
    {
        Random rand = new Random();
        Integer[] result = new Integer[count];
        for (int i = 0; i < count; i++)
            result[i] = rand.nextInt();
        return result;
    }

}
