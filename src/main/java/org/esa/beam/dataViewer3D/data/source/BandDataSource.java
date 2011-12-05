/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.castToType;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.NoSuchElementException;

import javax.help.UnsupportedOperationException;
import javax.media.jai.PixelAccessor;
import javax.media.jai.PlanarImage;
import javax.media.jai.UnpackedImageData;

import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.LongType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.data.type.ShortType;
import org.esa.beam.framework.datamodel.ProductData;
import org.esa.beam.framework.datamodel.RasterDataNode;
import org.esa.beam.framework.datamodel.VirtualBand;
import org.esa.beam.util.Guardian;
import org.esa.beam.util.SkippableIterator;
import org.esa.beam.util.ValidatingIterator;

/**
 * A data source taking the input data from a band.
 * 
 * @author Martin Pecka
 * @param <N> Type of the band values.
 */
public class BandDataSource<N extends Number> implements DataSource<N>
{

    /** The source band. */
    protected final RasterDataNode          band;

    /** The planar image of the raster. */
    protected final PlanarImage             planarImage;

    /** The source image of the raster node. */
    protected final RenderedImage           sourceImage;

    /** The shape we're interested in. */
    protected Shape                         maskShape;

    /** The mask we're interested in. */
    protected RenderedImage                 maskImage;

    /** The pixel accessor. */
    protected PixelAccessor                 maskAccessor;

    /** The type of the data this class returns. */
    protected final Class<? extends Number> dataType;

    /** The precision for decimal type bands. */
    protected final Integer                 precision;

    /** The min and max values this source can return. If <code>null</code>, there is no restriction on min/max values. */
    protected final N                       definedMin, definedMax;

    /** Accessor the the raster's pixels. */
    protected final PixelAccessor           dataAccessor;

    /** The sample model of the raster. */
    protected final SampleModel             dataSampleModel;

    /** The sample model of the mask. */
    protected SampleModel                   maskSampleModel;

    /** The min and max values to be considered - these are already scaled. */
    protected final Double                  min, max;

    /** The count of tiles. */
    protected final int                     numXTiles, numYTiles;

    /** Offset to the first tile. */
    protected final int                     tileX1, tileY1;

    /** Offset to the last tile. */
    protected final int                     tileX2, tileY2;

    /** The rectangle the pixels we are interested into reside in. */
    protected final Rectangle               imageRect;

    /**
     * Create the data source from the given band.
     * <p>
     * Use a default of 10 valid decimal digits for comparing decimal numbers.
     * 
     * @param band The source band.
     * @param dataType Type of data to be read from the band's raster.
     * @param min The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @param max The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     */
    protected BandDataSource(RasterDataNode band, Class<N> dataType, N min, N max)
    {
        this(band, dataType, 10, min, max);
    }

    /**
     * Create the data source from the given band.
     * 
     * @param band The source band.
     * @param dataType Type of data to be read from the band's raster.
     * @param precision The number of valid decimal digits for decimal numbers comparison.
     * @param min The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @param max The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     */
    protected BandDataSource(RasterDataNode band, Class<N> dataType, int precision, N min, N max)
    {
        this.band = band;
        this.dataType = dataType;
        this.precision = precision;
        this.sourceImage = band.getSourceImage();
        this.planarImage = band.getSourceImage();
        this.definedMin = min;
        this.definedMax = max;

        this.min = min != null ? band.scaleInverse(min.doubleValue()) : null;
        this.max = max != null ? band.scaleInverse(max.doubleValue()) : null;

        this.dataSampleModel = sourceImage.getSampleModel();

        if (dataSampleModel.getNumBands() != 1) {
            throw new IllegalStateException("dataSampleModel.numBands != 1");
        }

        dataAccessor = new PixelAccessor(dataSampleModel, null);

        numXTiles = sourceImage.getNumXTiles();
        numYTiles = sourceImage.getNumYTiles();

        tileX1 = sourceImage.getTileGridXOffset();
        tileY1 = sourceImage.getTileGridYOffset();
        tileX2 = tileX1 + numXTiles - 1;
        tileY2 = tileY1 + numYTiles - 1;

        imageRect = new Rectangle(sourceImage.getMinX(), sourceImage.getMinY(), sourceImage.getWidth(),
                sourceImage.getHeight());
    }

    /**
     * Set the interest mask of the image.
     * 
     * @param maskShape The shape of the mask.
     * @param maskImage The bitmap of the mask.
     * 
     * @throws IllegalStateException If the maskImage's sample model doesn't have exactly 1 band or isn't of byte type.
     * @throws IllegalStateException If the maskImage is incompatible with this source's raster.
     */
    public void setMask(Shape maskShape, RenderedImage maskImage)
    {
        this.maskShape = maskShape;
        this.maskImage = maskImage;

        if (maskImage != null) {
            final SampleModel maskSampleModel = maskImage.getSampleModel();
            if (maskSampleModel.getNumBands() != 1) {
                throw new IllegalStateException("maskSampleModel.numBands != 1");
            }
            if (maskSampleModel.getDataType() != DataBuffer.TYPE_BYTE) {
                throw new IllegalStateException("maskSampleModel.dataType != TYPE_BYTE");
            }

            this.maskSampleModel = maskSampleModel;

            maskAccessor = new PixelAccessor(maskSampleModel, null);

            if (maskImage.getMinX() != sourceImage.getMinX()) {
                throw new IllegalStateException("maskImage.getMinX() != sourceImage.getMinX()");
            }
            if (maskImage.getMinY() != sourceImage.getMinY()) {
                throw new IllegalStateException("maskImage.getMinY() != sourceImage.getMinY()");
            }
            if (maskImage.getWidth() != sourceImage.getWidth()) {
                throw new IllegalStateException("maskImage.getWidth() != sourceImage.getWidth()");
            }
            if (maskImage.getHeight() != sourceImage.getHeight()) {
                throw new IllegalStateException("maskImage.getWidth() != sourceImage.getWidth()");
            }

            if (maskImage.getTileGridXOffset() != sourceImage.getTileGridXOffset()) {
                throw new IllegalStateException("maskImage.getTileGridXOffset() != sourceImage.getTileGridXOffset()");
            }

            if (maskImage.getTileGridYOffset() != sourceImage.getTileGridYOffset()) {
                throw new IllegalStateException("maskImage.getTileGridYOffset() != sourceImage.getTileGridYOffset()");
            }

            if (maskImage.getNumXTiles() != sourceImage.getNumXTiles()) {
                throw new IllegalStateException("maskImage.getNumXTiles() != sourceImage.getNumXTiles()");
            }

            if (maskImage.getNumYTiles() != sourceImage.getNumYTiles()) {
                throw new IllegalStateException("maskImage.getNumYTiles() != sourceImage.getNumYTiles()");
            }

            if (maskImage.getTileWidth() != sourceImage.getTileWidth()) {
                throw new IllegalStateException("maskImage.getTileWidth() != sourceImage.getTileWidth()");
            }

            if (maskImage.getTileHeight() != sourceImage.getTileHeight()) {
                throw new IllegalStateException("maskImage.getTileHeight() != sourceImage.getTileHeight()");
            }
        } else {
            maskAccessor = null;
            maskSampleModel = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValidatingIterator<N> iterator()
    {
        if (dataType == Double.class)
            return (ValidatingIterator<N>) new DoubleIterator();
        else if (dataType == Float.class)
            return (ValidatingIterator<N>) new FloatIterator();
        else if (dataType == Integer.class)
            return (ValidatingIterator<N>) new IntegerIterator();
        else if (dataType == Short.class)
            return (ValidatingIterator<N>) new ShortIterator();
        else
            return (ValidatingIterator<N>) new ByteIterator();
    }

    @Override
    public ValidatingIterator<NumericType<N>> numericTypeIterator()
    {
        return new AbstractIterator<NumericType<N>>() {
            private final AbstractIterator<? extends NumericType<?>> it = createIterator();

            @SuppressWarnings("unchecked")
            @Override
            public NumericType<N> next()
            {
                return (NumericType<N>) it.next();
            }

            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            private AbstractIterator<? extends NumericType<?>> createIterator()
            {
                if (dataType == Double.class)
                    return new DoubleNumericTypeIterator(precision);
                else if (dataType == Float.class)
                    return new FloatNumericTypeIterator(precision);
                else if (dataType == Long.class)
                    return new LongNumericTypeIterator();
                else if (dataType == Integer.class)
                    return new IntegerNumericTypeIterator();
                else if (dataType == Short.class)
                    return new ShortNumericTypeIterator();
                else
                    return new ByteNumericTypeIterator();
            }

            @Override
            public void skip(int n)
            {
                it.skip(n);
            }

            @Override
            public boolean isLastReturnedValid()
            {
                return it.isLastReturnedValid();
            }

        };
    }

    /**
     * @return The source band.
     */
    public RasterDataNode getBand()
    {
        return band;
    }

    @Override
    public int size()
    {
        return imageRect.width * imageRect.height;
    }

    @Override
    public String getName()
    {
        if (band instanceof VirtualBand)
            return ((VirtualBand) band).getExpression();
        return band.getName();
    }

    /**
     * Create the data source out of the given band.
     * 
     * @param band The band to use as the source for this data source.
     * @param min The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @param max The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @return The data source.
     */
    public static BandDataSource<?> createForBand(RasterDataNode band, Number min, Number max)
    {
        return createForBand(band, null, min, max);
    }

    /**
     * Create the data source out of the given band.
     * 
     * @param band The band to use as the source for this data source.
     * @param precision The number of valid decimal digits for decimal numbers comparison.
     * @param min The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @param max The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * 
     * @return The data source.
     */
    public static BandDataSource<?> createForBand(RasterDataNode band, Integer precision, Number min, Number max)
    {
        Guardian.assertNotNull("band", band);
        switch (band.getGeophysicalDataType()) {
            case ProductData.TYPE_INT8:
            case ProductData.TYPE_UINT8:
                return new BandDataSource<Byte>(band, Byte.class, castToType((byte) 0, min), castToType((byte) 0, max));
            case ProductData.TYPE_INT16:
            case ProductData.TYPE_UINT16:
                return new BandDataSource<Short>(band, Short.class, castToType((short) 0, min), castToType((short) 0,
                        max));
            case ProductData.TYPE_INT32:
                return new BandDataSource<Integer>(band, Integer.class, castToType(0, min), castToType(0, max));
            case ProductData.TYPE_UINT32:
                return new BandDataSource<Long>(band, Long.class, castToType(0L, min), castToType(0L, max));
            case ProductData.TYPE_FLOAT32:
                return (precision != null ? new BandDataSource<Float>(band, Float.class, precision,
                        castToType(0f, min), castToType(0f, max)) : new BandDataSource<Float>(band, Float.class,
                        castToType(0f, min), castToType(0f, max)));
            case ProductData.TYPE_FLOAT64:
                return (precision != null ? new BandDataSource<Double>(band, Double.class, precision, castToType(0d,
                        min), castToType(0d, max)) : new BandDataSource<Double>(band, Double.class,
                        castToType(0d, min), castToType(0d, max)));
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isCompatible(DataSource<?> other)
    {
        if (!(other instanceof BandDataSource<?>))
            return size() == other.size();

        final BandDataSource<?> otherDataSource = (BandDataSource<?>) other;

        if (band.getSceneRasterWidth() != otherDataSource.getBand().getSceneRasterWidth())
            return false;

        if (band.getSceneRasterHeight() != otherDataSource.getBand().getSceneRasterHeight())
            return false;

        final RenderedImage mySource = sourceImage;
        final RenderedImage otherSource = otherDataSource.sourceImage;

        if (mySource.getTileGridXOffset() != otherSource.getTileGridXOffset())
            return false;

        if (mySource.getTileGridYOffset() != otherSource.getTileGridYOffset())
            return false;

        if (mySource.getNumXTiles() != otherSource.getNumXTiles())
            return false;

        if (mySource.getNumYTiles() != otherSource.getNumYTiles())
            return false;

        if (mySource.getTileWidth() != otherSource.getTileWidth())
            return false;

        if (mySource.getTileHeight() != otherSource.getTileHeight())
            return false;

        return true;
    }

    @Override
    public N getDefinedMin()
    {
        return definedMin;
    }

    @Override
    public N getDefinedMax()
    {
        return definedMax;
    }

    /**
     * An iterator not supporting the remove operation.
     * 
     * @author Martin Pecka
     * @param <T> Type of the items to iterate over.
     */
    protected abstract class AbstractIterator<T> implements SkippableIterator<T>, ValidatingIterator<T>
    {
        @Override
        public final void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Iterator allowing to iterate over 2-dimensional band data.
     * 
     * @author Martin Pecka
     * @param <T> Type of the data to iterate over.
     */
    protected abstract class AbstractNumberIterator<T extends Number> extends AbstractIterator<T>
    {
        protected final int                tileWidth        = sourceImage.getTileWidth(), tileHeight = sourceImage
                                                                    .getTileHeight(),
                tileSize = tileWidth * tileHeight;
        protected final int                numTilesX        = tileX2 - tileX1 + 1, numTilesY = tileY2 - tileY1 + 1;
        protected final int                maxIndex         = tileSize * numTilesX * numTilesY - 1;
        protected final Double             noDataValue      = (band.isNoDataValueUsed()) ? null : band.getNoDataValue();

        protected final TileDataHandler<?> tileDataHandler  = createTileDataHandler();

        protected Raster                   tile             = null;
        protected boolean                  tileContainsData = false;
        protected UnpackedImageData        tileData         = null;
        protected Rectangle                tileInterestRect = null;

        protected Raster                   maskTile         = null;
        protected UnpackedImageData        maskData         = null;
        protected byte[]                   mask             = null;

        protected int                      dataPixelStride, dataLineStride, dataBandOffset;
        protected int                      maskPixelStride, maskLineStride, maskBandOffset;

        protected int                      tileX            = tileX1 - 1, tileY = tileY1 - 1, inTileX = -1,
                inTileY = -1;
        protected int                      index            = -1;

        protected T                        last             = null;

        private void updateIndex(int newIndex) throws NoSuchElementException
        {
            if (newIndex == index)
                return;

            if (newIndex > maxIndex)
                throw new NoSuchElementException();

            index = newIndex;
            final int oldTileX = tileX, oldTileY = tileY;

            // we don't need to do a modulus by tileSize, because it is a multiple of tileWidth
            inTileX = newIndex % tileWidth;
            inTileY = (newIndex % tileSize) / tileWidth;

            final int tileIndex = newIndex / tileSize;
            tileX = tileX1 + tileIndex % numTilesX;
            tileY = tileY1 + tileIndex / numTilesX;

            if (oldTileX != tileX || oldTileY != tileY) {
                if (maskShape != null) {
                    final Rectangle dataRect = planarImage.getTileRect(tileX, tileY);
                    tileContainsData = maskShape.intersects(dataRect);
                } else {
                    tileContainsData = true;
                }

                if (tileContainsData) {
                    tile = sourceImage.getTile(tileX, tileY);
                    maskTile = maskImage != null ? maskImage.getTile(tileX, tileY) : null;
                    tileInterestRect = imageRect.intersection(tile.getBounds());
                    tileData = dataAccessor.getPixels(tile, tileInterestRect, dataAccessor.sampleType, false);
                    dataPixelStride = tileData.pixelStride;
                    dataLineStride = tileData.lineStride;
                    dataBandOffset = tileData.bandOffsets[0];
                    if (maskTile != null) {
                        maskData = maskAccessor.getPixels(maskTile, tileInterestRect, DataBuffer.TYPE_BYTE, false);
                        mask = maskData.getByteData(0);
                        maskPixelStride = maskData.pixelStride;
                        maskLineStride = maskData.lineStride;
                        maskBandOffset = maskData.bandOffsets[0];
                    } else {
                        maskData = null;
                        mask = null;
                        maskPixelStride = -1;
                        maskLineStride = -1;
                        maskBandOffset = -1;
                    }
                    loadTileData();
                } else {
                    tile = null;
                    maskTile = null;
                    tileInterestRect = null;
                    tileData = null;
                    dataPixelStride = -1;
                    dataLineStride = -1;
                    dataBandOffset = -1;
                    maskData = null;
                    mask = null;
                    maskPixelStride = -1;
                    maskLineStride = -1;
                    maskBandOffset = -1;
                }
            }
        }

        @Override
        public T next()
        {
            updateIndex(index + 1);

            return computeNext();
        }

        protected T computeNext()
        {
            final int dataPixelOffset = dataBandOffset + inTileY * dataLineStride + inTileX * dataPixelStride;
            final int maskPixelOffset = maskBandOffset + inTileY * maskLineStride + inTileX * maskPixelStride;

            if ((mask == null || mask[maskPixelOffset] != 0) && tileContainsData && tile != null) {
                return last = getTileData(dataPixelOffset);
            } else {
                return last = null;
            }
        }

        private TileDataHandler<?> createTileDataHandler()
        {
            switch (band.getDataType()) {
                case ProductData.TYPE_INT8:
                    return new ByteTileDataHandler();
                case ProductData.TYPE_UINT8:
                    return new UByteTileDataHandler();
                case ProductData.TYPE_INT16:
                    return new ShortTileDataHandler();
                case ProductData.TYPE_UINT16:
                    return new UShortTileDataHandler();
                case ProductData.TYPE_INT32:
                    return new IntTileDataHandler();
                case ProductData.TYPE_UINT32:
                    return new UIntTileDataHandler();
                case ProductData.TYPE_FLOAT32:
                    return new FloatTileDataHandler();
                case ProductData.TYPE_FLOAT64:
                    return new DoubleTileDataHandler();
                default:
                    throw new IllegalArgumentException();
            }
        }

        protected abstract T getTileData(int pixelOffset);

        protected abstract void loadTileData();

        @Override
        public boolean hasNext()
        {
            return index < maxIndex;
        }

        @Override
        public void skip(int n)
        {
            index += n; // we don't need to load any data, the next call to next() will do it
            if (index > maxIndex)
                throw new NoSuchElementException();
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return last != null;
        }

    }

    private abstract class TileDataHandler<D extends Number>
    {
        protected abstract D getTileData(int pixelOffset);

        protected abstract void loadTileData(UnpackedImageData tileData);
    }

    private class DoubleTileDataHandler extends TileDataHandler<Double>
    {
        private double[] buffer = null;

        @Override
        protected Double getTileData(int pixelOffset)
        {
            return buffer[pixelOffset];
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getDoubleData(0);
        }
    }

    private class FloatTileDataHandler extends TileDataHandler<Float>
    {
        private float[] buffer = null;

        @Override
        protected Float getTileData(int pixelOffset)
        {
            return buffer[pixelOffset];
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getFloatData(0);
        }
    }

    private class UIntTileDataHandler extends TileDataHandler<Long>
    {
        private int[] buffer = null;

        @Override
        protected Long getTileData(int pixelOffset)
        {
            return buffer[pixelOffset] & 0xffffffffL;
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getIntData(0);
        }
    }

    private class IntTileDataHandler extends TileDataHandler<Integer>
    {
        private int[] buffer = null;

        @Override
        protected Integer getTileData(int pixelOffset)
        {
            return buffer[pixelOffset];
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getIntData(0);
        }
    }

    private class UShortTileDataHandler extends TileDataHandler<Integer>
    {
        private short[] buffer = null;

        @Override
        protected Integer getTileData(int pixelOffset)
        {
            return (int) (buffer[pixelOffset] & 0xffff);
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getShortData(0);
        }
    }

    private class ShortTileDataHandler extends TileDataHandler<Short>
    {
        private short[] buffer = null;

        @Override
        protected Short getTileData(int pixelOffset)
        {
            return buffer[pixelOffset];
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getShortData(0);
        }
    }

    private class UByteTileDataHandler extends TileDataHandler<Short>
    {
        private byte[] buffer = null;

        @Override
        protected Short getTileData(int pixelOffset)
        {
            return (short) (buffer[pixelOffset] & 0xff);
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getByteData(0);
        }
    }

    private class ByteTileDataHandler extends TileDataHandler<Byte>
    {
        private byte[] buffer = null;

        @Override
        protected Byte getTileData(int pixelOffset)
        {
            return (byte) buffer[pixelOffset];
        }

        @Override
        protected void loadTileData(UnpackedImageData tileData)
        {
            buffer = tileData.getByteData(0);
        }
    }

    private class DoubleIterator extends AbstractNumberIterator<Double>
    {
        @Override
        protected Double getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return band.scale(value);
                else
                    return value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }
    };

    private class FloatIterator extends AbstractNumberIterator<Float>
    {
        @Override
        protected Float getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return (float) band.scale(value);
                else
                    return (float) value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }

    };

    private class LongIterator extends AbstractNumberIterator<Long>
    {
        @Override
        protected Long getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return (long) band.scale(value);
                else
                    return (long) value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }

    };

    private class IntegerIterator extends AbstractNumberIterator<Integer>
    {
        @Override
        protected Integer getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return (int) band.scale(value);
                else
                    return (int) value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }

    };

    private class ShortIterator extends AbstractNumberIterator<Short>
    {
        @Override
        protected Short getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return (short) band.scale(value);
                else
                    return (short) value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }
    };

    private class ByteIterator extends AbstractNumberIterator<Byte>
    {
        @Override
        protected Byte getTileData(int pixelOffset)
        {
            final double value = tileDataHandler.getTileData(pixelOffset).doubleValue();
            if ((noDataValue == null || value != noDataValue) && (min == null || value >= min)
                    && (max == null || value <= max)) {
                if (band.isScalingApplied())
                    return (byte) band.scale(value);
                else
                    return (byte) value;
            }
            return null;
        }

        @Override
        protected void loadTileData()
        {
            tileDataHandler.loadTileData(tileData);
        }

    };

    private class DoubleNumericTypeIterator extends AbstractIterator<DoubleType>
    {
        private final DoubleIterator it = new DoubleIterator();
        private final int            precision;

        public DoubleNumericTypeIterator(int precision)
        {
            this.precision = precision;
        }

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public DoubleType next()
        {
            final Double next = it.next();
            return next != null ? new DoubleType(next, precision) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }

    }

    private class FloatNumericTypeIterator extends AbstractIterator<FloatType>
    {
        private final FloatIterator it = new FloatIterator();
        private final int           precision;

        public FloatNumericTypeIterator(int precision)
        {
            this.precision = precision;
        }

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public FloatType next()
        {
            final Float next = it.next();
            return next != null ? new FloatType(next, precision) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }
    }

    private class LongNumericTypeIterator extends AbstractIterator<LongType>
    {
        private final LongIterator it = new LongIterator();

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public LongType next()
        {
            final Long next = it.next();
            return next != null ? new LongType(next) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }
    }

    private class IntegerNumericTypeIterator extends AbstractIterator<IntType>
    {
        private final IntegerIterator it = new IntegerIterator();

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public IntType next()
        {
            final Integer next = it.next();
            return next != null ? new IntType(next) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }
    }

    private class ShortNumericTypeIterator extends AbstractIterator<ShortType>
    {
        private final ShortIterator it = new ShortIterator();

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public ShortType next()
        {
            final Short next = it.next();
            return next != null ? new ShortType(next) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }
    }

    private class ByteNumericTypeIterator extends AbstractIterator<ByteType>
    {
        private final ByteIterator it = new ByteIterator();

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public ByteType next()
        {
            final Byte next = it.next();
            return next != null ? new ByteType(next) : null;
        }

        @Override
        public void skip(int n)
        {
            it.skip(n);
        }

        @Override
        public boolean isLastReturnedValid()
        {
            return it.isLastReturnedValid();
        }
    }
}
