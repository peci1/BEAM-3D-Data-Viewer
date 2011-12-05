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
     * Return the minimum value for the given dimension.
     * 
     * @param dimension The requested dimension (0 for x and so).
     * @return The minimum value of the source of the data set.
     */
    double getMin(int dimension);

    /**
     * Return the maximum value for the given dimension.
     * 
     * @param dimension The requested dimension (0 for x and so).
     * @return The maximum value of the source of the data set.
     */
    double getMax(int dimension);

    /**
     * Return the name of the source of the given dimension.
     * 
     * @param dimension The dimension to get name of.
     * @return The name of the dimension's source.
     */
    String getSourceName(int dimension);

    /**
     * Return the iterator that iterates through the data points and returns them as specific points.
     * 
     * @return The iterator that iterates through the data points and returns them as specific points.
     */
    Iterator<? extends DataPoint> pointIterator();

    /**
     * Return the iterator that iterates through the histogram entries (counts of data points with the same value) in
     * the same order as iterator() iterates over data points.
     * 
     * @return The iterator that iterates through the histogram entries (counts of data points with the same value) in
     *         the same order as iterator() iterates over data points.
     */
    Iterator<Integer> histogramIterator();
}
