/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.util.ValidatingIterator;

/**
 * A data source providing input for data sets.
 * 
 * @author Martin Pecka
 * @param <N> The type of values read from the data source.
 */
public interface DataSource<N extends Number> extends Iterable<N>
{
    /**
     * Return the number of entries this data source provides.
     * 
     * @return The number of entries this data source provides.
     */
    int size();

    /**
     * @return The minimum value this data source should return, or <code>null</code> if the value set is unbounded from
     *         the bottom.
     */
    N getDefinedMin();

    /**
     * @return The maximum value this data source should return, or <code>null</code> if the value set is unbounded from
     *         the top.
     */
    N getDefinedMax();

    /**
     * Check whether this data source is compatible with the other data source in order to be used together to form a
     * data set.
     * 
     * @param other The other data source.
     * @return Whether this and other data sources are compatible.
     */
    boolean isCompatible(DataSource<?> other);

    /**
     * @return The name of the source.
     */
    String getName();

    @Override
    ValidatingIterator<N> iterator();

    /**
     * Return an iterator that iterates the input data as {@link NumericType}s.
     * 
     * @return An iterator that iterates the input data as {@link NumericType}s.
     */
    ValidatingIterator<NumericType<N>> numericTypeIterator();

}
