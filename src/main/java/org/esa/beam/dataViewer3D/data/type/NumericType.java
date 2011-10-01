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
    private final N       number;
    /**
     * The precision of the number (only has effect for non-integral types). Defines the number of digits after the
     * decimal point to take into account.
     */
    private final Integer precision;

    /**
     * @param number The represented number.
     * @param precision The precision of the number (only has effect for non-integral types). Defines the number of
     *            digits after the
     *            decimal point to take into account.
     */
    public NumericType(N number, Integer precision)
    {
        this.number = number;
        this.precision = precision;
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

    /**
     * The precision of the number (only has effect for non-integral types). Defines the number of digits after the
     * decimal point to take into account.
     * 
     * @return The precision of the number (only has effect for non-integral types). Defines the number of digits after
     *         the decimal point to take into account.
     */
    public Integer getPrecision()
    {
        return precision;
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
        StringBuilder builder = new StringBuilder();
        builder.append(number);
        if (precision != null)
            builder.append(", precision=").append(precision);
        return builder.toString();
    }

}
