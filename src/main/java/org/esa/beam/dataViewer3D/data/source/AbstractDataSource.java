/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * Base for all data sources.
 * 
 * @author Martin Pecka
 * @param <N> Type of the data this data source provides.
 */
public abstract class AbstractDataSource<N extends Number> implements DataSource<N>
{
    @Override
    public Iterator<NumericType<N>> numericTypeIterator()
    {
        return new Iterator<NumericType<N>>() {

            Iterator<N> it = iterator();

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
