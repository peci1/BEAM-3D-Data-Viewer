/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * An unmutable 3-dimensional data point.
 * 
 * @author Martin Pecka
 * @param <X> The type of this point's x coordinate data.
 * @param <Y> The type of this point's y coordinate data.
 * @param <Z> The type of this point's z coordinate data.
 */
public interface DataPoint3D<X extends NumericType<?>, Y extends NumericType<?>, Z extends NumericType<?>> extends
        DataPoint
{
    /**
     * Return the value of the first (x) coordinate.
     * 
     * @return The value of the first (x) coordinate.
     */
    X getX();

    /**
     * Return the value of the second (y) coordinate.
     * 
     * @return The value of the second (y) coordinate.
     */
    Y getY();

    /**
     * Return the value of the third (z) coordinate.
     * 
     * @return The value of the third (z) coordinate.
     */
    Z getZ();

}
