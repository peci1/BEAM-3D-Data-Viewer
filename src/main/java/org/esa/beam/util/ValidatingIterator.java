/**
 * 
 */
package org.esa.beam.util;

import java.util.Iterator;

/**
 * Iterator that provides a form of validation of the last returned value.
 * 
 * @author Martin Pecka
 * @param <E> Type of the values to iterate over.
 */
public interface ValidatingIterator<E> extends Iterator<E>
{
    /**
     * Return true iff the last value returned by {@link #next()} is valid.
     * 
     * @return true iff the last value returned by {@link #next()} is valid.
     * 
     * @throws IllegalStateException If this method is called before the first call to {@link #next()}.
     */
    boolean isLastReturnedValid();
}
