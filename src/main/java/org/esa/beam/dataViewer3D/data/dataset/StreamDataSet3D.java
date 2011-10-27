/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.esa.beam.dataViewer3D.data.point.DataPoint;
import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.util.SkippableIterator;
import org.esa.beam.util.ValidatingIterator;

/**
 * A stream-backed 3D data set.
 * 
 * @author Martin Pecka
 * @param <X> The type of the values in x coordinate.
 * @param <Y> The type of the values in y coordinate.
 * @param <Z> The type of the values in z coordinate.
 */
public class StreamDataSet3D<X extends Number, Y extends Number, Z extends Number> extends
        StreamDataSet<DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>> implements DataSet3D<X, Y, Z>
{

    /** Minimum/maximum x value. */
    private final X                     minX, maxX;
    /** Minimum/maximum y value. */
    private final Y                     minY, maxY;
    /** Minimum/maximum z value. */
    private final Z                     minZ, maxZ;
    /** The source of x values. */
    private final DataSource<X>         xSource;
    /** The source of y values. */
    private final DataSource<Y>         ySource;
    /** The source of z values. */
    private final DataSource<Z>         zSource;
    /** The factory for creating points from single coordinates. */
    private final PointFactory<X, Y, Z> pointFactory;

    /**
     * A stream-backed 3D data set.
     * <p>
     * The data set will return only those points created from the sources with hashes in the usedPoints key set. The
     * hashes are obtained as the hashes of points created by the given point factory, which must be the same as the
     * factory used in the builder.
     * 
     * @param x The source of x values.
     * @param y The source of y values.
     * @param z The source of z values.
     * @param pointFactory The factory for creating points from single coordinates.
     * @param usedPoints Keys are hashes of the points, values are counts of points with the same hash.
     * @param usedPointsIndices Indices to the data source's iterator where all the used points are located.
     * @param minX Minimum x value.
     * @param minY Minimum y value.
     * @param minZ Minimum z value.
     * @param maxX Maximum x value.
     * @param maxY Maximum y value.
     * @param maxZ Maximum z value.
     * 
     * @throws IllegalArgumentException If x,y and z don't have all the same sizes; if usedPoints has 0 size; if
     *             usedPoints is longer than x or y or z; if some max is lower than the min for a coordinate.
     */
    protected StreamDataSet3D(DataSource<X> x, DataSource<Y> y, DataSource<Z> z, PointFactory<X, Y, Z> pointFactory,
            LinkedHashMap<Integer, Integer> usedPoints, List<Integer> usedPointsIndices, X minX, Y minY, Z minZ,
            X maxX, Y maxY, Z maxZ)
    {
        super(usedPoints, usedPointsIndices);

        if (x.size() != y.size() || y.size() != z.size())
            throw new IllegalArgumentException(getClass() + ": Cannot use data sources of different sizes.");

        if (x.size() == 0 || usedPoints.size() == 0)
            throw new IllegalArgumentException(getClass() + ": Cannot create data set of zero size.");

        if (x.size() < usedPoints.size())
            throw new IllegalArgumentException(getClass()
                    + ": Cannot pass more used points than is the size of the source.");

        if (minX.doubleValue() > maxX.doubleValue() || minY.doubleValue() > maxY.doubleValue()
                || minZ.doubleValue() > maxZ.doubleValue())
            throw new IllegalArgumentException(getClass() + ": min > max");

        xSource = x;
        ySource = y;
        zSource = z;

        this.pointFactory = pointFactory;

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override
    public Iterator<DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>> pointIterator()
    {
        return new Iterator<DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>>() {

            /** Iterator of the used points' indices in their data sources. */
            private final Iterator<Integer>        usedPointsIndices = StreamDataSet3D.this.usedPointsIndices
                                                                             .iterator();
            /** Iterator of the source for x values. */
            private final Iterator<NumericType<X>> xIt               = xSource.numericTypeIterator();
            /** Iterator of the source for y values. */
            private final Iterator<NumericType<Y>> yIt               = ySource.numericTypeIterator();
            /** Iterator of the source for z values. */
            private final Iterator<NumericType<Z>> zIt               = zSource.numericTypeIterator();
            /** Wheteher the iterators are skippable (because instanceof may be slow). */
            private final boolean                  xSkippable        = (xIt instanceof SkippableIterator<?>),
                    ySkippable = (yIt instanceof SkippableIterator<?>),
                    zSkippable = (zIt instanceof SkippableIterator<?>);
            /** The index of the last returned value in its datasource. */
            private int                            lastIndex         = -1;

            @Override
            public boolean hasNext()
            {
                return usedPointsIndices.hasNext() && xIt.hasNext() && yIt.hasNext() && zIt.hasNext();
            }

            @Override
            public DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>> next()
            {
                NumericType<X> x = null;
                NumericType<Y> y = null;
                NumericType<Z> z = null;

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

                return pointFactory.getPoint(x, y, z);
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

    /**
     * Return a builder able to build this class.
     * 
     * @return A builder able to build this class.
     */
    protected static <X extends Number, Y extends Number, Z extends Number> Builder3D<X, Y, Z> getBuilder()
    {
        return new Builder3D<X, Y, Z>();
    }

    /**
     * Create a 3D stream data set from the given sources. If <code>maxPoints != null</code>, then it defines the
     * maximum number of points in the created set. If the sources provide more points, then a random subset will be
     * present in the resulting set (the set could be a little smaller than <code>maxPoints</code>).
     * 
     * @param maxPoints If not <code>null</code>, specifies the maximum number of points in the resulting set.
     * @param x Source for x values.
     * @param y Source for x values.
     * @param z Source for x values.
     * 
     * @return The stream data set created using the given values.
     */
    public static <X extends Number, Y extends Number, Z extends Number> StreamDataSet3D<X, Y, Z> createFromDataSources(
            Integer maxPoints, DataSource<X> x, DataSource<Y> y, DataSource<Z> z)
    {
        Builder3D<X, Y, Z> builder = StreamDataSet3D.getBuilder();
        builder.setDataSourceX(x).setDataSourceY(y).setDataSourceZ(z);
        if (maxPoints != null)
            builder.setMaxPoints(maxPoints);
        return builder.getResult();
    }

    /**
     * The builder for building a stream data set.
     * 
     * @author Martin Pecka
     * @param <X> Type of values of the x coordinate.
     * @param <Y> Type of values of the y coordinate.
     * @param <Z> Type of values of the z coordinate.
     */
    protected static class Builder3D<X extends Number, Y extends Number, Z extends Number>
    {
        /** The source of x values. */
        private DataSource<X>               xSource;
        /** The source of y values. */
        private DataSource<Y>               ySource;
        /** The source of z values. */
        private DataSource<Z>               zSource;
        /** If not <code>null</code>, specifies the maximum number of points in the resulting set. */
        private Integer                     maxPoints    = null;
        /** A factory for creating points from single coordinates. */
        private final PointFactory<X, Y, Z> pointFactory = createPointFactory();

        /**
         * When the builder is configured, call this method to get the resulting set.
         * 
         * @return The resulting set.
         * 
         * @throws IllegalArgumentException If the sources have different sizes or are not set at all.
         */
        public StreamDataSet3D<X, Y, Z> getResult()
        {
            if (xSource == null || ySource == null || zSource == null)
                throw new NullPointerException();

            if (xSource.size() != ySource.size() || ySource.size() != zSource.size())
                throw new IllegalArgumentException(getClass()
                        + ": You must use data sources of the same size in the builder.");

            final LinkedHashMap<Integer, Integer> usedPoints = new LinkedHashMap<Integer, Integer>(
                    (maxPoints != null && maxPoints < xSource.size()) ? maxPoints : xSource.size());
            final List<Integer> usedPointsIndices = new LinkedList<Integer>();

            X minX = null, maxX = null;
            Y minY = null, maxY = null;
            Z minZ = null, maxZ = null;

            int alreadyProcessedPoints = 0;
            int size = xSource.size();
            ValidatingIterator<NumericType<X>> xIt = xSource.numericTypeIterator();
            ValidatingIterator<NumericType<Y>> yIt = ySource.numericTypeIterator();
            ValidatingIterator<NumericType<Z>> zIt = zSource.numericTypeIterator();

            NumericType<X> x;
            NumericType<Y> y;
            NumericType<Z> z;

            int i = -1;
            while (xIt.hasNext() && yIt.hasNext() && zIt.hasNext()) {
                i++;

                x = xIt.next();
                y = yIt.next();
                z = zIt.next();
                alreadyProcessedPoints++;

                if (!xIt.isLastReturnedValid() || !yIt.isLastReturnedValid() || !zIt.isLastReturnedValid())
                    continue;

                if (minX == null || minX.doubleValue() > x.getNumber().doubleValue())
                    minX = x.getNumber();
                if (minY == null || minY.doubleValue() > y.getNumber().doubleValue())
                    minY = y.getNumber();
                if (minZ == null || minZ.doubleValue() > z.getNumber().doubleValue())
                    minZ = z.getNumber();
                if (maxX == null || maxX.doubleValue() < x.getNumber().doubleValue())
                    maxX = x.getNumber();
                if (maxY == null || maxY.doubleValue() < y.getNumber().doubleValue())
                    maxY = y.getNumber();
                if (maxZ == null || maxZ.doubleValue() < z.getNumber().doubleValue())
                    maxZ = z.getNumber();

                Double addProbabilty = maxPoints == null ? null : ((double) (maxPoints - usedPoints.size()) / (size
                        - alreadyProcessedPoints + 1));
                if (addProbabilty != null && addProbabilty < 1 && Math.random() > addProbabilty)
                    continue;

                DataPoint point = pointFactory.getPoint(x, y, z);
                int hash = point.hashCode();
                point = null;

                if (usedPoints.containsKey(hash)) {
                    usedPoints.put(hash, usedPoints.get(hash) + 1);
                } else {
                    usedPoints.put(hash, 1);
                    usedPointsIndices.add(i);
                }
            }

            return new StreamDataSet3D<X, Y, Z>(xSource, ySource, zSource, pointFactory, usedPoints, usedPointsIndices,
                    minX, minY, minZ, maxX, maxY, maxZ);
        }

        /**
         * Set the source for x coordinates.
         * 
         * @param x The source.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder3D<X, Y, Z> setDataSourceX(DataSource<X> x)
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
        public Builder3D<X, Y, Z> setDataSourceY(DataSource<Y> y)
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
        public Builder3D<X, Y, Z> setDataSourceZ(DataSource<Z> z)
        {
            zSource = z;
            return this;
        }

        /**
         * Set the maximum number of points in the resulting set.
         * 
         * @param maxPoints The max. number of points in the resulting set.
         * @return <code>this</code> - provides fluent interface.
         */
        public Builder3D<X, Y, Z> setMaxPoints(int maxPoints)
        {
            this.maxPoints = maxPoints;
            return this;
        }

        /**
         * Return a factory for creating points from coordinates.
         * 
         * @return The factory.
         */
        protected PointFactory<X, Y, Z> createPointFactory()
        {
            return new PointFactory<X, Y, Z>();
        }
    }

    /**
     * A factory for creating points from single coordinates.
     * 
     * @author Martin Pecka
     * @param <X> The type of values in x coordinate.
     * @param <Y> The type of values in y coordinate.
     * @param <Z> The type of values in z coordinate.
     */
    public static class PointFactory<X extends Number, Y extends Number, Z extends Number>
    {
        /**
         * Create a point from the given coordinate values.
         * 
         * @param x X coordinate value.
         * @param y Y coordinate value.
         * @param z Z coordinate value.
         * @return The point.
         */
        public DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>> getPoint(NumericType<X> x, NumericType<Y> y,
                NumericType<Z> z)
        {
            // TODO maybe substitute with a mutable point in order not to create too much instances
            return new SimpleDataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>(x, y, z);
        }
    }

    /**
     * Return a default implementation of the point factory.
     * 
     * @return A default implementation of the point factory.
     */
    public static <X extends Number, Y extends Number, Z extends Number> PointFactory<X, Y, Z> createDefaultPointFactory()
    {
        return new PointFactory<X, Y, Z>();
    }
}
