/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.util.ValidatingIterator;

/**
 * Base for all data sources.
 * 
 * @author Martin Pecka
 * @param <N> Type of the data this data source provides.
 */
public abstract class AbstractDataSource<N extends Number> implements DataSource<N>
{
    /** The min and max values this source can return. If <code>null</code>, there is no restriction on min/max values. */
    protected final N definedMin, definedMax;

    /**
     * @param definedMin The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     * @param definedMax The min and max values this source can return. If <code>null</code>, there is no restriction on
     *            min/max values.
     */
    public AbstractDataSource(N definedMin, N definedMax)
    {
        this.definedMin = definedMin;
        this.definedMax = definedMax;
    }

    @Override
    public ValidatingIterator<NumericType<N>> numericTypeIterator()
    {
        return new ValidatingIterator<NumericType<N>>() {

            ValidatingIterator<N> it = iterator();

            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public NumericType<N> next()
            {
                return getNumericType(it.next());
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isLastReturnedValid()
            {
                return it.isLastReturnedValid();
            }
        };
    }

    /**
     * Convert the given number to a {@link NumericType}.
     * 
     * @param number The number to convert.
     * @return The corresponding {@link NumericType}.
     */
    protected abstract NumericType<N> getNumericType(N number);

    @Override
    public boolean isCompatible(DataSource<?> other)
    {
        return size() == other.size();
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

}
