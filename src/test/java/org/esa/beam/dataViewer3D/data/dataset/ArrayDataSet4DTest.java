/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ArrayDataSet4DTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#ArrayDataSet4D(org.esa.beam.dataViewer3D.data.point.DataPoint4D[], Long[], int, int, int, int, int, int, int, int)}
     * .
     */
    @Test
    public void testArrayDataSet4D()
    {
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(10), getHistogram(10), 2, 2, 2, 2,
                3, 3, 3, 4));
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(1), getHistogram(1), 0, 0, 0, 0, 0,
                0, 0, 0));
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0,
                0, 0, 0));

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 0, 0, 0, 0, 1, 1, 1, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0, 0, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(1), 1, 1, 1, 1, 0, 0, 0, 0);
            fail("Allowed to create data set with histogram shorter than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(1), getHistogram(2), 1, 1, 1, 1, 0, 0, 0, 0);
            fail("Allowed to create data set with histogram shorter than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(0), getHistogram(0), 1, 1, 1, 1, 0, 0, 0, 0);
            fail("Allowed to create data set of zero size");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMinX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMinY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMinZ()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMaxX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMaxY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#getMaxZ()}.
     */
    @Test
    public void testGetters()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(
                getTestData(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) Double.MIN_VALUE, dataSet.getMinZ());
        assertEquals("Wrong dataset minimum returned", (Float) Float.MIN_VALUE, dataSet.getMinW());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        assertEquals("Wrong dataset maximum returned", (Float) Float.POSITIVE_INFINITY, dataSet.getMaxW());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0, 0,
                0, 0);
        assertEquals("Wrong dataset minimum returned", (Byte) (byte) 0, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) 0, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) 0d, dataSet.getMinZ());
        assertEquals("Wrong dataset minimum returned", (Float) 0f, dataSet.getMinW());
        assertEquals("Wrong dataset maximum returned", (Byte) (byte) 5, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) 6, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) 6.5, dataSet.getMaxZ());
        assertEquals("Wrong dataset maximum returned", (Float) 6.6f, dataSet.getMaxW());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#size()}.
     */
    @Test
    public void testSize()
    {
        assertEquals("Wrong dataset size returned", 10, new ArrayDataSet4D<Byte, Integer, Double, Float>(
                getTestData(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4).size());
        assertEquals("Wrong dataset size returned", 1, new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(1),
                getHistogram(1), 0, 0, 0, 0, 0, 0, 0, 0).size());
        assertEquals("Wrong dataset size returned", 2, new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2),
                getHistogram(2), 1, 1, 1, 1, 0, 0, 0, 0).size());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#iterator()}.
     */
    @Test
    public void testIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(
                getTestData(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        int i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0, 0,
                0, 0);
        i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#pointIterator()}.
     */
    @Test
    public void testPointIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(
                getTestData(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0, 0,
                0, 0);
        it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#histogramIterator()}.
     */
    @Test
    public void testHistogramIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(
                getTestData(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<Long> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Long value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0, 0,
                0, 0);
        it = dataSet.iterator();
        for (Iterator<Long> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Long value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());
    }

    private DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] getTestData(
            int size)
    {
        if (size > 10 || size < 0)
            throw new IllegalArgumentException();

        @SuppressWarnings("unchecked")
        DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] data = (DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[]) new DataPoint4D<?, ?, ?, ?>[size];
        if (size == 0)
            return data;
        data[0] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 5), new IntType(6), new DoubleType(6.5, null), new FloatType(6.6f, null));
        if (size == 1)
            return data;
        data[1] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 0), new IntType(0), new DoubleType(0d, null), new FloatType(0f, null));
        if (size == 2)
            return data;
        data[2] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType(Byte.MIN_VALUE), new IntType(Integer.MIN_VALUE), new DoubleType(Double.MIN_VALUE, null),
                new FloatType(Float.MIN_VALUE, null));
        if (size == 3)
            return data;
        data[3] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType(Byte.MAX_VALUE), new IntType(Integer.MAX_VALUE), new DoubleType(Double.MAX_VALUE, null),
                new FloatType(Float.MAX_VALUE, null));
        if (size == 4)
            return data;
        data[4] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType(Byte.MIN_VALUE), new IntType(Integer.MAX_VALUE), new DoubleType(Double.NaN, null),
                new FloatType(Float.POSITIVE_INFINITY, null));
        if (size == 5)
            return data;
        data[5] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) -5), new IntType(-6), new DoubleType(-6.5, null), new FloatType(-6.6f, null));
        if (size == 6)
            return data;
        data[6] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 1), new IntType(10000000), new DoubleType(Double.MIN_NORMAL, null), new FloatType(
                        Float.MIN_NORMAL, null));
        if (size == 7)
            return data;
        data[7] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 2), new IntType(3), new DoubleType(3.99999999999, 20),
                new FloatType(6.4999999f, 20));
        if (size == 8)
            return data;
        data[8] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 2), new IntType(3), new DoubleType(3.99999999999, 1),
                new FloatType(6.499999999f, 1));
        if (size == 9)
            return data;
        data[9] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                new ByteType((byte) 1), new IntType(4), new DoubleType(-10d, null), new FloatType(-11f, null));
        return data;
    }

    private Long[] getHistogram(int size)
    {
        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return Arrays.copyOf(new Long[] { 1L, 2L, 3L, 1L, 1000L, 1L, 2L, 3L, 4L, 1L }, size);
    }

}
