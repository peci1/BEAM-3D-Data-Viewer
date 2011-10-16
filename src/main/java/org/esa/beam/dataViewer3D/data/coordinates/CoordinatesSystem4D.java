/**
 * 
 */
package org.esa.beam.dataViewer3D.data.coordinates;

import org.esa.beam.dataViewer3D.data.axis.Axis;
import org.esa.beam.dataViewer3D.data.grid.Grid;

/**
 * A 4D coordinates system.
 * 
 * @author Martin Pecka
 * 
 * @param <X> Type of the values of the x coordinate.
 * @param <Y> Type of the values of the y coordinate.
 * @param <Z> Type of the values of the z coordinate.
 * @param <W> Type of the values of the w coordinate.
 */
public class CoordinatesSystem4D<X extends Number, Y extends Number, Z extends Number, W extends Number> extends
        CoordinatesSystem
{

    /** X axis. */
    private Axis<X> axisX;
    /** Y axis. */
    private Axis<Y> axisY;
    /** Z axis. */
    private Axis<Z> axisZ;
    /** Z axis. */
    private Axis<W> axisW;

    /**
     * Create a 4D coordinate system from the given aces.
     * 
     * @param x The x axis.
     * @param y The y axis.
     * @param z The z axis.
     * @param w The w axis.
     */
    protected CoordinatesSystem4D(Axis<X> x, Axis<Y> y, Axis<Z> z, Axis<W> w)
    {
        this(x, y, z, w, null);
    }

    /**
     * Create a 4D coordinate system from the given aces and with the given grid.
     * 
     * @param x The x axis.
     * @param y The y axis.
     * @param z The z axis.
     * @param w The w axis.
     * @param grid The grid to use (may be <code>null</code>).
     */
    protected CoordinatesSystem4D(Axis<X> x, Axis<Y> y, Axis<Z> z, Axis<W> w, Grid grid)
    {
        super(grid);
        axisX = x;
        axisY = y;
        axisZ = z;
        axisW = w;
    }

    /**
     * Return the x axis.
     * 
     * @return The x axis.
     */
    public Axis<X> getXAxis()
    {
        return axisX;
    }

    /**
     * Return the y axis.
     * 
     * @return The y axis.
     */
    public Axis<Y> getYAxis()
    {
        return axisY;
    }

    /**
     * Return the z axis.
     * 
     * @return The z axis.
     */
    public Axis<Z> getZAxis()
    {
        return axisZ;
    }

    /**
     * Return the w axis.
     * 
     * @return The w axis.
     */
    public Axis<W> getWAxis()
    {
        return axisW;
    }

    /**
     * Set the x axis.
     * 
     * @param axis The new axis to set.
     * @return <code>this</code> - provides fluent interface.
     */
    public CoordinatesSystem setXAxis(Axis<X> axis)
    {
        axisX = axis;
        return this;
    }

    /**
     * Set the y axis.
     * 
     * @param axis The new axis to set.
     * @return <code>this</code> - provides fluent interface.
     */
    public CoordinatesSystem setYAxis(Axis<Y> axis)
    {
        axisY = axis;
        return this;
    }

    /**
     * Set the z axis.
     * 
     * @param axis The new axis to set.
     * @return <code>this</code> - provides fluent interface.
     */
    public CoordinatesSystem setZAxis(Axis<Z> axis)
    {
        axisZ = axis;
        return this;
    }

    /**
     * Set the w axis.
     * 
     * @param axis The new axis to set.
     * @return <code>this</code> - provides fluent interface.
     */
    public CoordinatesSystem setWAxis(Axis<W> axis)
    {
        axisW = axis;
        return this;
    }

    @Override
    public String toString()
    {
        return new StringBuilder("4D coordinates system:").append("\nX: ").append(axisX).append("\nY: ").append(axisY)
                .append("\nZ: ").append(axisZ).append("\nW: ").append(axisW).toString();
    }

    @Override
    public Axis<?>[] getAces()
    {
        return new Axis<?>[] { axisX, axisY, axisZ, axisW };
    }
}