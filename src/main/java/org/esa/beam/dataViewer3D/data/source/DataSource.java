/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.type.NumericType;

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
     * Return an iterator that iterates the input data as {@link NumericType}s.
     * 
     * @return An iterator that iterates the input data as {@link NumericType}s.
     */
    Iterator<NumericType<N>> numericTypeIterator();

}
