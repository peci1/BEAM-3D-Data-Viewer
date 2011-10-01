/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

/**
 * An unmutable data point.
 * 
 * @author Martin Pecka
 */
public interface DataPoint
{
    /**
     * Return the number of dimensions of this data point.
     * 
     * @return The number of dimensions of this data point.
     */
    byte getDimensions();
}
