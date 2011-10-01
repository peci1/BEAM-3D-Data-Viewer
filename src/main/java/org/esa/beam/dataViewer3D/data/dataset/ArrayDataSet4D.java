/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * A 4D data set implemented by an array.
 * 
 * @author Martin Pecka
 * 
 * @param <X> Type of the values of the x coordinate.
 * @param <Y> Type of the values of the y coordinate.
 * @param <Z> Type of the values of the z coordinate.
 * @param <W> Type of the values of the w coordinate.
 */
public class ArrayDataSet4D<X extends Number, Y extends Number, Z extends Number, W extends Number> extends
        ArrayDataSet implements DataSet4D<X, Y, Z, W>
{

    /** The array containing all data points. */
    private final DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>[] data;

    /** Indices to data[] where the minimum and maximum values are found. */
    private final int                                                                           minXIndex, minYIndex,
            minZIndex, minWIndex, maxXIndex, maxYIndex, maxZIndex, maxWIndex;

    /**
     * Create a data set with the given data points.
     * 
     * @param data The data points that form the new data set.
     * @param histogram The array containing the counts of data points with the same value in the source (indices
     *            correspond to indices in <code>data</code>).
     * @param minXIndex Indices to data[] where the minimum and maximum values are found.
     * @param minYIndex Indices to data[] where the minimum and maximum values are found.
     * @param minZIndex Indices to data[] where the minimum and maximum values are found.
     * @param minWIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxXIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxYIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxZIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxWIndex Indices to data[] where the minimum and maximum values are found.
     */
    protected ArrayDataSet4D(DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>[] data,
            Long[] histogram, int minXIndex, int minYIndex, int minZIndex, int minWIndex, int maxXIndex, int maxYIndex,
            int maxZIndex, int maxWIndex)
    {
        super(histogram);
        this.data = data;

        this.minXIndex = minXIndex;
        this.minYIndex = minYIndex;
        this.minZIndex = minZIndex;
        this.minWIndex = minWIndex;
        this.maxXIndex = maxXIndex;
        this.maxYIndex = maxYIndex;
        this.maxZIndex = maxZIndex;
        this.maxWIndex = maxWIndex;
    }

    @Override
    public Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>> pointIterator()
    {
        return Arrays.asList(data).iterator();
    }

    @Override
    protected DataPoint[] getData()
    {
        return data;
    }

    /**
     * Return the builder that can be used to build this data set.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * 
     * @return The builder that can be used to build this data set.
     */
    static <X extends Number, Y extends Number, Z extends Number, W extends Number> Builder4D<X, Y, Z, W> getBuilder(
            Long maxPoints)
    {
        return new Builder4D<X, Y, Z, W>(maxPoints, null);
    }

    /**
     * Return the builder that can be used to build this data set.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param expectedCount The expected number of data points.
     * 
     * @return The builder that can be used to build this data set.
     */
    static <X extends Number, Y extends Number, Z extends Number, W extends Number> Builder4D<X, Y, Z, W> getBuilder(
            Long maxPoints, long expectedCount)
    {
        return new Builder4D<X, Y, Z, W>(maxPoints, expectedCount);
    }

    /**
     * A class for easy building of data sets.
     * 
     * @author Martin Pecka
     * 
     * @param <X> Type of the values of the x coordinate.
     * @param <Y> Type of the values of the y coordinate.
     * @param <Z> Type of the values of the z coordinate.
     * @param <W> Type of the values of the w coordinate.
     */
    static class Builder4D<X extends Number, Y extends Number, Z extends Number, W extends Number> extends
            org.esa.beam.dataViewer3D.data.dataset.AbstractDataSet.Builder4D<X, Y, Z, W>
    {
        /**
         * @param maxPoints The maximum number of data points in the resulting set.
         * @param expectedCount The expected number of data points (<code>null</code> if the count cannot be estimated).
         */
        private Builder4D(Long maxPoints, Long expectedCount)
        {
            super(maxPoints, expectedCount);
        }

        /**
         * Return the data set built based on the information stuffed into this builder.
         * <p>
         * If <code>maxPoints</code> isn't <code>null</code> and the set would contain more data points than
         * <code>maxPoints</code>, a random subset of <code>maxPoints</code> points will be returned.
         * 
         * @return The data set built based on the information stuffed into this builder.
         */
        public DataSet4D<X, Y, Z, W> getResult()
        {
            if (inputSize == null && maxPoints != null && data.size() > maxPoints) {
                List<DataPoint> keys = new ArrayList<DataPoint>(data.keySet());
                Collections.shuffle(keys);
                keys.subList(maxPoints.intValue(), keys.size()).clear(); // we can use .intValue() as long as no Long
                                                                         // implementation of data set is provided
                data.keySet().retainAll(keys);
            }

            @SuppressWarnings("unchecked")
            DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>[] arr = (DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>[]) new DataPoint4D<?, ?, ?, ?>[data
                    .size()];

            int i = 0;
            int minXIndex = 0, minYIndex = 0, minZIndex = 0, minWIndex = 0, maxXIndex = 0, maxYIndex = 0, maxZIndex = 0, maxWIndex = 0;
            double minXDouble = Double.MAX_VALUE, minYDouble = Double.MAX_VALUE, minZDouble = Double.MAX_VALUE, minWDouble = Double.MAX_VALUE, maxXDouble = Double.MIN_VALUE, maxYDouble = Double.MIN_VALUE, maxZDouble = Double.MIN_VALUE, maxWDouble = Double.MIN_VALUE;

            for (DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>> point : data.keySet()) {
                double d = point.getX().getNumber().doubleValue();
                if (d < minXDouble) {
                    minXDouble = d;
                    minXIndex = i;
                }
                if (d > maxXDouble) {
                    maxXDouble = d;
                    maxXIndex = i;
                }

                d = point.getY().getNumber().doubleValue();
                if (d < minYDouble) {
                    minYDouble = d;
                    minYIndex = i;
                }
                if (d > maxYDouble) {
                    maxYDouble = d;
                    maxYIndex = i;
                }

                d = point.getZ().getNumber().doubleValue();
                if (d < minZDouble) {
                    minZDouble = d;
                    minZIndex = i;
                }
                if (d > maxZDouble) {
                    maxZDouble = d;
                    maxZIndex = i;
                }

                d = point.getW().getNumber().doubleValue();
                if (d < minWDouble) {
                    minWDouble = d;
                    minWIndex = i;
                }
                if (d > maxWDouble) {
                    maxWDouble = d;
                    maxWIndex = i;
                }

                arr[i++] = point;
            }
            return new ArrayDataSet4D<X, Y, Z, W>(arr, data.values().toArray(new Long[] {}), minXIndex, minYIndex,
                    minZIndex, minWIndex, maxXIndex, maxYIndex, maxZIndex, maxWIndex);
        }
    }

    @Override
    public X getMinX()
    {
        return data[minXIndex].getX().getNumber();
    }

    @Override
    public Y getMinY()
    {
        return data[minYIndex].getY().getNumber();
    }

    @Override
    public Z getMinZ()
    {
        return data[minZIndex].getZ().getNumber();
    }

    @Override
    public W getMinW()
    {
        return data[minWIndex].getW().getNumber();
    }

    @Override
    public X getMaxX()
    {
        return data[maxXIndex].getX().getNumber();
    }

    @Override
    public Y getMaxY()
    {
        return data[maxYIndex].getY().getNumber();
    }

    @Override
    public Z getMaxZ()
    {
        return data[maxZIndex].getZ().getNumber();
    }

    @Override
    public W getMaxW()
    {
        return data[maxWIndex].getW().getNumber();
    }
}
