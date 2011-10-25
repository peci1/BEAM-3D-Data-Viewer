/**
 * 
 */
package org.esa.beam.framework.barithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.esa.beam.framework.datamodel.ProductData;
import org.junit.Test;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class TypeUtilsTest
{

    /**
     * Test method for {@link org.esa.beam.framework.barithm.TypeUtils#isValidCast(int, int)}.
     */
    @Test
    public void testIsValidCast()
    {
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_INT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_UINT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT8, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_INT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_UINT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT16, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_INT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_UINT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_INT32, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_INT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_FLOAT64));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_UINT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_UINT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT8, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_INT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_UINT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_UINT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT16, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_INT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_INT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_UINT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UINT32, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT32));

        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT32));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_FLOAT64));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT8));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT16));
        assertFalse(TypeUtils.isValidCast(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT32));

        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_INT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_INT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_INT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_FLOAT32));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_FLOAT64));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_UINT8));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_UINT16));
        assertTrue(TypeUtils.isValidCast(ProductData.TYPE_UNDEFINED, ProductData.TYPE_UINT32));
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.TypeUtils#getSmallestCommonType(int, int)}.
     */
    @Test
    public void testGetSmallestCommonType()
    {
        assertEquals(ProductData.TYPE_INT8,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_UINT8));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT8, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_UINT16));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT16, ProductData.TYPE_INT8));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT8, ProductData.TYPE_UINT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT32, ProductData.TYPE_INT8));

        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_UINT8));
        assertEquals(ProductData.TYPE_INT16,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT8, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_UINT16));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT16, ProductData.TYPE_INT16));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT16, ProductData.TYPE_UINT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT32, ProductData.TYPE_INT16));

        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_UINT8));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT8, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_UINT16));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT16, ProductData.TYPE_INT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_INT32, ProductData.TYPE_UINT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT32, ProductData.TYPE_INT32));

        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT8));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT8, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT16));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT16, ProductData.TYPE_FLOAT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT32, ProductData.TYPE_UINT32));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT32));

        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT8));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT8, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT16));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT16, ProductData.TYPE_FLOAT64));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_FLOAT64, ProductData.TYPE_UINT32));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getSmallestCommonType(ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT64));
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.TypeUtils#getTypeBounds(int)}.
     */
    @Test
    public void testGetTypeBounds()
    {
        assertEquals((Double) (double) Byte.MIN_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT8)[0]);
        assertEquals((Double) (double) Byte.MAX_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT8)[1]);
        assertEquals((Double) 0d, TypeUtils.getTypeBounds(ProductData.TYPE_UINT8)[0]);
        assertEquals((Double) ((double) Byte.MAX_VALUE - (double) Byte.MIN_VALUE),
                TypeUtils.getTypeBounds(ProductData.TYPE_UINT8)[1]);
        assertEquals((Double) (double) Short.MIN_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT16)[0]);
        assertEquals((Double) (double) Short.MAX_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT16)[1]);
        assertEquals((Double) 0d, TypeUtils.getTypeBounds(ProductData.TYPE_UINT16)[0]);
        assertEquals((Double) ((double) Short.MAX_VALUE - (double) Short.MIN_VALUE),
                TypeUtils.getTypeBounds(ProductData.TYPE_UINT16)[1]);
        assertEquals((Double) (double) Integer.MIN_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT32)[0]);
        assertEquals((Double) (double) Integer.MAX_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_INT32)[1]);
        assertEquals((Double) 0d, TypeUtils.getTypeBounds(ProductData.TYPE_UINT32)[0]);
        assertEquals((Double) ((double) Integer.MAX_VALUE - (double) Integer.MIN_VALUE),
                TypeUtils.getTypeBounds(ProductData.TYPE_UINT32)[1]);
        assertEquals((Double) (double) (-Float.MAX_VALUE), TypeUtils.getTypeBounds(ProductData.TYPE_FLOAT32)[0]);
        assertEquals((Double) (double) Float.MAX_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_FLOAT32)[1]);
        assertEquals((Double) (-Double.MAX_VALUE), TypeUtils.getTypeBounds(ProductData.TYPE_FLOAT64)[0]);
        assertEquals((Double) Double.MAX_VALUE, TypeUtils.getTypeBounds(ProductData.TYPE_FLOAT64)[1]);
    }

    /**
     * Test method for
     * {@link org.esa.beam.framework.barithm.TypeUtils#getIdealDataTypeForBounds(int, java.lang.Double, java.lang.Double)}
     * .
     */
    @Test
    public void testGetIdealDataTypeForBounds()
    {
        assertEquals(ProductData.TYPE_INT8, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, 100d));
        assertEquals(ProductData.TYPE_INT16, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, 1000d));
        assertEquals(ProductData.TYPE_INT32,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, Short.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, Integer.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, 10d * Float.MAX_VALUE));

        assertEquals(ProductData.TYPE_UINT8, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_UINT8, 0d, 100d));
        assertEquals(ProductData.TYPE_UINT16, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_UINT8, 0d, 1000d));
        assertEquals(ProductData.TYPE_UINT32,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_UINT8, 0d, 2d * Short.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT32,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_UINT8, 0d, 2d * Integer.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_UINT8, 0d, 10d * Float.MAX_VALUE));

        assertEquals(ProductData.TYPE_FLOAT64, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_FLOAT64, -1d, 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_FLOAT64, -1d, 1000d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_FLOAT64, -1d, Short.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_FLOAT64, -1d, Integer.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_FLOAT64, -1d, 10d * Float.MAX_VALUE));

        assertEquals(ProductData.TYPE_INT8, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, -1d, null));
        assertEquals(ProductData.TYPE_UINT16, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, null, 1000d));
        assertEquals(ProductData.TYPE_UINT16,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, null, Short.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_UINT32,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, null, Integer.MAX_VALUE + 100d));
        assertEquals(ProductData.TYPE_FLOAT64,
                TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, null, 10d * Float.MAX_VALUE));
        assertEquals(ProductData.TYPE_INT8, TypeUtils.getIdealDataTypeForBounds(ProductData.TYPE_INT8, null, null));
    }

}
