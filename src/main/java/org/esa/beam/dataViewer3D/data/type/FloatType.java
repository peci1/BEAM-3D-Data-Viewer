/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

/**
 * A <code>float</code> wrapper class.
 * 
 * @author Martin Pecka
 */
public class FloatType extends DecimalNumericType<Float>
{

    /**
     * @param number The represented number.
     * @param precision The precision of the number. Defines the number of digits after the decimal point to take into
     *            account.
     */
    public FloatType(Float number, Integer precision)
    {
        super(number, precision);
    }

    @Override
    protected Long computeRoundedValue()
    {
        return Math.round(getNumber() * Math.pow(10, getPrecision()));
    }

}
