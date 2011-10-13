/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * An unmodifiable 4D data point.
 * 
 * @author Martin Pecka
 * @param <X> The type of this point's x coordinate data.
 * @param <Y> The type of this point's y coordinate data.
 * @param <Z> The type of this point's z coordinate data.
 * @param <W> The type of this point's w coordinate data.
 */
public final class SimpleDataPoint4D<X extends NumericType<?>, Y extends NumericType<?>, Z extends NumericType<?>, W extends NumericType<?>>
        implements DataPoint4D<X, Y, Z, W>
{

    private final X x;
    private final Y y;
    private final Z z;
    private final W w;

    /**
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     * @param w The w value.
     */
    public SimpleDataPoint4D(X x, Y y, Z z, W w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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
    public W getW()
    {
        return w;
    }

    @Override
    public byte getDimensions()
    {
        return 4;
    }

    @Override
    public int hashCode()
    {
        final int prime = 16777619;
        int result = -2128831035;
        result = prime * result + ((w == null) ? 0 : w.hashCode());
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        result = prime * result + ((z == null) ? 0 : z.hashCode());
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
        SimpleDataPoint4D<?, ?, ?, ?> other = (SimpleDataPoint4D<?, ?, ?, ?>) obj;
        if (w == null) {
            if (other.w != null)
                return false;
        } else if (!w.equals(other.w))
            return false;
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
        builder.append("SimpleDataPoint4D [x=").append(x).append(", y=").append(y).append(", z=").append(z)
                .append(", w=").append(w).append("]");
        return builder.toString();
    }
}
