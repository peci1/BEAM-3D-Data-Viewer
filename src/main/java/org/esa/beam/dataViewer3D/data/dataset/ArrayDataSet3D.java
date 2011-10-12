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
import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * A 3D data set implemented by an array.
 * 
 * @author Martin Pecka
 * 
 * @param <X> Type of the values of the x coordinate.
 * @param <Y> Type of the values of the y coordinate.
 * @param <Z> Type of the values of the z coordinate.
 */
public class ArrayDataSet3D<X extends Number, Y extends Number, Z extends Number> extends ArrayDataSet implements
        DataSet3D<X, Y, Z>
{

    /** The array containing all data points. */
    private final DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>[] data;

    /** Indices to data[] where the minimum and maximum values are found. */
    private final int                                                           minXIndex, minYIndex, minZIndex,
            maxXIndex, maxYIndex, maxZIndex;

    /**
     * Create a data set with the given data points.
     * 
     * @param data The data points that form the new data set.
     * @param histogram The array containing the counts of data points with the same value in the source (indices
     *            correspond to indices
     *            in <code>data</code>).
     * @param minXIndex Indices to data[] where the minimum and maximum values are found.
     * @param minYIndex Indices to data[] where the minimum and maximum values are found.
     * @param minZIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxXIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxYIndex Indices to data[] where the minimum and maximum values are found.
     * @param maxZIndex Indices to data[] where the minimum and maximum values are found.
     */
    protected ArrayDataSet3D(DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>[] data, Integer[] histogram,
            int minXIndex, int minYIndex, int minZIndex, int maxXIndex, int maxYIndex, int maxZIndex)
    {
        super(histogram);

        if (histogram.length != data.length)
            throw new IllegalArgumentException(getClass()
                    + ": The given histogram and data arrays must have equal lengths.");

        if (data.length <= 0)
            throw new IllegalArgumentException(getClass() + ": Cannot create data set of zero length.");

        if (minXIndex < 0 || minYIndex < 0 || minZIndex < 0 || maxXIndex < 0 || maxYIndex < 0 || maxZIndex < 0) {
            throw new IllegalArgumentException(getClass() + "Array index of min/max is < 0.");
        }
        if (minXIndex >= data.length || minYIndex >= data.length || minZIndex >= data.length
                || maxXIndex >= data.length || maxYIndex >= data.length || maxZIndex >= data.length) {
            throw new IllegalArgumentException(getClass() + "Array index of min/max is >= data.length .");
        }

        if (data[minXIndex].getX().getNumber().doubleValue() > data[maxXIndex].getX().getNumber().doubleValue()
                || data[minYIndex].getY().getNumber().doubleValue() > data[maxYIndex].getY().getNumber().doubleValue()
                || data[minZIndex].getZ().getNumber().doubleValue() > data[maxZIndex].getZ().getNumber().doubleValue()) {
            throw new IllegalArgumentException(getClass()
                    + ": The given minimum value is greater than the given maximum.");
        }

        this.data = data;

        this.minXIndex = minXIndex;
        this.minYIndex = minYIndex;
        this.minZIndex = minZIndex;
        this.maxXIndex = maxXIndex;
        this.maxYIndex = maxYIndex;
        this.maxZIndex = maxZIndex;
    }

    @Override
    public Iterator<DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>> pointIterator()
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
    static <X extends Number, Y extends Number, Z extends Number> Builder3D<X, Y, Z> getBuilder(Integer maxPoints)
    {
        return new Builder3D<X, Y, Z>(maxPoints, null);
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
    static <X extends Number, Y extends Number, Z extends Number> Builder3D<X, Y, Z> getBuilder(Integer maxPoints,
            int expectedCount)
    {
        return new Builder3D<X, Y, Z>(maxPoints, expectedCount);
    }

    /**
     * A class for easy building of data sets.
     * 
     * @author Martin Pecka
     * 
     * @param <X> Type of the values of the x coordinate.
     * @param <Y> Type of the values of the y coordinate.
     * @param <Z> Type of the values of the z coordinate.
     */
    static class Builder3D<X extends Number, Y extends Number, Z extends Number> extends
            org.esa.beam.dataViewer3D.data.dataset.AbstractDataSet.Builder3D<X, Y, Z>
    {
        /**
         * @param maxPoints The maximum number of data points in the resulting set.
         * @param inputSize The number of data points this builder will get as input (<code>null</code> if the count
         *            cannot be determined).
         */
        private Builder3D(Integer maxPoints, Integer inputSize)
        {
            super(maxPoints, inputSize);
        }

        /**
         * Return the data set built based on the information stuffed into this builder.
         * <p>
         * If <code>maxPoints</code> isn't <code>null</code> and the set would contain more data points than
         * <code>maxPoints</code>, a random subset of <code>maxPoints</code> points will be returned.
         * 
         * @return The data set built based on the information stuffed into this builder.
         */
        public DataSet3D<X, Y, Z> getResult()
        {
            if (inputSize == null && maxPoints != null && data.size() > maxPoints) {
                List<DataPoint> keys = new ArrayList<DataPoint>(data.keySet());
                Collections.shuffle(keys);
                keys.subList(maxPoints.intValue(), keys.size()).clear(); // we can use .intValue() as long as no Long
                                                                         // implementation of data set is provided
                data.keySet().retainAll(keys);
            }

            @SuppressWarnings("unchecked")
            DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>[] arr = (DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>[]) new DataPoint3D<?, ?, ?>[data
                    .size()];

            int i = 0;
            int minXIndex = 0, minYIndex = 0, minZIndex = 0, maxXIndex = 0, maxYIndex = 0, maxZIndex = 0;
            double minXDouble = Double.MAX_VALUE, minYDouble = Double.MAX_VALUE, minZDouble = Double.MAX_VALUE, maxXDouble = Double.MIN_VALUE, maxYDouble = Double.MIN_VALUE, maxZDouble = Double.MIN_VALUE;

            for (DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>> point : data.keySet()) {

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

                arr[i++] = point;
            }

            return new ArrayDataSet3D<X, Y, Z>(arr, data.values().toArray(new Integer[] {}), minXIndex, minYIndex,
                    minZIndex, maxXIndex, maxYIndex, maxZIndex);
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
}
