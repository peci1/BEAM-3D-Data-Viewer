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

import org.esa.beam.dataViewer3D.data.Common;
import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
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
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(10), getHistogram(10), 2,
                2, 2, 2, 3, 3, 3, 4));
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(1), getHistogram(1), 0, 0,
                0, 0, 0, 0, 0, 0));
        assertNotNull(new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1,
                1, 1, 0, 0, 0, 0));

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 0, 0, 0, 0, 1,
                    1, 1, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1, 0, 0,
                    0, 0, 1);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(1), 1, 1, 1, 1, 0,
                    0, 0, 0);
            fail("Allowed to create data set with histogram shorter than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(1), getHistogram(2), 1, 1, 1, 1, 0,
                    0, 0, 0);
            fail("Allowed to create data set with histogram Integerer than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(0), getHistogram(0), 1, 1, 1, 1, 0,
                    0, 0, 0);
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
                Common.getTestData4D(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) Double.MIN_VALUE, dataSet.getMinZ());
        assertEquals("Wrong dataset minimum returned", (Float) Float.MIN_VALUE, dataSet.getMinW());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        assertEquals("Wrong dataset maximum returned", (Float) Float.POSITIVE_INFINITY, dataSet.getMaxW());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1,
                1, 0, 0, 0, 0);
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
        assertEquals("Wrong dataset size returned", 10,
                new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(10), getHistogram(10), 2, 2, 2,
                        2, 3, 3, 3, 4).size());
        assertEquals("Wrong dataset size returned", 1,
                new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(1), getHistogram(1), 0, 0, 0, 0,
                        0, 0, 0, 0).size());
        assertEquals("Wrong dataset size returned", 2,
                new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1, 1,
                        0, 0, 0, 0).size());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.ArrayDataSet#iterator()}.
     */
    @Test
    public void testIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(
                Common.getTestData4D(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        int i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1,
                1, 0, 0, 0, 0);
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
                Common.getTestData4D(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1,
                1, 0, 0, 0, 0);
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
                Common.getTestData4D(10), getHistogram(10), 2, 2, 2, 2, 3, 3, 3, 4);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0L);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new ArrayDataSet4D<Byte, Integer, Double, Float>(Common.getTestData4D(2), getHistogram(2), 1, 1, 1,
                1, 0, 0, 0, 0);
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
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10));
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

        dataSet = ArrayDataSet.createFromDataSources((Integer) null, Common.getTestDataSourceX(12),
                Common.getTestDataSourceY(12), Common.getTestDataSourceZ(12), Common.getTestDataSourceW(12));
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

        dataSet = ArrayDataSet.createFromDataSources(5, Common.getTestDataSourceX(10), Common.getTestDataSourceY(10),
                Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10));
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

        dataSet = ArrayDataSet
                .createFromDataSources(50000, Common.getTestDataSourceX(100000), Common.getTestDataSourceY(100000),
                        Common.getTestDataSourceZ(100000), Common.getTestDataSourceW(100000));
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
        dataSet = ArrayDataSet
                .createFromDataSources((Integer) null, Common.getTestDataSourceX(120000),
                        Common.getTestDataSourceY(120000), Common.getTestDataSourceZ(120000),
                        Common.getTestDataSourceW(120000));
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

    private Integer[] getHistogram(int size)
    {
        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return Arrays.copyOf(new Integer[] { 1, 2, 3, 1, 1000, 1, 2, 3, 4, 1 }, size);
    }

}
