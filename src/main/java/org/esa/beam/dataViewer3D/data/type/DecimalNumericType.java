/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

/**
 * A decimal number that can have a defined "external" precision.
 * 
 * @author Martin Pecka
 * @param <N> The wrapper type this class represents.
 */
public abstract class DecimalNumericType<N extends Number> extends NumericType<N>
{

    /** The value after trimming all digits outside precision and "forgetting" the decimal point. */
    private final Long roundedValue;

    /**
     * @param number The represented number.
     * @param precision The precision of the number. Defines the number of digits after the decimal point to take into
     *            account.
     */
    public DecimalNumericType(N number, Integer precision)
    {
        super(number, precision);
        roundedValue = computeRoundedValue();
    }

    /**
     * Compute the value after trimming all digits outside precision and "forgetting" the decimal point.
     * 
     * @return The value after trimming all digits outside precision and "forgetting" the decimal point.
     */
    protected abstract Long computeRoundedValue();

    @Override
    public int hashCode()
    {
        return roundedValue.hashCode();
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
        DecimalNumericType<?> other = (DecimalNumericType<?>) obj;
        return roundedValue.equals(other.roundedValue);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (getPrecision() != null)
            builder.append(String.format("%+1." + getPrecision() + "f", getNumber()));
        else
            builder.append(getNumber());
        return builder.toString();
    }

}
