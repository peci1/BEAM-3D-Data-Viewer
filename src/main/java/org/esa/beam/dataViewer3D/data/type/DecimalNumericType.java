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

    /**
     * The precision of the number. Defines the number of digits after the decimal point to take into account.
     * <p>
     * Negative values mean that you want to do the "rounding" to the left from the decimal point, eg. precision -2 will
     * treat 1234 as 1200.
     * <p>
     * <code>null</code> values mean no explicit rounding is done and the precision is bounded only by the data type.
     */
    private final Integer precision;

    /** The value after trimming all digits outside precision and "forgetting" the decimal point. */
    private final Long    roundedValue;

    /**
     * @param number The represented number.
     * @param precision The precision of the number. Defines the number of digits after the decimal point to take into
     *            account. Negative values mean that you want to do the "rounding" to the left from the decimal point,
     *            eg. precision -2 will treat 1234 as 1200. <code>null</code> values mean no explicit rounding is done
     *            and the precision is bounded only by the data type.
     */
    protected DecimalNumericType(N number, Integer precision)
    {
        super(number);
        this.precision = precision;
        roundedValue = computeRoundedValue();
    }

    /**
     * Compute the value after trimming all digits outside precision and "forgetting" the decimal point.
     * 
     * @return The value after trimming all digits outside precision and "forgetting" the decimal point.
     */
    protected abstract Long computeRoundedValue();

    /**
     * The precision of the number. Defines the number of digits after the decimal point to take into account.
     * <p>
     * Negative values mean that you want to do the "rounding" to the left from the decimal point, eg. precision -2 will
     * treat 1234 as 1200.
     * <p>
     * <code>null</code> values mean no explicit rounding is done and the precision is bounded only by the data type.
     * 
     * @return The precision of the number.
     */
    public Integer getPrecision()
    {
        return precision;
    }

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
            builder.append(getNumber() + "(infinite precision)");
        return builder.toString();
    }
}
