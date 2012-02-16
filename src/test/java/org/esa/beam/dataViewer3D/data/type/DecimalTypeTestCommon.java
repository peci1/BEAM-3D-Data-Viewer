/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import static org.junit.Assert.assertEquals;

/**
 * 
 * 
 * @author Martin Pecka
 * @param <T>
 */
public abstract class DecimalTypeTestCommon<T extends Number> extends NumericTypeTestCommon<T>
{
    @Override
    protected DecimalNumericType<T> getInstance(T value)
    {
        return getInstance(value, null);
    }

    @Override
    protected abstract DecimalNumericType<T> getInstance(T value, Integer precision);

    protected void testGetPrecision(T[] values, Integer[] precisions)
    {
        for (int i = 0; i < values.length; i++)
            assertEquals("getPrecision() returned bad value", precisions[i], getInstance(values[i], precisions[i])
                    .getPrecision());
    }
}
