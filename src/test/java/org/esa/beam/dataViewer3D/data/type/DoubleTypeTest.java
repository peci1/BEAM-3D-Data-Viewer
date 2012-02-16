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
public class DoubleTypeTest extends DecimalTypeTestCommon<Double>
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.type.DoubleType#DoubleType(java.lang.Double, java.lang.Integer)}.
     */
    @Test
    public void testDoubleType()
    {
        testConstructor(new Double[] { 0d, 1.5d, -1.5d, Double.MAX_VALUE, Double.MIN_VALUE, Double.NaN,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY },
                new Integer[] { null, 10, -10, 10, -10, 10, 0, 1 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(false, new Double[] { 5d, 5d, 0d, 0d, Double.MIN_VALUE, Double.MIN_VALUE, 1.5d, 1.7d, 1.333d,
                1.334d, 101d, 102d }, new Integer[] { 0, 0, 2, 2, 10, 10, 0, 0, 2, 2, -1, -1 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Double[] { (double) 0, (double) 0, Double.MAX_VALUE, Double.MAX_VALUE, 1.5d, 1.7d, 1.333d,
                1.334d, 101d, 102d }, new Integer[] { 2, 2, null, null, 0, 0, 2, 2, -1, -1, }, new Double[] {
                (double) 0, (double) 1, (double) 0, (double) -1, Double.MIN_VALUE, Double.MAX_VALUE, 1.5d, 1.4d,
                1.336d, 1.334d, 94d, 102d }, new Integer[] { 2, 2, -2, -2, 0, 0, 0, 0, 2, 2, -1, -1, }, new Object[] {
                (double) 0, null, (double) 0, "asd", (double) 0, (double) 0 }, new Integer[] { null, 100, 10000 });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.DecimalNumericType#getPrecision()}.
     */
    @Test
    public void testGetPrecision()
    {
        testGetPrecision(new Double[] { 0d, 100d, Double.MIN_VALUE, Double.MAX_VALUE, -10d }, new Integer[] { 2, 0,
                null, -10, null });
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber(new Double[] { 0d, 100d, Double.MIN_VALUE, Double.MAX_VALUE, -10d }, new Integer[] { 2, 0, null,
                -10, null });
    }

    @Override
    protected DecimalNumericType<Double> getInstance(Double value, Integer precision)
    {
        return new DoubleType(value, precision);
    }

    @Override
    protected Double[] getRandomValues(int count)
    {
        Double[] result = new Double[count];
        Random rand = new Random();
        for (int i = 0; i < count; i++)
            result[i] = rand.nextDouble();
        return result;
    }

}
