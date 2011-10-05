/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Random;

import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class RandomDataSourceTest
{

    /**
     * Test method for
     * {@link org.esa.beam.dataViewer3D.data.source.RandomDataSource#RandomDataSource(int, java.lang.Number, java.lang.Number)}
     * .
     */
    @Test
    public void testRandomDataSource()
    {
        assertNotNull(new ByteRandomDataSource(10, (byte) 5, (byte) 10));
        assertNotNull(new ByteRandomDataSource(1, (byte) 0, Byte.MAX_VALUE));
        assertNotNull(new IntRandomDataSource(10, 5, 5));
        assertNotNull(new FloatRandomDataSource(10000000, -5f, 5f));
        assertNotNull(new DoubleRandomDataSource(10, -100d, -50d));

        try {
            new ByteRandomDataSource(0, (byte) 5, (byte) 10);
            fail("Managed to create data source with invalid size: 0");
        } catch (IllegalArgumentException e) {}

        try {
            new IntRandomDataSource(-10, 5, 10);
            fail("Managed to create data source with invalid size: -10");
        } catch (IllegalArgumentException e) {}

        try {
            new FloatRandomDataSource(100, 5.1234f, 5.0234f);
            fail("Managed to create data source with min=5.1234 > max=5.0234");
        } catch (IllegalArgumentException e) {}

        try {
            new DoubleRandomDataSource(-1, 0.1234, 0.0234);
            fail("Managed to create data source with invalid size: -1, and min=0.1234 > max=0.0234");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.source.RandomDataSource#size()}.
     */
    @Test
    public void testSize()
    {
        assertEquals("Invalid size", 10, new ByteRandomDataSource(10, (byte) 5, (byte) 10).size());
        assertEquals("Invalid size", 1, new IntRandomDataSource(1, 5, 5).size());
        assertEquals("Invalid size", 1000000, new FloatRandomDataSource(1000000, -5f, 5f).size());
        assertEquals("Invalid size", Integer.MAX_VALUE / 100, new DoubleRandomDataSource(Integer.MAX_VALUE / 100,
                -100d, -50d).size());
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.source.RandomDataSource#iterator()}.
     */
    @Test
    public void testIterator()
    {
        int i = 0;
        ByteRandomDataSource src1 = new ByteRandomDataSource(10, (byte) 5, (byte) 10);
        for (Iterator<Byte> it = src1.iterator(); it.hasNext();) {
            i++;
            assertNotNull("null returned from iterator", it.next());
        }
        assertEquals("iterator returned different number of items than is the size of the source", src1.size(), i);

        i = 0;
        IntRandomDataSource src2 = new IntRandomDataSource(1, -100, 10);
        for (Iterator<Integer> it = src2.iterator(); it.hasNext();) {
            i++;
            assertNotNull("null returned from iterator", it.next());
        }
        assertEquals("iterator returned different number of items than is the size of the source", src2.size(), i);

        i = 0;
        FloatRandomDataSource src3 = new FloatRandomDataSource(1000000, 0f, Float.MAX_VALUE);
        for (Iterator<Float> it = src3.iterator(); it.hasNext();) {
            i++;
            assertNotNull("null returned from iterator", it.next());
        }
        assertEquals("iterator returned different number of items than is the size of the source", src3.size(), i);

        i = 0;
        DoubleRandomDataSource src4 = new DoubleRandomDataSource(Integer.MAX_VALUE / 1000000, Double.MIN_VALUE,
                Double.MAX_VALUE);
        for (Iterator<Double> it = src4.iterator(); it.hasNext();) {
            i++;
            assertNotNull("null returned from iterator", it.next());
        }
        assertEquals("iterator returned different number of items than is the size of the source", src4.size(), i);
    }

    /**
     * Test method for {@link org.esa.beam.dataViewer3D.data.source.AbstractDataSource#numericTypeIterator()}.
     */
    @Test
    public void testNumericTypeIterator()
    {
        Iterator<? extends NumericType<?>> numIt;
        Iterator<?> it;

        int i = 0;
        ByteRandomDataSource src1 = new ByteRandomDataSource(10, (byte) 5, (byte) 10);
        numIt = src1.numericTypeIterator();
        it = src1.iterator();
        while (numIt.hasNext()) {
            i++;
            assertTrue("Numeric type iterator has more elements than normal iterator.", it.hasNext());
            // we don't have to test if numIt.next() != null, because we've already tested that it.next() != null and
            // here we check for equality
            assertEquals("Numeric type iterator returns different values than the standard iterator.", it.next(), numIt
                    .next().getNumber());
        }
        assertEquals("Numeric type iterator returned different number of items than is the size of the source.",
                src1.size(), i);
        assertFalse("The standard iterator returns more elements than the numeric type one.", it.hasNext());

        i = 0;
        IntRandomDataSource src2 = new IntRandomDataSource(1, -100, 10);
        numIt = src2.numericTypeIterator();
        it = src2.iterator();
        while (numIt.hasNext()) {
            i++;
            assertTrue("Numeric type iterator has more elements than normal iterator.", it.hasNext());
            // we don't have to test if numIt.next() != null, because we've already tested that it.next() != null and
            // here we check for equality
            assertEquals("Numeric type iterator returns different values than the standard iterator.", it.next(), numIt
                    .next().getNumber());
        }
        assertEquals("Numeric type iterator returned different number of items than is the size of the source.",
                src2.size(), i);
        assertFalse("The standard iterator returns more elements than the numeric type one.", it.hasNext());

        i = 0;
        FloatRandomDataSource src3 = new FloatRandomDataSource(1000000, 0f, Float.MAX_VALUE);
        numIt = src3.numericTypeIterator();
        it = src3.iterator();
        while (numIt.hasNext()) {
            i++;
            assertTrue("Numeric type iterator has more elements than normal iterator.", it.hasNext());
            // we don't have to test if numIt.next() != null, because we've already tested that it.next() != null and
            // here we check for equality
            assertEquals("Numeric type iterator returns different values than the standard iterator.", it.next(), numIt
                    .next().getNumber());
        }
        assertEquals("Numeric type iterator returned different number of items than is the size of the source.",
                src3.size(), i);
        assertFalse("The standard iterator returns more elements than the numeric type one.", it.hasNext());

        i = 0;
        DoubleRandomDataSource src4 = new DoubleRandomDataSource(Integer.MAX_VALUE / 1000000, Double.MIN_VALUE,
                Double.MAX_VALUE);
        numIt = src4.numericTypeIterator();
        it = src4.iterator();
        while (numIt.hasNext()) {
            i++;
            assertTrue("Numeric type iterator has more elements than normal iterator.", it.hasNext());
            // we don't have to test if numIt.next() != null, because we've already tested that it.next() != null and
            // here we check for equality
            assertEquals("Numeric type iterator returns different values than the standard iterator.", it.next(), numIt
                    .next().getNumber());
        }
        assertEquals("Numeric type iterator returned different number of items than is the size of the source.",
                src4.size(), i);
        assertFalse("The standard iterator returns more elements than the numeric type one.", it.hasNext());
    }

    private class ByteRandomDataSource extends RandomDataSource<Byte>
    {
        /**
         * @param size
         * @param min
         * @param max
         * @throws IllegalArgumentException
         */
        public ByteRandomDataSource(int size, Byte min, Byte max) throws IllegalArgumentException
        {
            super(size, min, max);
        }

        @Override
        protected Byte getRandomValue()
        {
            Random r = new Random();
            byte[] b = new byte[1];
            r.nextBytes(b);
            return b[0];
        }

        @Override
        protected NumericType<Byte> getNumericType(Byte number)
        {
            return new ByteType(number);
        }
    }

    private class IntRandomDataSource extends RandomDataSource<Integer>
    {
        /**
         * @param size
         * @param min
         * @param max
         * @throws IllegalArgumentException
         */
        public IntRandomDataSource(int size, Integer min, Integer max) throws IllegalArgumentException
        {
            super(size, min, max);
        }

        @Override
        protected Integer getRandomValue()
        {
            return new Random().nextInt();
        }

        @Override
        protected NumericType<Integer> getNumericType(Integer number)
        {
            return new IntType(number);
        }
    }

    private class FloatRandomDataSource extends RandomDataSource<Float>
    {
        /**
         * @param size
         * @param min
         * @param max
         * @throws IllegalArgumentException
         */
        public FloatRandomDataSource(int size, Float min, Float max) throws IllegalArgumentException
        {
            super(size, min, max);
        }

        @Override
        protected Float getRandomValue()
        {
            return Float.MIN_VALUE + new Random().nextFloat() * Float.MAX_VALUE;
        }

        @Override
        protected NumericType<Float> getNumericType(Float number)
        {
            return new FloatType(number, null);
        }
    }

    private class DoubleRandomDataSource extends RandomDataSource<Double>
    {
        /**
         * @param size
         * @param min
         * @param max
         * @throws IllegalArgumentException
         */
        public DoubleRandomDataSource(int size, Double min, Double max) throws IllegalArgumentException
        {
            super(size, min, max);
        }

        @Override
        protected Double getRandomValue()
        {
            return Double.MIN_VALUE + new Random().nextDouble() * Double.MAX_VALUE;
        }

        @Override
        protected NumericType<Double> getNumericType(Double number)
        {
            return new DoubleType(number, null);
        }
    }
}
