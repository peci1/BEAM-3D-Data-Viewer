/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

/**
 * A number that can have a defined "external" precision if it isn't integer type.
 * 
 * @author Martin Pecka
 * @param <N> The wrapper type this class represents.
 */
public abstract class NumericType<N extends Number>
{
    /** The represented number. */
    private final N number;

    /**
     * @param number The represented number.
     */
    protected NumericType(N number)
    {
        this.number = number;
    }

    /**
     * The represented number.
     * 
     * @return The represented number.
     */
    public N getNumber()
    {
        return number;
    }

    @Override
    public int hashCode()
    {
        return number.hashCode();
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
        NumericType<?> other = (NumericType<?>) obj;
        return number.equals(other.number);
    }

    @Override
    public String toString()
    {
        return number.toString();
    }

}
