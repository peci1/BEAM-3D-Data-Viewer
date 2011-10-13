/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.source.DataSource;

/**
 * A stream-backed data set. Doesn't duplicate the data from the input sources.
 * 
 * @author Martin Pecka
 * @param <P> The type of the points this data set contains.
 */
abstract class StreamDataSet<P extends DataPoint> extends AbstractDataSet
{

    /**
     * The maximum number of data points this set can contain (<code>null</code> means no other than the free memory
     * limit is set).
     */
    public static final Long                        MAX_SIZE = (long) (Integer.MAX_VALUE - 1);

    // we don't use the points as keys here in order to be able to free the points from memory if we don't need them
    /** Keys are hashes of the points, values are counts of points with the same hash. */
    protected final LinkedHashMap<Integer, Integer> usedPoints;

    /**
     * A stream-backed data set.
     * 
     * @param usedPoints Keys are hashes of the points, values are counts of points with the same hash.
     */
    protected StreamDataSet(LinkedHashMap<Integer, Integer> usedPoints)
    {
        this.usedPoints = usedPoints;
    }

    @Override
    public int size()
    {
        return usedPoints.size();
    }

    @Override
    public Iterator<DataPoint> iterator()
    {
        return new Iterator<DataPoint>() {
            private Iterator<? extends DataPoint> pointIt = pointIterator();

            @Override
            public boolean hasNext()
            {
                return pointIt.hasNext();
            }

            @Override
            public DataPoint next()
            {
                return pointIt.next();
            }

            @Override
            public void remove()
            {
                pointIt.remove();
            }
        };
    }

    @Override
    public Iterator<Integer> histogramIterator()
    {
        return usedPoints.values().iterator();
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
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z)
    {
        return StreamDataSet3D.createFromDataSources(maxPoints, x, y, z);
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
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w)
    {
        return StreamDataSet4D.createFromDataSources(maxPoints, x, y, z, w);
    }
}
