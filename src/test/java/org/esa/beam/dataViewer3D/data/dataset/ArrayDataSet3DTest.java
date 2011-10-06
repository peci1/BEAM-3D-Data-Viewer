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
import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ArrayDataSet3DTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#ArrayDataSet3D(org.esa.beam.dataViewer3D.data.point.DataPoint3D[], Long[], int, int, int, int, int, int)}
     * .
     */
    @Test
    public void testArrayDataSet3D()
    {
        assertNotNull(new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10), getHistogram(10), 2, 2, 2, 3, 3, 3));
        assertNotNull(new ArrayDataSet3D<Byte, Integer, Double>(getTestData(1), getHistogram(1), 0, 0, 0, 0, 0, 0));
        assertNotNull(new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0));

        try {
            new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 0, 0, 0, 1, 1, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 0, 0, 0, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(1), 1, 1, 1, 0, 0, 0);
            fail("Allowed to create data set with histogram shorter than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet3D<Byte, Integer, Double>(getTestData(1), getHistogram(2), 1, 1, 1, 0, 0, 0);
            fail("Allowed to create data set with histogram shorter than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet3D<Byte, Integer, Double>(getTestData(0), getHistogram(0), 1, 1, 1, 0, 0, 0);
            fail("Allowed to create data set of zero size");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMinX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMinY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMinZ()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMaxX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMaxY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#getMaxZ()}.
     */
    @Test
    public void testGetters()
    {
        DataSet3D<Byte, Integer, Double> dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10),
                getHistogram(10), 2, 2, 9, 3, 3, 3);
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());

        dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0);
        assertEquals("Wrong dataset minimum returned", (Byte) (byte) 0, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) 0, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) 0d, dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) (byte) 5, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) 6, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) 6.5, dataSet.getMaxZ());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#size()}.
     */
    @Test
    public void testSize()
    {
        assertEquals("Wrong dataset size returned", 10, new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10),
                getHistogram(10), 2, 2, 9, 3, 3, 3).size());
        assertEquals("Wrong dataset size returned", 1, new ArrayDataSet3D<Byte, Integer, Double>(getTestData(1),
                getHistogram(1), 0, 0, 0, 0, 0, 0).size());
        assertEquals("Wrong dataset size returned", 2, new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2),
                getHistogram(2), 1, 1, 1, 0, 0, 0).size());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#iterator()}.
     */
    @Test
    public void testIterator()
    {
        DataSet3D<Byte, Integer, Double> dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10),
                getHistogram(10), 2, 2, 9, 3, 3, 3);
        int i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);

        dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0);
        i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet3D#pointIterator()}.
     */
    @Test
    public void testPointIterator()
    {
        DataSet3D<Byte, Integer, Double> dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10),
                getHistogram(10), 2, 2, 9, 3, 3, 3);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<? extends DataPoint3D<?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0);
        it = dataSet.iterator();
        for (Iterator<? extends DataPoint3D<?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
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
        DataSet3D<Byte, Integer, Double> dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(10),
                getHistogram(10), 2, 2, 9, 3, 3, 3);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<Long> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Long value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet3D<Byte, Integer, Double>(getTestData(2), getHistogram(2), 1, 1, 1, 0, 0, 0);
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

    @Test
    public void testGetBuilder()
    {
        assertNotNull(ArrayDataSet3D.getBuilder(null));
        assertNotNull(ArrayDataSet3D.getBuilder(1L));
        assertNotNull(ArrayDataSet3D.getBuilder(1000000L));
        assertNotNull(ArrayDataSet3D.getBuilder(null, 1));
        assertNotNull(ArrayDataSet3D.getBuilder(null, 100));
        assertNotNull(ArrayDataSet3D.getBuilder(1L, 1));
        assertNotNull(ArrayDataSet3D.getBuilder(100L, 1));
        assertNotNull(ArrayDataSet3D.getBuilder(1L, 100));
        assertNotNull(ArrayDataSet3D.getBuilder(100L, 100));

        try {
            ArrayDataSet3D.getBuilder(0L);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(0L, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(0L, 10);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(10L, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(null, 0);
            fail("Managed to get a builder for data set of target size 0.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(-1L);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(-1L, 0);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(-1L, 1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(-1L, -1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(10L, -1);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}

        try {
            ArrayDataSet3D.getBuilder(null, -10);
            fail("Managed to get a builder for data set of negative target size.");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testCreateFromDataSources()
    {
        DataSet3D<Byte, Integer, Double> dataSet = ArrayDataSet.createFromDataSources(null, getTestDataSourceX(10),
                getTestDataSourceY(10), getTestDataSourceZ(10));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        Iterator<Long> histIt = dataSet.histogramIterator();
        for (int i = 0; i < 10; i++)
            assertEquals("Wrong histogram count reported", (Long) 1L, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources(null, getTestDataSourceX(12), getTestDataSourceY(12),
                getTestDataSourceZ(12));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());

        // sort the result because there is no preset order
        histIt = dataSet.histogramIterator();
        List<Long> histogramData = new ArrayList<Long>(10);
        while (histIt.hasNext())
            histogramData.add(histIt.next());
        Collections.sort(histogramData);
        histIt = histogramData.iterator();

        for (int i = 0; i < 8; i++)
            assertEquals("Wrong histogram count reported", (Long) 1L, histIt.next());
        assertEquals("Wrong histogram count reported", (Long) 2L, histIt.next());
        assertEquals("Wrong histogram count reported", (Long) 2L, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources(5L, getTestDataSourceX(10), getTestDataSourceY(10),
                getTestDataSourceZ(10));
        assertTrue("Wrong data set size reported", 5 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        for (int i = 0; i < 5; i++)
            assertEquals("Wrong histogram count reported", (Long) 1L, histIt.next());

        dataSet = ArrayDataSet.createFromDataSources(50000L, getTestDataSourceX(100000), getTestDataSourceY(100000),
                getTestDataSourceZ(100000));
        assertTrue("Wrong data set size reported", 50000 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1L)).or(is(2L)));

        //the test data have 2 equal points for every 12 points
        dataSet = ArrayDataSet.createFromDataSources(null, getTestDataSourceX(120000), getTestDataSourceY(120000),
                getTestDataSourceZ(120000));
        assertEquals("Wrong data set size reported", 100000, dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1L)).or(is(2L)));
    }

    private DataSource<Byte> getTestDataSourceX(final int size)
    {
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Byte>() {

            @Override
            public Iterator<Byte> iterator()
            {
                return new Iterator<Byte>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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
            public long size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Byte>> numericTypeIterator()
            {
                return new Iterator<NumericType<Byte>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Integer>() {

            @Override
            public Iterator<Integer> iterator()
            {
                return new Iterator<Integer>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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
            public long size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Integer>> numericTypeIterator()
            {
                return new Iterator<NumericType<Integer>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(getTestData(size));
        return new DataSource<Double>() {

            @Override
            public Iterator<Double> iterator()
            {
                return new Iterator<Double>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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
            public long size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Double>> numericTypeIterator()
            {
                return new Iterator<NumericType<Double>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

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

    private DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[] getTestData(int size)
    {
        @SuppressWarnings("unchecked")
        DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[] data = (DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[]) new DataPoint3D<?, ?, ?>[size];
        for (int i = 0; i < size; i++) {
            switch ((i + 1) % 12) {
                case 0:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null));
                    break;

                case 1:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null));
                    break;

                case 2:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null));
                    break;
                case 3:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MIN_VALUE + i / 12),
                            new DoubleType(Double.MIN_VALUE + i / 12, null));
                    break;
                case 4:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MAX_VALUE - i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.MAX_VALUE - i / 12, null));
                    break;
                case 5:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.NaN, null));
                    break;
                case 6:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (-5 + i / 12)), new IntType(-6 + i / 12), new DoubleType(-6.5 + i / 12,
                                    null));
                    break;
                case 7:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(10000000 + i / 12), new DoubleType(
                                    Double.MIN_NORMAL + i / 12, null));
                    break;
                case 8:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 20));
                    break;
                case 9:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 1));
                    break;
                case 10:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(4 + i / 12), new DoubleType(-10d + i / 12,
                                    null));
                    break;
                case 11:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null));
                    break;

                default:
                    break;
            }
        }
        return data;
    }

    private Long[] getHistogram(int size)
    {
        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return Arrays.copyOf(new Long[] { 2L, 2L, 3L, 1L, 1000L, 1L, 2L, 3L, 4L, 1L }, size);
    }

}
