/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * An unmutable 3D data point.
 * 
 * @author Martin Pecka
 * @param <X> The type of this point's x coordinate data.
 * @param <Y> The type of this point's y coordinate data.
 * @param <Z> The type of this point's z coordinate data.
 */
public final class SimpleDataPoint3D<X extends NumericType<?>, Y extends NumericType<?>, Z extends NumericType<?>>
        implements DataPoint3D<X, Y, Z>
{

    private final X x;
    private final Y y;
    private final Z z;

    /**
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public SimpleDataPoint3D(X x, Y y, Z z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public X getX()
    {
        return x;
    }

    @Override
    public Y getY()
    {
        return y;
    }

    @Override
    public Z getZ()
    {
        return z;
    }

    @Override
    public byte getDimensions()
    {
        return 3;
    }

    @Override
    public int hashCode()
    {
        return hashCode(x, y, z);
    }

    /**
     * Return the hash code of a data point with the given coordinates.
     * <p>
     * This method is provided for fast computing of the hashcode without creating the data point object.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The same value as <code>new SampleDataPoint3D(x,y,z).hashCode()</code> for points with the given
     *         hashcodes.
     */
    public static <X extends NumericType<?>, Y extends NumericType<?>, Z extends NumericType<?>> int hashCode(X x, Y y,
            Z z)
    {
        final int prime = 16777619;
        int result = -2128831035;
        result = prime * result + (x == null ? 0 : x.hashCode());
        result = prime * result + (y == null ? 0 : y.hashCode());
        result = prime * result + (z == null ? 0 : z.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleDataPoint3D<?, ?, ?> other = (SimpleDataPoint3D<?, ?, ?>) obj;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        if (z == null) {
            if (other.z != null)
                return false;
        } else if (!z.equals(other.z))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleDataPoint3D [x=").append(x).append(", y=").append(y).append(", z=").append(z).append("]");
        return builder.toString();
    }

}
