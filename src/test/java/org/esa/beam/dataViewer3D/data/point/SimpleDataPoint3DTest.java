/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import static org.junit.Assert.assertEquals;

import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.data.type.ShortType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class SimpleDataPoint3DTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D#getX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D#getY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D#getZ()}.
     */
    @Test
    public void testGetters()
    {
        testGetters(new ByteType((byte) 5), new ShortType((short) 6), new IntType(7));
        testGetters(new ByteType((byte) 0), new ShortType((short) 0), new IntType(0));
        testGetters(new ByteType(Byte.MAX_VALUE), new ShortType(Short.MAX_VALUE), new IntType(Integer.MAX_VALUE));
        testGetters(new ByteType(Byte.MIN_VALUE), new ShortType(Short.MIN_VALUE), new IntType(Integer.MIN_VALUE));

        testGetters(new FloatType(0f, 10), new FloatType(Float.MIN_VALUE, 10), new FloatType(Float.NaN, 10));
        testGetters(new DoubleType(0d, 10), new DoubleType(Double.NEGATIVE_INFINITY, 10), new DoubleType(
                Double.MAX_VALUE, 10));
    }

    private <X extends Number, Y extends Number, Z extends Number> void testGetters(NumericType<X> x, NumericType<Y> y,
            NumericType<Z> z)
    {
        SimpleDataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>> point = new SimpleDataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>(
                x, y, z);

        assertEquals("getX() returned bad value", x.getNumber(), point.getX().getNumber());
        assertEquals("getY() returned bad value", y.getNumber(), point.getY().getNumber());
        assertEquals("getZ() returned bad value", z.getNumber(), point.getZ().getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D#getDimensions()}.
     */
    @Test
    public void testGetDimensions()
    {
        assertEquals("Invalid number of dimensions returned.", 3,
                new SimpleDataPoint3D<NumericType<?>, NumericType<?>, NumericType<?>>(new ByteType((byte) 5),
                        new ShortType((short) 6), new IntType(7)).getDimensions());
    }

}
