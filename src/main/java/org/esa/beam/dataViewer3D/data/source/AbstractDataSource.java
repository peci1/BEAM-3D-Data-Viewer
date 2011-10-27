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
}
