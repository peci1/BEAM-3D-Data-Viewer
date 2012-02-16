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
public class FloatTypeTest extends DecimalTypeTestCommon<Float>
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.type.FloatType#FloatType(java.lang.Float, java.lang.Integer)}.
     */
    @Test
    public void testFloatType()
    {
        testConstructor(new Float[] { 0f, 1.5f, -1.5f, Float.MAX_VALUE, Float.MIN_VALUE, Float.NaN,
                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY }, new Integer[] { null, 10, -10, 10, -10, 10, 0, 1 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(false, new Float[] { 5f, 5f, 0f, 0f, Float.MIN_VALUE, Float.MIN_VALUE, 1.5f, 1.7f, 1.333f, 1.334f,
                101f, 102f }, new Integer[] { 0, 0, 2, 2, 10, 10, 0, 0, 2, 2, -1, -1 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Float[] { (float) 0, (float) 0, Float.MAX_VALUE, Float.MAX_VALUE, 1.5f, 1.7f, 1.333f,
                1.334f, 101f, 102f }, new Integer[] { 2, 2, null, null, 0, 0, 2, 2, -1, -1, }, new Float[] { (float) 0,
                (float) 1, (float) 0, (float) -1, Float.MIN_VALUE, Float.MAX_VALUE, 1.5f, 1.4f, 1.336f, 1.334f, 94f,
                102f }, new Integer[] { 2, 2, -2, -2, 0, 0, 0, 0, 2, 2, -1, -1, }, new Object[] { (float) 0, null,
                (float) 0, "asd", (float) 0, (float) 0 }, new Integer[] { null, 100, 10000 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#getPrecision()}.
     */
    @Test
    public void testGetPrecision()
    {
        testGetPrecision(new Float[] { 0f, 100f, Float.MIN_VALUE, Float.MAX_VALUE, -10f }, new Integer[] { 2, 0, null,
                -10, null });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber(new Float[] { 0f, 100f, Float.MIN_VALUE, Float.MAX_VALUE, -10f }, new Integer[] { 2, 0, null,
                -10, null });
    }

    @Override
    protected DecimalNumericType<Float> getInstance(Float value, Integer precision)
    {
        return new FloatType(value, precision);
    }

    @Override
    protected Float[] getRandomValues(int count)
    {
        Float[] result = new Float[count];
        Random rand = new Random();
        for (int i = 0; i < count; i++)
            result[i] = rand.nextFloat();
        return result;
    }

}
