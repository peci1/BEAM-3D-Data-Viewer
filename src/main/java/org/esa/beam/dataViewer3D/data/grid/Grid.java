/**
 * 
 */
package org.esa.beam.dataViewer3D.data.grid;

/**
 * A grid in a coordinate system.
 * 
 * @author Martin Pecka
 */
public abstract class Grid
{

    /** The array of grid lines to be drawn. */
    protected double[][] gridLines;

    /**
     * Return the array of grid lines to be drawn.
     * <p>
     * It is an array of arrays, where every second-level array has six items - three for the startpoint and three for
     * the endpoint.
     * 
     * @return The array of grid lines to be drawn.
     */
    public double[][] getGridLines()
    {
        return gridLines;
    }
}
