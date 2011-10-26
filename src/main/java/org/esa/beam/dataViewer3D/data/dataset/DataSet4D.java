/**
 * 
 */
package org.esa.beam.dataViewer3D.data.dataset;

import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * A set of 4D data points.
 * 
 * @author Martin Pecka
 * 
 * @param <X> Type of the values of the x coordinate.
 * @param <Y> Type of the values of the y coordinate.
 * @param <Z> Type of the values of the z coordinate.
 * @param <W> Type of the values of the w coordinate.
 */
public interface DataSet4D<X extends Number, Y extends Number, Z extends Number, W extends Number> extends DataSet
{
    /**
     * Return the iterator that iterates through the data points and returns them as specific 4D points.
     * 
     * @return The iterator that iterates through the data points and returns them as specific 4D points.
     */
    @Override
    Iterator<DataPoint4D<NumericType<X>, NumericType<Y>, NumericType<Z>, NumericType<W>>> pointIterator();

    /**
     * Iterator over x values.
     * 
     * @return The iterator.
     */
    Iterator<X> xIterator();

    /**
     * Iterator over y values.
     * 
     * @return The iterator.
     */
    Iterator<Y> yIterator();

    /**
     * Iterator over z values.
     * 
     * @return The iterator.
     */
    Iterator<Z> zIterator();

    /**
     * Iterator over w values.
     * 
     * @return The iterator.
     */
    Iterator<W> wIterator();

    /**
     * Return the minimum x-value in this set.
     * 
     * @return The minimum x-value in this set.
     */
    X getMinX();

    /**
     * Return the minimum y-value in this set.
     * 
     * @return The minimum y-value in this set.
     */
    Y getMinY();

    /**
     * Return the minimum z-value in this set.
     * 
     * @return The minimum z-value in this set.
     */
    Z getMinZ();

    /**
     * Return the minimum w-value in this set.
     * 
     * @return The minimum w-value in this set.
     */
    W getMinW();

    /**
     * Return the maximum x-value in this set.
     * 
     * @return The maximum x-value in this set.
     */
    X getMaxX();

    /**
     * Return the maximum y-value in this set.
     * 
     * @return The maximum y-value in this set.
     */
    Y getMaxY();

    /**
     * Return the maximum z-value in this set.
     * 
     * @return The maximum z-value in this set.
     */
    Z getMaxZ();

    /**
     * Return the maximum w-value in this set.
     * 
     * @return The maximum w-value in this set.
     */
    W getMaxW();
}
