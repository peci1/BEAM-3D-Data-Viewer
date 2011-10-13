/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.esa.beam.dataViewer3D.data.Common;
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

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        // it is important to do this statistical test, because the hashcodes need to be equally distributed
        int rounds = 100000;
        LinkedHashMap<Integer, List<DataPoint>> sameHashes = new LinkedHashMap<Integer, List<DataPoint>>();
        int hash;

        for (DataPoint point : Common.getTestData3D(rounds)) {
            hash = point.hashCode();
            if (sameHashes.get(hash) == null) {
                sameHashes.put(hash, new LinkedList<DataPoint>());
            }
            boolean contains = false;
            for (DataPoint p : sameHashes.get(hash)) {
                if (p.equals(point)) {
                    contains = true;
                    break;
                }
            }
            if (!contains)
                sameHashes.get(hash).add(point);
        }

        int i = 0;
        for (Entry<Integer, List<DataPoint>> e : sameHashes.entrySet()) {
            if (e.getValue().size() > 1) {
                i += e.getValue().size();
            }
        }

        if ((double) i / rounds > 0.05)
            fail("More than 5% of different items with colliding hash codes: " + (double) i / rounds * 100 + "%!");

        sameHashes.clear();
        Random rand = new Random();
        byte[] bytes = new byte[1];
        DataPoint point;

        for (i = 0; i < rounds; i++) {
            rand.nextBytes(bytes);
            point = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(new ByteType(
                    bytes[0]), new IntType(rand.nextInt()), new DoubleType(rand.nextDouble(), rand.nextInt(1000)));
            hash = point.hashCode();
            if (sameHashes.get(hash) == null) {
                sameHashes.put(hash, new LinkedList<DataPoint>());
            }
            boolean contains = false;
            for (DataPoint p : sameHashes.get(hash)) {
                if (p.equals(point)) {
                    contains = true;
                    break;
                }
            }
            if (!contains)
                sameHashes.get(hash).add(point);
        }

        i = 0;
        for (Entry<Integer, List<DataPoint>> e : sameHashes.entrySet()) {
            if (e.getValue().size() > 1) {
                i += e.getValue().size();
            }
        }

        if ((double) i / rounds > 0.05)
            fail("More than 5% of different items with colliding hash codes: " + (double) i / rounds * 100 + "%!");
    }
}
