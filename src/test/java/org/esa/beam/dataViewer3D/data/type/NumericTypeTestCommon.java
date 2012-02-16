/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Random;

/**
 * 
 * 
 * @author Martin Pecka
 * @param <T>
 */
public abstract class NumericTypeTestCommon<T extends Number>
{
    protected NumericType<T> getInstance(T value)
    {
        return getInstance(value, null);
    }

    protected abstract NumericType<T> getInstance(T value, Integer precision);

    protected abstract T[] getRandomValues(int count);

    protected void testConstructor(T... values)
    {
        testConstructor(values, getNullPrecisions(values.length));
    }

    protected void testConstructor(T[] values, Integer[] precisions)
    {
        for (int i = 0; i < values.length; i++)
            assertNotNull(getInstance(values[i], precisions[i]));
    }

    /**
     * 
     * 
     * @param requireInjective If true, it is required that the hashCode values for different values are also different.
     * @param values
     */
    protected void testHashCode(boolean requireInjective, T... values)
    {
        testHashCode(requireInjective, values, getNullPrecisions(values.length));
    }

    /**
     * 
     * 
     * @param requireInjective If true, it is required that the hashCode values for different values are also different.
     * @param values
     * @param precisions
     */
    protected void testHashCode(boolean requireInjective, T[] values, Integer[] precisions)
    {
        for (int i = 0; i < values.length; i++)
            assertEquals("hashCode() differs for equal values", getInstance(values[i], precisions[i]).hashCode(),
                    getInstance(values[i], precisions[i]).hashCode());

        if (requireInjective) {
            for (int i = 0; i < values.length - 1; i++) {
                if (values[i] != values[i + 1])
                    assertNotSame("Equal hashcodes for nonequal values found.", getInstance(values[i], precisions[i])
                            .hashCode(), getInstance(values[i + 1], precisions[i + 1]).hashCode());
            }

            final int testSize = 255;

            T[] randomValues = getRandomValues(2 * testSize);
            Integer[] randPrecisions = new Integer[2 * testSize];
            Random rand = new Random();
            for (int i = 0; i < 2 * testSize; i++)
                randPrecisions[i] = rand.nextDouble() > 0.1 ? rand.nextInt() : null;

            for (int i = 0; i < testSize; i++) {
                if (randomValues[2 * i] != randomValues[2 * i + 1])
                    assertNotSame("Equal hashcodes for nonequal values found.",
                            getInstance(randomValues[2 * i], randPrecisions[2 * i]).hashCode(),
                            getInstance(randomValues[2 * i + 1], randPrecisions[2 * i + 1]).hashCode());
            }
        }
    }

    protected void testGetNumber(T... values)
    {
        testGetNumber(values, getNullPrecisions(values.length));
    }

    protected void testGetNumber(T[] values, Integer[] precisions)
    {
        for (int i = 0; i < values.length; i++)
            assertEquals("getNumber() returned bad value", values[i], getInstance(values[i], precisions[i]).getNumber());
    }

    public void testEqualsObject(T[] sameValues, T[] differentValues, Object[] otherTypeValues)
    {
        testEqualsObject(sameValues, getNullPrecisions(sameValues.length), differentValues,
                getNullPrecisions(differentValues.length), otherTypeValues, getNullPrecisions(otherTypeValues.length));
    }

    @SuppressWarnings({ "unchecked" })
    public void testEqualsObject(T[] sameValues, Integer[] samePrecisions, T[] differentValues,
            Integer[] differentPrecisions, Object[] otherTypeValues, Integer[] otherTypePrecisions)
    {
        for (int i = 0; i < sameValues.length; i++)
            assertEquals("Equal objects claimed not equal.", getInstance(sameValues[i], samePrecisions[i]),
                    getInstance(sameValues[i], samePrecisions[i]));

        for (int i = 0; i < differentValues.length / 2; i++)
            assertNotSame("Unequal objects claimed equal.",
                    getInstance(differentValues[2 * i], differentPrecisions[2 * i]),
                    getInstance(differentValues[2 * i + 1], differentPrecisions[2 * i + 1]));

        for (int i = 0; i < otherTypeValues.length / 2; i++)
            assertNotSame("Unequal objects claimed equal.",
                    getInstance((T) otherTypeValues[2 * i], otherTypePrecisions[i]), otherTypeValues[2 * i + 1]);
    }

    private Integer[] getNullPrecisions(int length)
    {
        final Integer[] result = new Integer[length];
        for (int i = 0; i < length; i++)
            result[i] = null;
        return result;
    }
}
