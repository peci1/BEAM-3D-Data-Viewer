/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Arrays;
import java.util.Iterator;

import javax.help.UnsupportedOperationException;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet3D;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet4D;

import com.bc.ceres.core.ProgressMonitor;

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
    private final Integer[]  histogram;

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
    protected ArrayDataSet(Integer[] histogram)
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
    public int size()
    {
        return getData().length;
    }

    @Override
    public Iterator<DataPoint> iterator()
    {
        return Arrays.asList(getData()).iterator();
    }

    @Override
    public Iterator<Integer> histogramIterator()
    {
        return Arrays.asList(histogram).iterator();
    }

    @Override
    public String getSourceName(int dimension)
    {
        switch (dimension) {
            case 0:
                return "x";
            case 1:
                return "y";
            case 2:
                return "z";
            case 3:
                return "w";
            default:
                throw new IndexOutOfBoundsException();
        }
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
     * 
     * @return A new 3D data set from the given data sources.
     */
    public static <X extends Number, Y extends Number, Z extends Number> DataSet3D<X, Y, Z> createFromDataSources(
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z)
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
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w)
    {
        ArrayDataSet4D.Builder4D<X, Y, Z, W> builder = ArrayDataSet4D.getBuilder(maxPoints, x.size());

        setupBuilderFromDataSources(builder, x, y, z, w);

        return builder.getResult();
    }

    /**
     * Iterator over a single axis' data.
     * 
     * @author Martin Pecka
     * @param <N> Type of the axis' data.
     */
    protected abstract class SingleAxisIterator<N extends Number> implements Iterator<N>
    {
        protected int i = 0;

        @Override
        public boolean hasNext()
        {
            return i + 1 < size();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

    }

}