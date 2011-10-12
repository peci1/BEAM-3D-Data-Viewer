/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.either;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
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
     * {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet4D#ArrayDataSet4D(org.esa.beam.dataViewer3D.data.point.DataPoint4D[], Integer[], int, int, int, int, int, int, int, int)}
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
            fail("Allowed to create data set with histogram Integerer than test data");
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
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(getTestData(2), getHistogram(2), 1, 1, 1, 1, 0, 0,
                0, 0);
        it = dataSet.iterator();
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());
    }

    @Test
    public void testGetBuilder()
    {
        assertNotNull(ArrayDataSet4D.getBuilder(null));
        assertNotNull(ArrayDataSet4D.getBuilder(1));
        assertNotNull(ArrayDataSet4D.getBuilder(1000000));
        assertNotNull(ArrayDataSet4D.getBuilder(null, 1));
        assertNotNull(ArrayDataSet4D.getBuilder(null, 100));
        assertNotNull(ArrayDataSet4D.getBuilder(1, 1));
        assertNotNull(ArrayDataSet4D.getBuilder(100, 1));
        assertNotNull(ArrayDataSet4D.getBuilder(1, 100));
        assertNotNull(ArrayDataSet4D.getBuilder(100, 100));

        try {
            ArrayDataSet4D.getBuilder(0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(0, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(0, 10);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(10, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(null, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(-1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(-1, 0);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(-1, 1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(-1, -1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(10, -1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet4D.getBuilder(null, -10);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testCreateFromDataSources()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = ArrayDataSet.createFromDataSources((Integer) null,
                getTestDataSourceX(10), getTestDataSourceY(10), getTestDataSourceZ(10), getTestDataSourceW(10));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset minimum returned", (Float) (-11f), dataSet.getMinW());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        assertEquals("Wrong dataset maximum returned", (Float) Float.POSITIVE_INFINITY, dataSet.getMaxW());
        Iterator<Integer> histIt = dataSet.histogramIterator();
        for (int i = 0; i < 10; i++)
            assertEquals("Wrong histogram count reported", (Integer) 1, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources((Integer) null, getTestDataSourceX(12), getTestDataSourceY(12),
                getTestDataSourceZ(12), getTestDataSourceW(12));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset minimum returned", (Float) (-11f), dataSet.getMinW());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        assertEquals("Wrong dataset maximum returned", (Float) Float.POSITIVE_INFINITY, dataSet.getMaxW());

        // sort the result because there is no preset order
        histIt = dataSet.histogramIterator();
        List<Integer> histogramData = new ArrayList<Integer>(10);
        while (histIt.hasNext())
            histogramData.add(histIt.next());
        Collections.sort(histogramData);
        histIt = histogramData.iterator();

        for (int i = 0; i < 8; i++)
            assertEquals("Wrong histogram count reported", (Integer) 1, histIt.next());
        assertEquals("Wrong histogram count reported", (Integer) 2, histIt.next());
        assertEquals("Wrong histogram count reported", (Integer) 2, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources(5, getTestDataSourceX(10), getTestDataSourceY(10),
                getTestDataSourceZ(10), getTestDataSourceW(10));
        assertTrue("Wrong data set size reported", 5 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset minimum returned", -11f <= dataSet.getMinW());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        assertTrue("Wrong dataset maximum returned", Float.POSITIVE_INFINITY >= dataSet.getMaxW());
        histIt = dataSet.histogramIterator();
        for (int i = 0; i < 5; i++)
            assertEquals("Wrong histogram count reported", (Integer) 1, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources(50000, getTestDataSourceX(100000), getTestDataSourceY(100000),
                getTestDataSourceZ(100000), getTestDataSourceW(100000));
        assertTrue("Wrong data set size reported", 50000 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset minimum returned", -11f <= dataSet.getMinW());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        assertTrue("Wrong dataset maximum returned", Float.POSITIVE_INFINITY >= dataSet.getMaxW());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1)).or(is(2)));

        // the test data have 2 equal points for every 12 points
        dataSet = ArrayDataSet.createFromDataSources((Integer) null, getTestDataSourceX(120000),
                getTestDataSourceY(120000), getTestDataSourceZ(120000), getTestDataSourceW(120000));
        assertEquals("Wrong data set size reported", 100000, dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset minimum returned", -11f <= dataSet.getMinW());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        assertTrue("Wrong dataset maximum returned", Float.POSITIVE_INFINITY >= dataSet.getMaxW());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1)).or(is(2)));
    }

    private DataSource<Byte> getTestDataSourceX(final int size)
    {
        final List<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Byte>() {

            @Override
            public Iterator<Byte> iterator()
            {
                return new Iterator<Byte>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Byte next()
                    {
                        return it.next().getX().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Byte>> numericTypeIterator()
            {
                return new Iterator<NumericType<Byte>>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Byte> next()
                    {
                        return it.next().getX();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private DataSource<Integer> getTestDataSourceY(final int size)
    {
        final List<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Integer>() {

            @Override
            public Iterator<Integer> iterator()
            {
                return new Iterator<Integer>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Integer next()
                    {
                        return it.next().getY().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Integer>> numericTypeIterator()
            {
                return new Iterator<NumericType<Integer>>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Integer> next()
                    {
                        return it.next().getY();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private DataSource<Double> getTestDataSourceZ(final int size)
    {
        final List<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Double>() {

            @Override
            public Iterator<Double> iterator()
            {
                return new Iterator<Double>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Double next()
                    {
                        return it.next().getZ().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Double>> numericTypeIterator()
            {
                return new Iterator<NumericType<Double>>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Double> next()
                    {
                        return it.next().getZ();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private DataSource<Float> getTestDataSourceW(final int size)
    {
        final List<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Float>() {

            @Override
            public Iterator<Float> iterator()
            {
                return new Iterator<Float>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Float next()
                    {
                        return it.next().getW().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Float>> numericTypeIterator()
            {
                return new Iterator<NumericType<Float>>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Float> next()
                    {
                        return it.next().getW();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] getTestData(
            int size)
    {
        @SuppressWarnings("unchecked")
        DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] data = (DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[]) new DataPoint4D<?, ?, ?, ?>[size];
        for (int i = 0; i < size; i++) {
            switch ((i + 1) % 12) {
                case 0:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null), new FloatType(0f + i / 12, null));
                    break;
                case 1:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null), new FloatType(6.6f + i / 12, null));
                    break;
                case 2:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null), new FloatType(0f + i / 12, null));
                    break;
                case 3:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MIN_VALUE + i / 12),
                            new DoubleType(Double.MIN_VALUE + i / 12, null), new FloatType(Float.MIN_VALUE + i / 12,
                                    null));
                    break;
                case 4:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MAX_VALUE - i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.MAX_VALUE - i / 12, null), new FloatType(Float.MAX_VALUE - i / 12,
                                    null));
                    break;
                case 5:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.NaN, null), new FloatType(Float.POSITIVE_INFINITY, null));
                    break;
                case 6:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (-5 + i / 12)), new IntType(-6 + i / 12), new DoubleType(-6.5 + i / 12,
                                    null), new FloatType(-6.6f + i / 12, null));
                    break;
                case 7:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(10000000 + i / 12), new DoubleType(
                                    Double.MIN_NORMAL + i / 12, null), new FloatType(Float.MIN_NORMAL + i / 12, null));
                    break;
                case 8:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 20), new FloatType(6.4999999f + i / 12, 20));
                    break;
                case 9:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 1), new FloatType(6.499999999f + i / 12, 1));
                    break;
                case 10:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(4 + i / 12), new DoubleType(-10d + i / 12,
                                    null), new FloatType(-11f + i / 12, null));
                    break;
                case 11:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null), new FloatType(6.6f + i / 12, null));
                    break;
            }
        }
        return data;
    }

    private Integer[] getHistogram(int size)
    {
        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return Arrays.copyOf(new Integer[] { 1, 2, 3, 1, 1000, 1, 2, 3, 4, 1 }, size);
    }

}
