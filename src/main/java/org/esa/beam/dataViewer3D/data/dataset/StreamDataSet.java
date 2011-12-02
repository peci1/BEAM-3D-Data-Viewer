/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet3D;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet4D;
import org.esa.beam.util.Guardian;
import org.esa.beam.util.SkippableIterator;

import com.bc.ceres.core.ProgressMonitor;

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

    /** Indices to the data source's iterator where all the used points are located. */
    protected final List<Integer>                   usedPointsIndices;

    /**
     * A stream-backed data set.
     * 
     * @param usedPoints Keys are hashes of the points, values are counts of points with the same hash.
     * @param usedPointsIndices Indices to the data source's iterator where all the used points are located.
     */
    protected StreamDataSet(LinkedHashMap<Integer, Integer> usedPoints, List<Integer> usedPointsIndices)
    {
        this.usedPoints = usedPoints;
        this.usedPointsIndices = usedPointsIndices;
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
     * Return an iterator over one axis' data.
     * 
     * @param source The source of the axis' data.
     * @return The iterator.
     */
    protected <N extends Number> Iterator<N> singleAxisIterator(final DataSource<N> source)
    {
        if (source.iterator() instanceof SkippableIterator<?>)
            return new SingleAxisSkippingIterator<N>(source);
        else
            return new SingleAxisIterator<N>(source);
    }

    /**
     * Create a new 3D or 4D data set from the given data sources set.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param sourceSet The set of sources.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static DataSet createFromDataSources(Integer maxPoints, DataSourceSet sourceSet,
            ProgressMonitor progressMonitor)
    {
        if (sourceSet instanceof DataSourceSet3D<?, ?, ?>)
            return createFromDataSources(maxPoints, (DataSourceSet3D<?, ?, ?>) sourceSet, progressMonitor);
        else
            return createFromDataSources(maxPoints, (DataSourceSet4D<?, ?, ?, ?>) sourceSet, progressMonitor);
    }

    /**
     * Create a new 3D data set from the given data sources set.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param sourceSet The set of sources.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number> DataSet3D<X, Y, Z> createFromDataSources(
            Integer maxPoints, DataSourceSet3D<X, Y, Z> sourceSet, ProgressMonitor progressMonitor)
    {
        return createFromDataSources(maxPoints, sourceSet.getXSource(), sourceSet.getYSource(), sourceSet.getZSource(),
                progressMonitor);
    }

    /**
     * Create a new 4D data set from the given data sources set.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param sourceSet The set of sources.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> DataSet4D<X, Y, Z, W> createFromDataSources(
            Integer maxPoints, DataSourceSet4D<X, Y, Z, W> sourceSet, ProgressMonitor progressMonitor)
    {
        return createFromDataSources(maxPoints, sourceSet.getXSource(), sourceSet.getYSource(), sourceSet.getZSource(),
                sourceSet.getWSource(), progressMonitor);
    }

    /**
     * Create a new 3D data set from the given data sources.
     * 
     * @param maxPoints The maximum number of data points in the resulting set (<code>null</code> means the count is
     *            unbounded).
     * @param x The data source for the x coordinate.
     * @param y The data source for the y coordinate.
     * @param z The data source for the z coordinate.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number> DataSet3D<X, Y, Z> createFromDataSources(
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, ProgressMonitor progressMonitor)
    {
        return StreamDataSet3D.createFromDataSources(maxPoints, x, y, z, progressMonitor);
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
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return A new 4D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> DataSet4D<X, Y, Z, W> createFromDataSources(
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w,
            ProgressMonitor progressMonitor)
    {
        return StreamDataSet4D.createFromDataSources(maxPoints, x, y, z, w, progressMonitor);
    }

    /**
     * A single axis iterator which provides an efficient way to iterate over data sources with skippable iterators.
     * 
     * @author Martin Pecka
     * @param <N> Type of the data.
     */
    protected class SingleAxisSkippingIterator<N extends Number> extends SingleAxisIterator<N>
    {
        public SingleAxisSkippingIterator(final DataSource<N> source)
        {
            super(source);
            Guardian.assertTrue("not a skippable iterator", sourceIt instanceof SkippableIterator<?>);
        }

        @Override
        public N next()
        {
            final int oldIndex = lastIndex;
            lastIndex = indices.next();
            final int difference = lastIndex - oldIndex;

            if (difference > 1)
                ((SkippableIterator<?>) sourceIt).skip(difference - 1);

            return sourceIt.next();
        }
    }

    /**
     * Iterator of a single axis' data.
     * 
     * @author Martin Pecka
     * @param <N> Type of the data.
     */
    protected class SingleAxisIterator<N extends Number> implements Iterator<N>
    {
        protected final Iterator<N>       sourceIt;
        protected final Iterator<Integer> indices   = usedPointsIndices.iterator();
        protected int                     lastIndex = -1;

        public SingleAxisIterator(final DataSource<N> source)
        {
            sourceIt = source.iterator();
        }

        @Override
        public boolean hasNext()
        {
            return indices.hasNext();
        }

        @Override
        public N next()
        {
            final int oldIndex = lastIndex;
            lastIndex = indices.next();
            final int difference = lastIndex - oldIndex;
            N value = null;

            for (int i = 0; i < difference; i++) {
                value = sourceIt.next();
            }
            return value;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
