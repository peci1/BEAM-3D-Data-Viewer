/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.io.IOException;
import java.util.Iterator;

import javax.help.UnsupportedOperationException;

import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.data.type.ShortType;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.ProductData;
import org.esa.beam.util.Guardian;
import org.esa.beam.visat.VisatApp;

/**
 * A data source taking the input data from a band.
 * 
 * @author Martin Pecka
 * @param <N> Type of the band values.
 */
public class BandDataSource<N extends Number> implements DataSource<N>
{

    /** The source band. */
    protected final Band                    band;

    /** The type of the data this class returns. */
    protected final Class<? extends Number> dataType;

    /** The precision for decimal type bands. */
    protected final Integer                 precision;

    protected BandDataSource(Band band, Class<N> dataType)
    {
        this.band = band;
        this.dataType = dataType;
        this.precision = 10;
    }

    protected BandDataSource(Band band, Class<N> dataType, int precision)
    {
        this.band = band;
        this.dataType = dataType;
        this.precision = precision;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<N> iterator()
    {
        if (!band.hasRasterData()) {
            try {
                band.loadRasterData();
            } catch (IOException e) {
                VisatApp.getApp().getLogger().throwing(getClass().getName(), "iterator", e);
            }
        }

        if (dataType == Double.class)
            return (Iterator<N>) new DoubleIterator();
        else if (dataType == Float.class)
            return (Iterator<N>) new FloatIterator();
        else if (dataType == Integer.class)
            return (Iterator<N>) new IntegerIterator();
        else if (dataType == Short.class)
            return (Iterator<N>) new ShortIterator();
        else
            return (Iterator<N>) new ByteIterator();
    }

    @Override
    public Iterator<NumericType<N>> numericTypeIterator()
    {
        if (!band.hasRasterData()) {
            try {
                band.loadRasterData();
            } catch (IOException e) {
                VisatApp.getApp().getLogger().throwing(getClass().getName(), "numericTypeIterator", e);
            }
        }

        return new AbstractIterator<NumericType<N>>() {
            private final Iterator<? extends NumericType<?>> it = createIterator();

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

            private Iterator<? extends NumericType<?>> createIterator()
            {
                if (dataType == Double.class)
                    return new DoubleNumericTypeIterator(precision);
                else if (dataType == Float.class)
                    return new FloatNumericTypeIterator(precision);
                else if (dataType == Integer.class)
                    return new IntegerNumericTypeIterator();
                else if (dataType == Short.class)
                    return new ShortNumericTypeIterator();
                else
                    return new ByteNumericTypeIterator();
            }
        };
    }

    @Override
    public int size()
    {
        return (int) band.getNumDataElems();
    }

    /**
     * Create the data source out of the given band.
     * 
     * @param band The band to use as the source for this data source.
     * @return The data source.
     */
    public static BandDataSource<?> createForBand(Band band)
    {
        return createForBand(band, null);
    }

    /**
     * Create the data source out of the given band.
     * 
     * @param band The band to use as the source for this data source.
     * @param precision If the band is decimal, defines the precision of comparisons (in the number of decimal digits).
     * @return The data source.
     */
    public static BandDataSource<?> createForBand(Band band, Integer precision)
    {
        Guardian.assertNotNull("band", band);
        switch (band.getDataType()) {
            case ProductData.TYPE_INT8:
                return new BandDataSource<Byte>(band, Byte.class);
            case ProductData.TYPE_UINT8:
            case ProductData.TYPE_INT16:
                return new BandDataSource<Short>(band, Short.class);
            case ProductData.TYPE_UINT16:
            case ProductData.TYPE_INT32:
                return new BandDataSource<Integer>(band, Integer.class);
            case ProductData.TYPE_UINT32:
                return new BandDataSource<Long>(band, Long.class);
            case ProductData.TYPE_FLOAT32:
                return (precision != null ? new BandDataSource<Float>(band, Float.class, precision)
                        : new BandDataSource<Float>(band, Float.class));
            case ProductData.TYPE_FLOAT64:
                return (precision != null ? new BandDataSource<Double>(band, Double.class, precision)
                        : new BandDataSource<Double>(band, Double.class));
            default:
                throw new IllegalArgumentException();
        }
    }

    protected abstract class AbstractIterator<T> implements Iterator<T>
    {
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    protected abstract class AbstractNumberIterator<T> extends AbstractIterator<T>
    {
        protected final int width = band.getRasterWidth(), height = band.getRasterHeight();
        protected final int size  = width * height;
        // this setting of i and j is to load the first row of data
        protected int       i     = 0, j = width;

        @Override
        public boolean hasNext()
        {
            if (height == i && j + 1 == width)
                return false;
            return true;
        }
    }

    private class DoubleIterator extends AbstractNumberIterator<Double>
    {
        final double[] buffer = new double[width];

        @Override
        public Double next()
        {
            if (j >= buffer.length) {
                band.getPixels(0, i++, width, 1, buffer);
                j = 0;
            }
            return buffer[j++];
        }

    };

    private class FloatIterator extends AbstractNumberIterator<Float>
    {
        final float[] buffer = new float[width];

        @Override
        public Float next()
        {
            if (j >= buffer.length) {
                band.getPixels(0, i++, width, 1, buffer);
                j = 0;
            }
            return buffer[j++];
        }

    };

    private class IntegerIterator extends AbstractNumberIterator<Integer>
    {
        final int[] buffer = new int[width];

        @Override
        public Integer next()
        {
            if (j >= buffer.length) {
                band.getPixels(0, i++, width, 1, buffer);
                j = 0;
            }
            return buffer[j++];
        }

    };

    private class ShortIterator extends AbstractNumberIterator<Short>
    {
        final int[] buffer = new int[width];

        @Override
        public Short next()
        {
            if (j >= buffer.length) {
                band.getPixels(0, i++, width, 1, buffer);
                j = 0;
            }
            return (short) buffer[j++];
        }

    };

    private class ByteIterator extends AbstractNumberIterator<Byte>
    {
        final int[] buffer = new int[width];

        @Override
        public Byte next()
        {
            if (j >= buffer.length) {
                band.getPixels(0, i++, width, 1, buffer);
                j = 0;
            }
            return (byte) buffer[j++];
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
            return new DoubleType(it.next(), precision);
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
            return new FloatType(it.next(), precision);
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
            return new IntType(it.next());
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
            return new ShortType(it.next());
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
            return new ByteType(it.next());
        }

    }
}
