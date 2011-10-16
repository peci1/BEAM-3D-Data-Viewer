/**
 * 
 */
package org.esa.beam.dataViewer3D.utils;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.add;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.castToType;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.max;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.min;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.multiply;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.sub;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class NumberTypeUtilsTest
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#multiply(double, java.lang.Number)}.
     */
    @Test
    public void testMultiply()
    {
        assertEquals(3 * 4d, multiply(3, 4d), 0d);
        assertEquals(3 * 4f, multiply(3, 4f), 0f);
        assertEquals(3 * 4, (int) multiply(3, 4));
        assertEquals(3 * 4L, (long) multiply(3, 4L));
        assertEquals(3 * (short) 4, (short) multiply(3, (short) 4));
        assertEquals(3 * (byte) 4, (byte) multiply(3, (byte) 4));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#add(java.lang.Number, java.lang.Number)}.
     */
    @Test
    public void testAdd()
    {
        assertEquals(3d + 4d, add(3d, 4d), 0d);
        assertEquals(3f + 4f, add(3f, 4f), 0f);
        assertEquals(3 + 4, (int) add(3, 4));
        assertEquals(3L + 4L, (long) add(3L, 4L));
        assertEquals((short) 3 + (short) 4, (short) add((short) 3, (short) 4));
        assertEquals((byte) 3 + (byte) 4, (byte) add((byte) 3, (byte) 4));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#sub(java.lang.Number, java.lang.Number)}.
     */
    @Test
    public void testSub()
    {
        assertEquals(3d - 4d, sub(3d, 4d), 0d);
        assertEquals(3f - 4f, sub(3f, 4f), 0f);
        assertEquals(3 - 4, (int) sub(3, 4));
        assertEquals(3L - 4L, (long) sub(3L, 4L));
        assertEquals((short) 3 - (short) 4, (short) sub((short) 3, (short) 4));
        assertEquals((byte) 3 - (byte) 4, (byte) sub((byte) 3, (byte) 4));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#max(java.lang.Number[])}.
     */
    @Test
    public void testMax()
    {
        assertEquals(1024d, max(-10000d, 0d, -32d, 64d, 1024d, 100d, 6.5, Double.NEGATIVE_INFINITY));
        assertEquals(1024f, max(-10000f, 0f, -32f, 64f, 1024f, 100f, 6.5, Float.NEGATIVE_INFINITY));
        assertEquals(1024, max(-10000, 0, -32, 64, 1024, 100, Integer.MIN_VALUE));
        assertEquals(1024L, max(-10000L, 0L, -32L, 64L, 1024L, 100L, Long.MIN_VALUE));
        assertEquals((short) 1024,
                max((short) (-10000), (short) 0, (short) (-32), (short) 64, (short) 1024, (short) 100, Short.MIN_VALUE));
        assertEquals((byte) 100,
                max((byte) (-10000), (byte) 0, (byte) (-32), (byte) 64, (byte) 1024, (byte) 100, Byte.MIN_VALUE));
        assertEquals(1024f, max(-10000, (byte) 0, (short) (-32), 64d, 1024f, 100, 6.5, Float.NEGATIVE_INFINITY));
        assertEquals(100, max(-10000, (byte) 0, (short) (-32), 64d, 100, 6.5, Float.NEGATIVE_INFINITY));
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#min(java.lang.Number[])}.
     */
    @Test
    public void testMin()
    {
        assertEquals(-10000d, min(-10000d, 0d, -32d, 64d, 1024d, 100d, 6.5, Double.POSITIVE_INFINITY));
        assertEquals(-10000f, min(-10000f, 0f, -32f, 64f, 1024f, 100f, 6.5, Float.POSITIVE_INFINITY));
        assertEquals(-10000, min(-10000, 0, -32, 64, 1024, 100, Integer.MAX_VALUE));
        assertEquals(-10000L, min(-10000L, 0L, -32L, 64L, 1024L, 100L, Long.MAX_VALUE));
        assertEquals((short) (-10000),
                min((short) (-10000), (short) 0, (short) (-32), (short) 64, (short) 1024, (short) 100, Short.MAX_VALUE));
        assertEquals((byte) (-32),
                min((byte) (-10000), (byte) 0, (byte) (-32), (byte) 64, (byte) 1024, (byte) 100, Byte.MAX_VALUE));
        assertEquals(-10000, min(-10000, (byte) 0, (short) (-32), 64d, 1024f, 100, 6.5, Float.POSITIVE_INFINITY));
        assertEquals((short) (-32), min((byte) 0, (short) (-32), 64d, 100, 6.5, Float.POSITIVE_INFINITY));
    }

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.utils.NumberTypeUtils#castToType(java.lang.Number, java.lang.Number)}.
     */
    @Test
    public void testCastToType()
    {
        assertEquals(6.5f, castToType(4f, 6.5d), 0.001f);
        assertThat(castToType(4, 6.5d), is(6));
        assertEquals(6.5d, castToType(4d, 6.5f), 0.001d);
        assertThat(castToType(4, 6.5f), is(6));
        assertThat(castToType((byte) 0, 384), is(Byte.MAX_VALUE));
        assertThat(castToType((byte) 0, 384L), is(Byte.MAX_VALUE));
        assertThat(castToType((byte) 0, (short) 384), is(Byte.MAX_VALUE));
        assertThat(castToType(6.5, (byte) 120), is(120d));
    }

}
