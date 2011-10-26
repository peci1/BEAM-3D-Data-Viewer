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
import java.util.LinkedHashMap;
import java.util.List;

import org.esa.beam.dataViewer3D.data.Common;
import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class StreamDataSet4DTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#StreamDataSet4D(DataSource, DataSource, DataSource, DataSource, org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D.PointFactory, LinkedHashMap, List, Number, Number, Number, Number, Number, Number, Number, Number)}
     * .
     */
    @Test
    public void testStreamDataSet4D()
    {
        assertNotNull(new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(10),
                Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE, Byte.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY));
        assertNotNull(new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(1),
                Common.getTestDataSourceY(1), Common.getTestDataSourceZ(1), Common.getTestDataSourceW(1),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(1),
                getUsedPointsIndices(1), (byte) 5, 6, 6.5, 6.6f, (byte) 5, 6, 6.5, 6.6f));
        assertNotNull(new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f));

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                    Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                    getUsedPointsIndices(2), (byte) 5, 6, 6.5, 6.6f, (byte) 0, 0, 0d, 0f);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                    Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                    getUsedPointsIndices(2), (byte) 0, 0, 6.5, 6.6f, (byte) 5, 6, 0d, 6.6f);
            fail("Allowed to create data set with max < min");
        } catch (IllegalArgumentException e) {}

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(1),
                    Common.getTestDataSourceY(1), Common.getTestDataSourceZ(1), Common.getTestDataSourceW(1),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                    getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
            fail("Allowed to create data set with histogram longer than test data");
        } catch (IllegalArgumentException e) {}

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(0),
                    Common.getTestDataSourceY(0), Common.getTestDataSourceZ(0), Common.getTestDataSourceW(0),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(0),
                    getUsedPointsIndices(0), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
            fail("Allowed to create data set of zero size");
        } catch (IllegalArgumentException e) {}

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                    Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(0),
                    getUsedPointsIndices(0), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
            fail("Allowed to create data set of zero size");
        } catch (IllegalArgumentException e) {}

        try {
            new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(1),
                    Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                    StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                    getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
            fail("Allowed to create data set from sources with different sizes");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMinX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMinY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMinZ()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMaxX()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMaxY()}.
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#getMaxZ()}.
     */
    @Test
    public void testGetters()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE, Byte.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY);
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        assertEquals("Wrong dataset minimum returned", (Byte) (byte) 0, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) 0, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) 0d, dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) (byte) 5, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) 6, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) 6.5, dataSet.getMaxZ());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet#size()}.
     */
    @Test
    public void testSize()
    {
        assertEquals(
                "Wrong dataset size returned",
                10,
                new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(10), Common
                        .getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10),
                        StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                        getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE,
                        Byte.MAX_VALUE, Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY).size());
        assertEquals(
                "Wrong dataset size returned",
                1,
                new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(1), Common
                        .getTestDataSourceY(1), Common.getTestDataSourceZ(1), Common.getTestDataSourceW(1),
                        StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(1),
                        getUsedPointsIndices(1), (byte) 5, 6, 6.5, 6.6f, (byte) 5, 6, 6.5, 6.6f).size());
        assertEquals(
                "Wrong dataset size returned",
                2,
                new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2), Common
                        .getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                        StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                        getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f).size());
        assertEquals(
                "Wrong dataset size returned",
                1,
                new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2), Common
                        .getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                        StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(1),
                        getUsedPointsIndices(1), (byte) 5, 6, 6.5, 6.6f, (byte) 5, 6, 6.5, 6.6f).size());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet#iterator()}.
     */
    @Test
    public void testIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE, Byte.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY);
        int i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(10),
                Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        i = 0;
        for (DataPoint p : dataSet) {
            i++;
            assertNotNull("null returned by dataset iterator", p);
        }
        assertEquals("Iterator returned different number of items than size()", dataSet.size(), i);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet4D#pointIterator()}.
     */
    @Test
    public void testPointIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE, Byte.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(10),
                Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        it = dataSet.iterator();
        for (Iterator<? extends DataPoint4D<?, ?, ?, ?>> pointIt = dataSet.pointIterator(); pointIt.hasNext();) {
            assertTrue("Point iterator returns more elements than the normal one", it.hasNext());
            assertEquals("Point iterator returned different point than the normal iterator", it.next(), pointIt.next());
        }
        assertFalse("Point iterator returned less items than the normal one", it.hasNext());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.dataset.StreamDataSet#histogramIterator()}.
     */
    @Test
    public void testHistogramIterator()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(10),
                getUsedPointsIndices(10), Byte.MIN_VALUE, Integer.MIN_VALUE, -10d, Float.MIN_VALUE, Byte.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE, Float.POSITIVE_INFINITY);
        Iterator<DataPoint> it = dataSet.iterator();
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(2),
                Common.getTestDataSourceY(2), Common.getTestDataSourceZ(2), Common.getTestDataSourceW(2),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        it = dataSet.iterator();
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());

        dataSet = new StreamDataSet4D<Byte, Integer, Double, Float>(Common.getTestDataSourceX(10),
                Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10),
                StreamDataSet4D.<Byte, Integer, Double, Float>createDefaultPointFactory(), getUsedPoints(2),
                getUsedPointsIndices(2), (byte) 0, 0, 0d, 0f, (byte) 5, 6, 6.5, 6.6f);
        it = dataSet.iterator();
        for (Iterator<Integer> histIt = dataSet.histogramIterator(); histIt.hasNext();) {
            assertTrue("Histogram iterator returns more elements than the normal one", it.hasNext());
            Integer value = histIt.next();
            it.next();
            assertNotNull("Histogram iterator returned null", value);
            assertTrue("Histogram iterator returned non-positive value", value > 0);
        }
        assertFalse("Histogram iterator returned less items than the normal one", it.hasNext());
    }

    @Test
    public void testCreateFromDataSources()
    {
        DataSet4D<Byte, Integer, Double, Float> dataSet = StreamDataSet4D.createFromDataSources(null,
                Common.getTestDataSourceX(10), Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10),
                Common.getTestDataSourceW(10));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());
        Iterator<Integer> histIt = dataSet.histogramIterator();
        for (int i = 0; i < 10; i++)
            assertEquals("Wrong histogram count reported", (Integer) 1, histIt.next());

        dataSet = StreamDataSet4D.createFromDataSources(null, Common.getTestDataSourceX(12),
                Common.getTestDataSourceY(12), Common.getTestDataSourceZ(12), Common.getTestDataSourceW(12));
        assertEquals("Wrong data set size reported", 10, dataSet.size());
        assertEquals("Wrong dataset minimum returned", (Byte) Byte.MIN_VALUE, dataSet.getMinX());
        assertEquals("Wrong dataset minimum returned", (Integer) Integer.MIN_VALUE, dataSet.getMinY());
        assertEquals("Wrong dataset minimum returned", (Double) (-10d), dataSet.getMinZ());
        assertEquals("Wrong dataset maximum returned", (Byte) Byte.MAX_VALUE, dataSet.getMaxX());
        assertEquals("Wrong dataset maximum returned", (Integer) Integer.MAX_VALUE, dataSet.getMaxY());
        assertEquals("Wrong dataset maximum returned", (Double) Double.MAX_VALUE, dataSet.getMaxZ());

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

        dataSet = StreamDataSet4D.createFromDataSources(5, Common.getTestDataSourceX(10),
                Common.getTestDataSourceY(10), Common.getTestDataSourceZ(10), Common.getTestDataSourceW(10));
        assertTrue("Wrong data set size reported", 5 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        for (int i = 0; i < 5; i++)
            assertEquals("Wrong histogram count reported", (Integer) 1, histIt.next());

        dataSet = StreamDataSet4D
                .createFromDataSources(50000, Common.getTestDataSourceX(100000), Common.getTestDataSourceY(100000),
                        Common.getTestDataSourceZ(100000), Common.getTestDataSourceW(100000));
        assertTrue("Wrong data set size reported", 50000 >= dataSet.size());
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1)).or(is(2)).or(is(3)).or(is(4)));

        // the test data have 2 equal points for every 12 points
        dataSet = StreamDataSet4D
                .createFromDataSources(null, Common.getTestDataSourceX(120000), Common.getTestDataSourceY(120000),
                        Common.getTestDataSourceZ(120000), Common.getTestDataSourceW(120000));
        // we have to take into account the hash collisions, which are allowed to be 5%
        assertTrue("Wrong data set size reported", dataSet.size() > 100000 * 0.95 && dataSet.size() <= 100000);
        assertTrue("Wrong dataset minimum returned", Byte.MIN_VALUE <= dataSet.getMinX());
        assertTrue("Wrong dataset minimum returned", Integer.MIN_VALUE <= dataSet.getMinY());
        assertTrue("Wrong dataset minimum returned", -10d <= dataSet.getMinZ());
        assertTrue("Wrong dataset maximum returned", Byte.MAX_VALUE >= dataSet.getMaxX());
        assertTrue("Wrong dataset maximum returned", Integer.MAX_VALUE >= dataSet.getMaxY());
        assertTrue("Wrong dataset maximum returned", Double.MAX_VALUE >= dataSet.getMaxZ());
        histIt = dataSet.histogramIterator();
        while (histIt.hasNext())
            assertThat("Wrong histogram count reported", histIt.next(), either(is(1)).or(is(2)).or(is(3)).or(is(4)));
    }

    private List<Integer> getUsedPointsIndices(final int size)
    {
        if (size == 0)
            return Arrays.asList(new Integer[0]);

        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }).subList(0, size);
    }

    private LinkedHashMap<Integer, Integer> getUsedPoints(final int size)
    {
        if (size == 0)
            return new LinkedHashMap<Integer, Integer>(1);

        if (size > 10 || size < 1)
            throw new IllegalArgumentException();

        return new LinkedHashMap<Integer, Integer>() {
            private static final long serialVersionUID = 1L;
            {
                Iterator<NumericType<Byte>> xIt = Common.getTestDataSourceX(size).numericTypeIterator();
                Iterator<NumericType<Integer>> yIt = Common.getTestDataSourceY(size).numericTypeIterator();
                Iterator<NumericType<Double>> zIt = Common.getTestDataSourceZ(size).numericTypeIterator();
                Iterator<NumericType<Float>> wIt = Common.getTestDataSourceW(size).numericTypeIterator();

                put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(xIt.next(),
                        yIt.next(), zIt.next(), wIt.next()).hashCode(), 2);
                if (size > 1) {
                    put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                            xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 2);
                    if (size > 2) {
                        put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 3);
                        if (size > 3) {
                            put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                    xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 1);
                            if (size > 4) {
                                put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                        xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 1000);
                                if (size > 5) {
                                    put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                            xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 1);
                                    if (size > 6) {
                                        put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                                xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 2);
                                        if (size > 7) {
                                            put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                                    xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 3);
                                            if (size > 8) {
                                                put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                                        xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(), 4);
                                                if (size > 9) {
                                                    put(new SimpleDataPoint4D<NumericType<?>, NumericType<?>, NumericType<?>, NumericType<?>>(
                                                            xIt.next(), yIt.next(), zIt.next(), wIt.next()).hashCode(),
                                                            1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

}
