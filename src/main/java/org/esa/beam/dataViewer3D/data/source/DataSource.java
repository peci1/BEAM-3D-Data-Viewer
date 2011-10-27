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

    @Override
    ValidatingIterator<N> iterator();

    /**
     * Return an iterator that iterates the input data as {@link NumericType}s.
     * 
     * @return An iterator that iterates the input data as {@link NumericType}s.
     */
    ValidatingIterator<NumericType<N>> numericTypeIterator();

}
