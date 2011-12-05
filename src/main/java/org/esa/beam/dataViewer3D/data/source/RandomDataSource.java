/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.util.ArrayList;
import java.util.List;

import org.esa.beam.util.ValidatingIterator;

/**
 * Data source producing random data. Mainly for development purposes.
 * 
 * @author Martin Pecka
 * @param <N>
 */
public abstract class RandomDataSource<N extends Number> extends AbstractDataSource<N> implements DataSource<N>
{

    protected final List<N> data;
    protected final int     size;
    protected final N       min, max;

    public RandomDataSource(int size, N min, N max) throws IllegalArgumentException
    {
        super(min, max);
        if (size <= 0)
            throw new IllegalArgumentException(getClass() + ": Cannot set non-positive size.");

        if (min.doubleValue() > max.doubleValue())
            throw new IllegalArgumentException(getClass() + ": Cannot set minimum bigger than maximum.");

        data = new ArrayList<N>(size);
        this.size = size;

        this.min = min;
        this.max = max;
    }

    @Override
    public ValidatingIterator<N> iterator()
    {
        return new ValidatingIterator<N>() {
            private int i = 0;

            @Override
            public boolean hasNext()
            {
                return i < size;
            }

            @Override
            public N next()
            {
                if (data.size() - 1 <= i || data.get(i) == null)
                    data.add(getRandomValue());
                return data.get(i++);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isLastReturnedValid()
            {
                return true;
            }
        };
    }

    protected abstract N getRandomValue();

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public String getName()
    {
        return "random"; /* I18N */
    }

}
