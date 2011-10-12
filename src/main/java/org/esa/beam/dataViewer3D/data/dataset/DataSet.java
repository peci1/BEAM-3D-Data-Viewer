/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.point.DataPoint;

/**
 * An unmutable set of unmutable data points.
 * 
 * @author Martin Pecka
 */
public interface DataSet extends Iterable<DataPoint>
{
    /**
     * Return the number of data points in this set.
     * 
     * @return The number of data points in this set.
     */
    int size();

    /**
     * Return the iterator that iterates through the histogram entries (counts of data points with the same value) in
     * the same order as iterator() iterates over data points.
     * 
     * @return The iterator that iterates through the histogram entries (counts of data points with the same value) in
     *         the same order as iterator() iterates over data points.
     */
    Iterator<Integer> histogramIterator();
}
