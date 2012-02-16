/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.esa.beam.dataViewer3D.data.Common;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.LongType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.data.type.ShortType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class SimpleDataPoint4DTest extends SimpleDataPointTestCommon
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#getX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#getY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#getZ()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#getW()}.
     */
    @Test
    public void testGetters()
    {
        testGetters(new ByteType((byte) 5), new ShortType((short) 6), new IntType(7), new LongType(8L));
        testGetters(new ByteType((byte) 0), new ShortType((short) 0), new IntType(0), new LongType(0L));
        testGetters(new ByteType(Byte.MAX_VALUE), new ShortType(Short.MAX_VALUE), new IntType(Integer.MAX_VALUE),
                new LongType(Long.MAX_VALUE));
        testGetters(new ByteType(Byte.MIN_VALUE), new ShortType(Short.MIN_VALUE), new IntType(Integer.MIN_VALUE),
                new LongType(Long.MIN_VALUE));

        testGetters(new FloatType(0f, 10), new FloatType(Float.MIN_VALUE, 10), new FloatType(Float.NaN, 10),
                new FloatType(Float.POSITIVE_INFINITY, 0));
        testGetters(new DoubleType(0d, 10), new DoubleType(Double.NEGATIVE_INFINITY, 10), new DoubleType(
                Double.MAX_VALUE, 10), new DoubleType(Double.POSITIVE_INFINITY, 256));
    }

    private <X extends Number, Y extends Number, Z extends Number, W extends Number> void testGetters(NumericType<X> x,
            NumericType<Y> y, NumericType<Z> z, NumericType<W> w)
    {
        SimpleDataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>> point = new SimpleDataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>(
                x, y, z, w);

        assertEquals("getX() returned bad value", x.getNumber(), point.getX().getNumber());
        assertEquals("getY() returned bad value", y.getNumber(), point.getY().getNumber());
        assertEquals("getZ() returned bad value", z.getNumber(), point.getZ().getNumber());
        assertEquals("getW() returned bad value", w.getNumber(), point.getW().getNumber());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#getDimensions()}.
     */
    @Test
    public void testGetDimensions()
    {
        assertEquals("Invalid number of dimensions returned.", 4,
                new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(new ByteType(
                        (byte) 5), new ShortType((short) 6), new IntType(7), new LongType(8L)).getDimensions());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        // it is important to do this statistical test, because the hashcodes need to be equally distributed
        int rounds = 100000;

        testHashCodeHelper(Common.getTestData4D(rounds));

        Random rand = new Random();
        byte[] bytes = new byte[1];
        DataPoint[] testPoints = new DataPoint[rounds];

        for (int i = 0; i < rounds; i++) {
            rand.nextBytes(bytes);
            testPoints[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                    new ByteType(bytes[0]), new IntType(rand.nextInt()), new DoubleType(rand.nextDouble(),
                            rand.nextInt(1000)), new FloatType(rand.nextFloat(), rand.nextInt(1000)));
        }

        testHashCodeHelper(testPoints);
    }
}
