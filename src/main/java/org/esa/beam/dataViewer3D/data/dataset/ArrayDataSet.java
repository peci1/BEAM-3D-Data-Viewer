/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Arrays;
import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.source.DataSource;

/**
 * A data set backed by array.
 * 
 * @author Martin Pecka
 */
public abstract class ArrayDataSet extends AbstractDataSet
{

    /**
     * The array containing the counts of data points with the same value in the source (indices correspond to indices
     * in <code>data</code>).
     */
    private final Long[]     histogram;

    /**
     * The maximum number of data points this set can contain (<code>null</code> means no other than the free memory
     * limit is set).
     */
    public static final Long MAX_SIZE = (long) (Integer.MAX_VALUE - 1);

    /**
     * Create a data set with the given histogram.
     * 
     * @param histogram The array containing the counts of data points with the same value in the source.
     */
    protected ArrayDataSet(Long[] histogram)
    {
        this.histogram = histogram;
    }

    /**
     * Return the data of this set as an array of generic data points.
     * 
     * @return The data of this set as an array of generic data points.
     */
    protected abstract DataPoint[] getData();

    @Override
    public long size()
    {
        return getData().length;
    }

    @Override
    public Iterator<DataPoint> iterator()
    {
        return Arrays.asList(getData()).iterator();
    }

    @Override
    public Iterator<Long> histogramIterator()
    {
        return Arrays.asList(histogram).iterator();
    }

    /**
     * Create a new 3D data set from the given data sources.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param x The data source for the x coordinate.
     * @param y The data source for the y coordinate.
     * @param z The data source for the z coordinate.
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number> DataSet3D<X, Y, Z> createFromDataSources(
            Long maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z)
    {
        ArrayDataSet3D.Builder3D<X, Y, Z> builder = ArrayDataSet3D.getBuilder(maxPoints, x.size());

        setupBuilderFromDataSources(builder, x, y, z);

        return builder.getResult();
    }

    /**
     * Create a new 4D data set from the given data sources.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param x The data source for the x coordinate.
     * @param y The data source for the y coordinate.
     * @param z The data source for the z coordinate.
     * @param w The data source for the w coordinate.
     * 
     * @return A new 4D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> DataSet4D<X, Y, Z, W> createFromDataSources(
            Long maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w)
    {
        ArrayDataSet4D.Builder4D<X, Y, Z, W> builder = ArrayDataSet4D.getBuilder(maxPoints, x.size());

        setupBuilderFromDataSources(builder, x, y, z, w);

        return builder.getResult();
    }

}