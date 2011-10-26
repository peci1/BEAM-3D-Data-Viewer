/**
 * 
 */
package org.esa.beam.util;

import java.util.Iterator;

/**
 * Iterator that allows to skip some number of values, which should be faster than calling {@link #next()} for unneeded
 * values.
 * 
 * @author Martin Pecka
 * @param <E> Type of the items to iterate over.
 */
public interface SkippableIterator<E> extends Iterator<E>
{
    /**
     * Skip <code>n</code> items. If the iterator should get beyond the end, no exception is thrown, but a subsequent
     * call to {@link #hasNext()} will return false.
     * 
     * @param n The number of entries to skip. If last {@link #next()} call returned the <code>i</code>-th element,
     *            calling <code>skip(1);next();</code> will return the <code>(i+2)</code>-nd element.
     */
    void skip(int n);
}
