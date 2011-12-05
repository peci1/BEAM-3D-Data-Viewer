/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;

import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.source.DataSourceSet4D;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.util.SkippableIterator;
import org.esa.beam.util.ValidatingIterator;

import com.bc.ceres.core.ProgressMonitor;

/**
 * A stream-backed 4D data set.
 * 
 * @author Martin Pecka
 * @param <X> The type of the values in x coordinate.
 * @param <Y> The type of the values in y coordinate.
 * @param <Z> The type of the values in z coordinate.
 * @param <W> The type of the values in w coordinate.
 */
public class StreamDataSet4D<X extends Number, Y extends Number, Z extends Number, W extends Number> extends
        StreamDataSet<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>> implements
        DataSet4D<X, Y, Z, W>
{

    /** Minimum/maximum x value. */
    private final X                        minX, maxX;
    /** Minimum/maximum y value. */
    private final Y                        minY, maxY;
    /** Minimum/maximum z value. */
    private final Z                        minZ, maxZ;
    /** Minimum/maximum w value. */
    private final W                        minW, maxW;
    /** The source of x values. */
    private final DataSource<X>            xSource;
    /** The source of y values. */
    private final DataSource<Y>            ySource;
    /** The source of z values. */
    private final DataSource<Z>            zSource;
    /** The source of w values. */
    private final DataSource<W>            wSource;
    /** The factory for creating points from single coordinates. */
    private final PointFactory<X, Y, Z, W> pointFactory;

    /**
     * A stream-backed 4D data set.
     * <p>
     * The data set will return only those points created from the sources with hashes in the usedPoints key set. The
     * hashes are obtained as the hashes of points created by the given point factory, which must be the same as the
     * factory used in the builder.
     * 
     * @param x The source of x values.
     * @param y The source of y values.
     * @param z The source of z values.
     * @param w The source of w values.
     * @param pointFactory The factory for creating points from single coordinates.
     * @param usedPoints Keys are hashes of the points, values are counts of points with the same hash.
     * @param usedPointsIndices Indices to the data source's iterator where all the used points are located.
     * @param minX Minimum x value.
     * @param minY Minimum y value.
     * @param minZ Minimum z value.
     * @param minW Minimum w value.
     * @param maxX Maximum x value.
     * @param maxY Maximum y value.
     * @param maxZ Maximum z value.
     * @param maxW Maximum w value.
     * 
     * @throws IllegalArgumentException If x,y and z don't have all the same sizes; if usedPoints has 0 size; if
     *             usedPoints is longer than x or y or z; if some max is lower than the min for a coordinate.
     */
    protected StreamDataSet4D(DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w,
            PointFactory<X, Y, Z, W> pointFactory, LinkedHashMap<Integer, Integer> usedPoints,
            List<Integer> usedPointsIndices, X minX, Y minY, Z minZ, W minW, X maxX, Y maxY, Z maxZ, W maxW)
    {
        super(usedPoints, usedPointsIndices);

        if (x.size() != y.size() || y.size() != z.size() || z.size() != w.size())
            throw new IllegalArgumentException(getClass() + ": Cannot use data sources of different sizes.");

        if (x.size() == 0 || usedPoints.size() == 0)
            throw new IllegalArgumentException(getClass() + ": Cannot create data set of zero size.");

        if (x.size() < usedPoints.size())
            throw new IllegalArgumentException(getClass()
                    + ": Cannot pass more used points than is the size of the source.");

        if (minX.doubleValue() > maxX.doubleValue() || minY.doubleValue() > maxY.doubleValue()
                || minZ.doubleValue() > maxZ.doubleValue() || minW.doubleValue() > maxW.doubleValue())
            throw new IllegalArgumentException(getClass() + ": min > max");

        xSource = x;
        ySource = y;
        zSource = z;
        wSource = w;

        this.pointFactory = pointFactory;

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.minW = minW;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.maxW = maxW;
    }

    @Override
    public Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>> pointIterator()
    {
        return new Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>>() {

            /** Iterator of the used points' indices in their data sources. */
            private final Iterator<Integer>        usedPointsIndices = StreamDataSet4D.this.usedPointsIndices
                                                                             .iterator();
            /** Iterator of the source for x values. */
            private final Iterator<NumericType<X>> xIt               = xSource.numericTypeIterator();
            /** Iterator of the source for y values. */
            private final Iterator<NumericType<Y>> yIt               = ySource.numericTypeIterator();
            /** Iterator of the source for z values. */
            private final Iterator<NumericType<Z>> zIt               = zSource.numericTypeIterator();
            /** Iterator of the source for w values. */
            private final Iterator<NumericType<W>> wIt               = wSource.numericTypeIterator();
            /** Wheteher the iterators are skippable (because instanceof may be slow). */
            private final boolean                  xSkippable        = (xIt instanceof SkippableIterator<?>),
                    ySkippable = (yIt instanceof SkippableIterator<?>),
                    zSkippable = (zIt instanceof SkippableIterator<?>),
                    wSkippable = (wIt instanceof SkippableIterator<?>);
            /** The index of the last returned value in its datasource. */
            private int                            lastIndex         = -1;

            @Override
            public boolean hasNext()
            {
                return usedPointsIndices.hasNext() && xIt.hasNext() && yIt.hasNext() && zIt.hasNext() && wIt.hasNext();
            }

            @Override
            public DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>> next()
            {
                NumericType<X> x = null;
                NumericType<Y> y = null;
                NumericType<Z> z = null;
                NumericType<W> w = null;

                final int oldIndex = lastIndex;
                lastIndex = usedPointsIndices.next();
                final int difference = lastIndex - oldIndex;

                if (!xSkippable) {
                    for (int i = 0; i < difference; i++)
                        x = xIt.next();
                } else {
                    if (difference > 1)
                        ((SkippableIterator<?>) xIt).skip(difference - 1);
                    x = xIt.next();
                }

                if (!ySkippable) {
                    for (int i = 0; i < difference; i++)
                        y = yIt.next();
                } else {
                    if (difference > 1)
                        ((SkippableIterator<?>) yIt).skip(difference - 1);
                    y = yIt.next();
                }

                if (!zSkippable) {
                    for (int i = 0; i < difference; i++)
                        z = zIt.next();
                } else {
                    if (difference > 1)
                        ((SkippableIterator<?>) zIt).skip(difference - 1);
                    z = zIt.next();
                }

                if (!wSkippable) {
                    for (int i = 0; i < difference; i++)
                        w = wIt.next();
                } else {
                    if (difference > 1)
                        ((SkippableIterator<?>) wIt).skip(difference - 1);
                    w = wIt.next();
                }

                return pointFactory.getPoint(x, y, z, w);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Iterator<X> xIterator()
    {
        return singleAxisIterator(xSource);
    }

    @Override
    public Iterator<Y> yIterator()
    {
        return singleAxisIterator(ySource);
    }

    @Override
    public Iterator<Z> zIterator()
    {
        return singleAxisIterator(zSource);
    }

    @Override
    public Iterator<W> wIterator()
    {
        return singleAxisIterator(wSource);
    }

    @Override
    public X getMinX()
    {
        return minX;
    }

    @Override
    public Y getMinY()
    {
        return minY;
    }

    @Override
    public Z getMinZ()
    {
        return minZ;
    }

    @Override
    public W getMinW()
    {
        return minW;
    }

    @Override
    public X getMaxX()
    {
        return maxX;
    }

    @Override
    public Y getMaxY()
    {
        return maxY;
    }

    @Override
    public Z getMaxZ()
    {
        return maxZ;
    }

    @Override
    public W getMaxW()
    {
        return maxW;
    }

    @Override
    public double getMin(int dimension)
    {
        switch (dimension) {
            case 0:
                return getMinX().doubleValue();
            case 1:
                return getMinY().doubleValue();
            case 2:
                return getMinZ().doubleValue();
            case 3:
                return getMinW().doubleValue();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public double getMax(int dimension)
    {
        switch (dimension) {
            case 0:
                return getMaxX().doubleValue();
            case 1:
                return getMaxY().doubleValue();
            case 2:
                return getMaxZ().doubleValue();
            case 3:
                return getMaxW().doubleValue();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String getSourceName(int dimension)
    {
        switch (dimension) {
            case 0:
                return xSource.getName();
            case 1:
                return ySource.getName();
            case 2:
                return zSource.getName();
            case 3:
                return wSource.getName();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Return a builder able to build this class.
     * 
     * @return A builder able to build this class.
     */
    protected static <X extends Number, Y extends Number, Z extends Number, W extends Number> Builder4D<X, Y, Z, W> getBuilder()
    {
        return new Builder4D<X, Y, Z, W>();
    }

    /**
     * Create a 4D stream data set from the given set of sources. If <code>maxPoints != null</code>, then it defines the
     * maximum number of points in the created set. If the sources provide more points, then a random subset will be
     * present in the resulting set (the set could be a little smaller than <code>maxPoints</code>).
     * 
     * @param maxPoints If not <code>null</code>, specifies the maximum number of points in the resulting set.
     * @param dataSourceSet The set of data sources.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return The stream data set created using the given values.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> StreamDataSet4D<X, Y, Z, W> createFromDataSources(
            Integer maxPoints, DataSourceSet4D<X, Y, Z, W> dataSourceSet, ProgressMonitor progressMonitor)
    {
        return createFromDataSources(maxPoints, dataSourceSet.getXSource(), dataSourceSet.getYSource(),
                dataSourceSet.getZSource(), dataSourceSet.getWSource(), progressMonitor);
    }

    /**
     * Create a 4D stream data set from the given sources. If <code>maxPoints != null</code>, then it defines the
     * maximum number of points in the created set. If the sources provide more points, then a random subset will be
     * present in the resulting set (the set could be a little smaller than <code>maxPoints</code>).
     * 
     * @param maxPoints If not <code>null</code>, specifies the maximum number of points in the resulting set.
     * @param x Source for x values.
     * @param y Source for y values.
     * @param z Source for z values.
     * @param w Source for w values.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     * 
     * @return The stream data set created using the given values.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> StreamDataSet4D<X, Y, Z, W> createFromDataSources(
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z, DataSource<W> w,
            ProgressMonitor progressMonitor)
    {
        Builder4D<X, Y, Z, W> builder = StreamDataSet4D.getBuilder();
        builder.setDataSourceX(x).setDataSourceY(y).setDataSourceZ(z).setDataSourceW(w);
        if (maxPoints != null)
            builder.setMaxPoints(maxPoints);
        if (progressMonitor != null)
            builder.setProgressMonitor(progressMonitor);
        return builder.getResult();
    }

    /**
     * The builder for building a stream data set.
     * 
     * @author Martin Pecka
     * @param <X> Type of values of the x coordinate.
     * @param <Y> Type of values of the y coordinate.
     * @param <Z> Type of values of the z coordinate.
     * @param <W> Type of values of the w coordinate.
     */
    protected static class Builder4D<X extends Number, Y extends Number, Z extends Number, W extends Number>
    {
        /** The source of x values. */
        private DataSource<X>                  xSource;
        /** The source of y values. */
        private DataSource<Y>                  ySource;
        /** The source of z values. */
        private DataSource<Z>                  zSource;
        /** The source of w values. */
        private DataSource<W>                  wSource;
        /** If not <code>null</code>, specifies the maximum number of points in the resulting set. */
        private Integer                        maxPoints    = null;
        /** A factory for creating points from single coordinates. */
        private final PointFactory<X, Y, Z, W> pointFactory = createPointFactory();
        /** The progress monitor. */
        private ProgressMonitor                progressMonitor;

        /**
         * When the builder is configured, call this method to get the resulting set.
         * 
         * @return The resulting set.
         * 
         * @throws IllegalArgumentException If the sources have different sizes or are not set at all.
         */
        public StreamDataSet4D<X, Y, Z, W> getResult()
        {
            if (xSource == null || ySource == null || zSource == null || wSource == null)
                throw new NullPointerException();

            if (xSource.size() != ySource.size() || ySource.size() != zSource.size()
                    || zSource.size() != wSource.size())
                throw new IllegalArgumentException(getClass()
                        + ": You must use data sources of the same size in the builder.");

            final LinkedHashMap<Integer, Integer> usedPoints = new LinkedHashMap<Integer, Integer>(
                    (maxPoints != null && maxPoints < xSource.size()) ? maxPoints : xSource.size());
            final List<Integer> usedPointsIndices = new LinkedList<Integer>();

            X minX = null, maxX = null;
            Y minY = null, maxY = null;
            Z minZ = null, maxZ = null;
            W minW = null, maxW = null;

            int alreadyProcessedPoints = 0;
            int size = xSource.size();
            ValidatingIterator<NumericType<X>> xIt = xSource.numericTypeIterator();
            ValidatingIterator<NumericType<Y>> yIt = ySource.numericTypeIterator();
            ValidatingIterator<NumericType<Z>> zIt = zSource.numericTypeIterator();
            ValidatingIterator<NumericType<W>> wIt = wSource.numericTypeIterator();

            NumericType<X> x;
            NumericType<Y> y;
            NumericType<Z> z;
            NumericType<W> w;

            if (progressMonitor != null)
                progressMonitor.beginTask("Loading band data", size); /* I18N */

            int i = -1;
            while (xIt.hasNext() && yIt.hasNext() && zIt.hasNext() && wIt.hasNext()) {
                i++;

                if (progressMonitor != null) {
                    progressMonitor.worked(1);
                    if (progressMonitor.isCanceled()) {
                        progressMonitor.done();
                        throw new CancellationException();
                    }
                }

                x = xIt.next();
                y = yIt.next();
                z = zIt.next();
                w = wIt.next();
                alreadyProcessedPoints++;

                if (!xIt.isLastReturnedValid() || !yIt.isLastReturnedValid() || !zIt.isLastReturnedValid()
                        || !wIt.isLastReturnedValid())
                    continue;

                if (minX == null || minX.doubleValue() > x.getNumber().doubleValue())
                    minX = x.getNumber();
                if (minY == null || minY.doubleValue() > y.getNumber().doubleValue())
                    minY = y.getNumber();
                if (minZ == null || minZ.doubleValue() > z.getNumber().doubleValue())
                    minZ = z.getNumber();
                if (minW == null || minW.doubleValue() > w.getNumber().doubleValue())
                    minW = w.getNumber();
                if (maxX == null || maxX.doubleValue() < x.getNumber().doubleValue())
                    maxX = x.getNumber();
                if (maxY == null || maxY.doubleValue() < y.getNumber().doubleValue())
                    maxY = y.getNumber();
                if (maxZ == null || maxZ.doubleValue() < z.getNumber().doubleValue())
                    maxZ = z.getNumber();
                if (maxW == null || maxW.doubleValue() < w.getNumber().doubleValue())
                    maxW = w.getNumber();

                Double addProbabilty = maxPoints == null ? null : ((double) (maxPoints - usedPoints.size()) / (size
                        - alreadyProcessedPoints + 1));
                if (addProbabilty != null && addProbabilty < 1 && Math.random() > addProbabilty)
                    continue;

                int hash = pointFactory.getHashCode(x, y, z, w);

                if (usedPoints.containsKey(hash)) {
                    usedPoints.put(hash, usedPoints.get(hash) + 1);
                } else {
                    usedPoints.put(hash, 1);
                    usedPointsIndices.add(i);
                }

                if (maxPoints != null && usedPoints.size() >= maxPoints)
                    break;
            }

            if (progressMonitor != null)
                progressMonitor.done();

            return new StreamDataSet4D<X, Y, Z, W>(xSource, ySource, zSource, wSource, pointFactory, usedPoints,
                    usedPointsIndices, minX, minY, minZ, minW, maxX, maxY, maxZ, maxW);
        }

        /**
         * Set the source for x coordinates.
         * 
         * @param x The source.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder4D<X, Y, Z, W> setDataSourceX(DataSource<X> x)
        {
            xSource = x;
            return this;
        }

        /**
         * Set the source for y coordinates.
         * 
         * @param y The source.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder4D<X, Y, Z, W> setDataSourceY(DataSource<Y> y)
        {
            ySource = y;
            return this;
        }

        /**
         * Set the source for z coordinates.
         * 
         * @param z The source.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder4D<X, Y, Z, W> setDataSourceZ(DataSource<Z> z)
        {
            zSource = z;
            return this;
        }

        /**
         * Set the source for w coordinates.
         * 
         * @param w The source.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder4D<X, Y, Z, W> setDataSourceW(DataSource<W> w)
        {
            wSource = w;
            return this;
        }

        /**
         * Set the maximum number of points in the resulting set.
         * 
         * @param maxPoints The max. number of points in the resulting set.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder4D<X, Y, Z, W> setMaxPoints(int maxPoints)
        {
            this.maxPoints = maxPoints;
            return this;
        }

        /**
         * @param progressMonitor The progress monitor.
         */
        public void setProgressMonitor(ProgressMonitor progressMonitor)
        {
            this.progressMonitor = progressMonitor;
        }

        /**
         * Return a factory for creating points from coordinates.
         * 
         * @return The factory.
         */
        protected PointFactory<X, Y, Z, W> createPointFactory()
        {
            return new PointFactory<X, Y, Z, W>();
        }
    }

    /**
     * A factory for creating points from single coordinates.
     * 
     * @author Martin Pecka
     * @param <X> The type of values in x coordinate.
     * @param <Y> The type of values in y coordinate.
     * @param <Z> The type of values in z coordinate.
     * @param <W> The type of values in w coordinate.
     */
    public static class PointFactory<X extends Number, Y extends Number, Z extends Number, W extends Number>
    {
        /**
         * Create a point from the given coordinate values.
         * 
         * @param x X coordinate value.
         * @param y Y coordinate value.
         * @param z Z coordinate value.
         * @param w W coordinate value.
         * @return The point.
         */
        public DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>> getPoint(NumericType<X> x,
                NumericType<Y> y, NumericType<Z> z, NumericType<W> w)
        {
            return new SimpleDataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>(x, y, z, w);
        }

        /**
         * Return the hashcode of a point from the given values.
         * 
         * @param x X coordinate value.
         * @param y Y coordinate value.
         * @param z Z coordinate value.
         * @param w W coordinate value.
         * @return The point's hashcode.
         */
        public int getHashCode(NumericType<X> x, NumericType<Y> y, NumericType<Z> z, NumericType<W> w)
        {
            return SimpleDataPoint4D.hashCode(x, y, z, w);
        }
    }

    /**
     * Return a default implementation of the point factory.
     * 
     * @return A default implementation of the point factory.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> PointFactory<X, Y, Z, W> createDefaultPointFactory()
    {
        return new PointFactory<X, Y, Z, W>();
    }
}
