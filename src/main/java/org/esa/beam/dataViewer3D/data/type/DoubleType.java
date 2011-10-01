/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

/**
 * A <code>double</code> wrapper class.
 * 
 * @author Martin Pecka
 */
public class DoubleType extends DecimalNumericType<Double>
{

    /**
     * @param number The represented number.
     * @param precision The precision of the number. Defines the number of digits after the decimal point to take into
     *            account.
     */
    public DoubleType(Double number, Integer precision)
    {
        super(number, precision);
    }

    @Override
    protected Long computeRoundedValue()
    {
        return Math.round(getNumber() * Math.pow(10, getPrecision()));
    }

}
