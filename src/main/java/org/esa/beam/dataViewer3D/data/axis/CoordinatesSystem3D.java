/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

/**
 * A 3D coordinates system.
 * 
 * @author Martin Pecka
 * 
 * @param <X> Type of the values of the x coordinate.
 * @param <Y> Type of the values of the y coordinate.
 * @param <Z> Type of the values of the z coordinate.
 */
public class CoordinatesSystem3D<X extends Number, Y extends Number, Z extends Number> extends CoordinatesSystem
{

    /** X axis. */
    private Axis<X> axisX;
    /** Y axis. */
    private Axis<Y> axisY;
    /** Z axis. */
    private Axis<Z> axisZ;

    /**
     * Create a 3D coordinate system from the given aces.
     * 
     * @param x The x axis.
     * @param y The y axis.
     * @param z The z axis.
     */
    protected CoordinatesSystem3D(Axis<X> x, Axis<Y> y, Axis<Z> z)
    {
        axisX = x;
        axisY = y;
        axisZ = z;
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

    @Override
    public String toString()
    {
        return new StringBuilder("3D coordinates system:").append("\nX: ").append(axisX).append("\nY: ").append(axisY)
                .append("\nZ: ").append(axisZ).toString();
    }

    @Override
    public Axis<?>[] getAces()
    {
        return new Axis<?>[] { axisX, axisY, axisZ };
    }
}