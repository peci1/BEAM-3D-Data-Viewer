/**
 * 
 */
package org.esa.beam.dataViewer3D.data.type;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ByteTypeTest extends NumericTypeTestCommon<Byte>
{

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.ByteType#ByteType(java.lang.Byte)}.
     */
    @Test
    public void testByteType()
    {
        testConstructor((byte) 0, Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#hashCode()}.
     */
    @Test
    public void testHashCode()
    {
        testHashCode(true, (byte) 5, (byte) 0, Byte.MIN_VALUE);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#getNumber()}.
     */
    @Test
    public void testGetNumber()
    {
        testGetNumber((byte) 0, (byte) 100, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) -10);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.type.NumericType#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        testEqualsObject(new Byte[] { (byte) 0, Byte.MAX_VALUE }, new Byte[] { (byte) 0, (byte) 1, (byte) 0, (byte) -1,
                Byte.MIN_VALUE, Byte.MAX_VALUE }, new Object[] { (byte) 0, null, (byte) 0, "asd", (byte) 0, (byte) 0 });
    }

    @Override
    protected NumericType<Byte> getInstance(Byte value, Integer precision)
    {
        return new ByteType(value);
    }

    @Override
    protected Byte[] getRandomValues(int count)
    {
        Random rand = new Random();
        byte[] rndBytes = new byte[count];
        rand.nextBytes(rndBytes);
        Byte[] result = new Byte[count];
        for (int i = 0; i < count; i++)
            result[i] = rndBytes[i];
        return result;
    }

}
