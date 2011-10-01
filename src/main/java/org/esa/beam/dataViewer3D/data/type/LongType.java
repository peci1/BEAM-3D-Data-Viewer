/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

/**
 * A <code>long</code> wrapper class.
 * 
 * @author Martin Pecka
 */
public class LongType extends NumericType<Long>
{

    /**
     * @param number The represented number.
     */
    public LongType(Long number)
    {
        super(number, null);
    }

}
