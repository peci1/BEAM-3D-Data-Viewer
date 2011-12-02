/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.RenderedImage;
import java.util.Iterator;

import javax.media.jai.operator.MinDescriptor;

import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.framework.datamodel.Mask;

/**
 * A set of 4 {@link BandDataSource}s.
 * 
 * @author Martin Pecka
 * @param <X> The type of x coordinate.
 * @param <Y> The type of y coordinate.
 * @param <Z> The type of z coordinate.
 * @param <W> The type of w coordinate.
 */
public class BandDataSourceSet4D<X extends Number, Y extends Number, Z extends Number, W extends Number> implements
        DataSourceSet4D<X, Y, Z, W>
{

    /** X coordinate data source. */
    protected final BandDataSource<X> xSource;

    /** Y coordinate data source. */
    protected final BandDataSource<Y> ySource;

    /** Z coordinate data source. */
    protected final BandDataSource<Z> zSource;

    /** W coordinate data source. */
    protected final BandDataSource<W> wSource;

    /**
     * Create a new set of 4 band data sources.
     * 
     * @param xSource X coordinate source.
     * @param ySource Y coordinate source.
     * @param zSource Z coordinate source.
     * @param wSource W coordinate source.
     * @param roiMask The ROI mask. Pass <code>null</code> if you are interested into the whole image.
     */
    public BandDataSourceSet4D(BandDataSource<X> xSource, BandDataSource<Y> ySource, BandDataSource<Z> zSource,
            BandDataSource<W> wSource, Mask roiMask)
    {
        this.xSource = xSource;
        this.ySource = ySource;
        this.zSource = zSource;
        this.wSource = wSource;

        // here we assume data source compatibility to be transitive, which should be ok
        if (!xSource.isCompatible(ySource) || !ySource.isCompatible(zSource) || !zSource.isCompatible(wSource))
            throw new IllegalArgumentException("Incompatible data sources.");

        // compute the common mask of all sources
        final RenderedImage roiImage = (roiMask != null ? roiMask.getSourceImage() : null);

        final RenderedImage xMaskImage = xSource.getBand().getValidMaskImage();
        final RenderedImage yMaskImage = ySource.getBand().getValidMaskImage();
        final RenderedImage zMaskImage = zSource.getBand().getValidMaskImage();
        final RenderedImage wMaskImage = wSource.getBand().getValidMaskImage();

        RenderedImage maskImage = null;
        for (RenderedImage image : new RenderedImage[] { xMaskImage, yMaskImage, zMaskImage, wMaskImage, roiImage }) {
            if (image != null && maskImage != image) {
                if (maskImage == null)
                    maskImage = image;
                else
                    maskImage = MinDescriptor.create(maskImage, image, null);
            }
        }

        final Shape roiShape = (roiMask != null ? roiMask.getValidShape() : null);

        final Shape xValidShape = xSource.getBand().getValidShape();
        final Shape yValidShape = ySource.getBand().getValidShape();
        final Shape zValidShape = zSource.getBand().getValidShape();
        final Shape wValidShape = wSource.getBand().getValidShape();

        Shape effectiveShape = null;
        for (Shape shape : new Shape[] { xValidShape, yValidShape, zValidShape, wValidShape, roiShape }) {
            if (shape != null && shape != effectiveShape) {
                if (effectiveShape != null) {
                    Area area = new Area(effectiveShape);
                    area.intersect(new Area(shape));
                    effectiveShape = area;
                } else {
                    effectiveShape = shape;
                }
            }
        }

        for (int i = 0; i < 4; i++)
            getDataSource(i).setMask(effectiveShape, maskImage);
    }

    @Override
    public BandDataSource<?> getDataSource(int index)
    {
        switch (index) {
            case 0:
                return xSource;
            case 1:
                return ySource;
            case 2:
                return zSource;
            case 3:
                return wSource;
            default:
                throw new IndexOutOfBoundsException("Cannot get data source with index " + index
                        + " from a 4-dimensional data source set.");
        }
    }

    @Override
    public DataSource<X> getXSource()
    {
        return xSource;
    }

    @Override
    public DataSource<Y> getYSource()
    {
        return ySource;
    }

    @Override
    public DataSource<Z> getZSource()
    {
        return zSource;
    }

    @Override
    public DataSource<W> getWSource()
    {
        return wSource;
    }

    @Override
    public Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>> pointIterator()
    {
        return new Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>>() {
            private final PointFactory             pointFactory = createPointFactory();

            /** Iterator of the source for x values. */
            private final Iterator<NumericType<X>> xIt          = xSource.numericTypeIterator();
            /** Iterator of the source for y values. */
            private final Iterator<NumericType<Y>> yIt          = ySource.numericTypeIterator();
            /** Iterator of the source for z values. */
            private final Iterator<NumericType<Z>> zIt          = zSource.numericTypeIterator();
            /** Iterator of the source for w values. */
            private final Iterator<NumericType<W>> wIt          = wSource.numericTypeIterator();

            @Override
            public boolean hasNext()
            {
                return xIt.hasNext() && yIt.hasNext() && zIt.hasNext() && wIt.hasNext();
            }

            @Override
            public DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>> next()
            {
                return pointFactory.getPoint(xIt.next(), yIt.next(), zIt.next(), wIt.next());
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
        return xSource.iterator();
    }

    @Override
    public Iterator<Y> yIterator()
    {
        return ySource.iterator();
    }

    @Override
    public Iterator<Z> zIterator()
    {
        return zSource.iterator();
    }

    @Override
    public Iterator<W> wIterator()
    {
        return wSource.iterator();
    }

    /**
     * Convenience method for creating the band data source set.
     * 
     * @param xSource X coordinate source.
     * @param ySource Y coordinate source.
     * @param zSource Z coordinate source.
     * @param wSource W coordinate source.
     * @param roiMask The ROI mask. Pass <code>null</code> if you are interested into the whole image.
     * @return The corresponding band data set.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> BandDataSourceSet4D<X, Y, Z, W> create(
            BandDataSource<X> xSource, BandDataSource<Y> ySource, BandDataSource<Z> zSource, BandDataSource<W> wSource,
            Mask roiMask)
    {
        return new BandDataSourceSet4D<X, Y, Z, W>(xSource, ySource, zSource, wSource, roiMask);
    }

    /**
     * @return A point factory.
     */
    protected PointFactory createPointFactory()
    {
        return new PointFactory();
    }

    /**
     * A factory for creating points ot of the single coordinates.
     * 
     * @author Martin Pecka
     */
    protected class PointFactory
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
    }
}
