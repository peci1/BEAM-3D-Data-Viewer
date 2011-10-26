/**
 * 
 */
package org.esa.beam.framework.barithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.ProductData;
import org.esa.beam.framework.dataop.barithm.BandArithmetic;
import org.junit.Before;
import org.junit.Test;

import com.bc.jexp.Namespace;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class PossiblyInvalidExpressionTest
{

    private Namespace namespace;

    @Before
    public void startup()
    {
        namespace = BandArithmetic.createDefaultNamespace(new Product[] { new TestProduct() }, 0);
    }

    /**
     * Test method for
     * {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#PossiblyInvalidExpression(com.bc.jexp.Namespace)}
     * .
     */
    @Test
    public void testPossiblyInvalidExpressionNamespace()
    {
        assertEquals("", new PossiblyInvalidExpression(namespace).toString());
    }

    /**
     * Test method for
     * {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#PossiblyInvalidExpression(java.lang.String, com.bc.jexp.Namespace)}
     * .
     */
    @Test
    public void testPossiblyInvalidExpressionStringNamespace()
    {
        assertEquals("reflec_1", new PossiblyInvalidExpression("reflec_1", namespace).toString().trim());
        assertEquals("reflec_2", new PossiblyInvalidExpression("reflec_2", namespace).toString().trim());
        assertEquals("(int32) reflec_1", new PossiblyInvalidExpression("(int32) reflec_1", namespace).toString().trim());
        assertEquals("(uint8) reflec_1", new PossiblyInvalidExpression("(uint8) reflec_1", namespace).toString().trim());
        assertEquals("reflec_1 + reflec_2", new PossiblyInvalidExpression("reflec_1 + reflec_2", namespace).toString()
                .trim());
        assertEquals("2*reflec_1", new PossiblyInvalidExpression("2*reflec_1", namespace).toString().trim());
        assertEquals("(float64) reflec_1 + reflec_2", new PossiblyInvalidExpression("(float64) reflec_1 + reflec_2",
                namespace).toString().trim());
        assertEquals("nonexistent", new PossiblyInvalidExpression("nonexistent", namespace).toString().trim());
        assertEquals("(int32) nonexistent", new PossiblyInvalidExpression("(int32) nonexistent", namespace).toString()
                .trim());
    }

    /**
     * Test method for
     * {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#PossiblyInvalidExpression(java.lang.String, com.bc.jexp.Namespace, int)}
     * .
     */
    @Test
    public void testPossiblyInvalidExpressionStringNamespaceInt()
    {
        assertEquals("(float32) reflec_1", new PossiblyInvalidExpression("reflec_1", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
        assertEquals("(float64) reflec_2", new PossiblyInvalidExpression("reflec_2", namespace,
                ProductData.TYPE_FLOAT64).toString().trim());
        assertEquals("(float32) reflec_1", new PossiblyInvalidExpression("(int32) reflec_1", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
        assertEquals("(uint16) reflec_1", new PossiblyInvalidExpression("(uint8) reflec_1", namespace,
                ProductData.TYPE_UINT16).toString().trim());
        assertEquals("(float32) reflec_1 + reflec_2", new PossiblyInvalidExpression("reflec_1 + reflec_2", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
        assertEquals("(float32) 2*reflec_1", new PossiblyInvalidExpression("2*reflec_1", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
        assertEquals("(float64) reflec_1 + reflec_2", new PossiblyInvalidExpression("(float64) reflec_1 + reflec_2",
                namespace, ProductData.TYPE_FLOAT64).toString().trim());
        assertEquals("(float32) nonexistent", new PossiblyInvalidExpression("nonexistent", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
        assertEquals("(float32) nonexistent", new PossiblyInvalidExpression("(int32) nonexistent", namespace,
                ProductData.TYPE_FLOAT32).toString().trim());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#isEmpty()}.
     */
    @Test
    public void testIsEmpty()
    {
        assertTrue(new PossiblyInvalidExpression(namespace).isEmpty());
        assertTrue(new PossiblyInvalidExpression("", namespace).isEmpty());
        assertFalse(new PossiblyInvalidExpression("refl", namespace).isEmpty());
        assertFalse(new PossiblyInvalidExpression("reflec_1", namespace, ProductData.TYPE_INT16).isEmpty());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#setExpression(java.lang.String)}.
     */
    @Test
    public void testSetExpression()
    {
        PossiblyInvalidExpression expr = new PossiblyInvalidExpression(namespace);
        expr.setExpression("reflec_1");
        assertEquals("reflec_1", expr.toString());
        expr.setExpression("reflec_2");
        assertEquals("reflec_2", expr.toString());
        expr.setExpression("ref");
        assertEquals("(int32) ref", expr.toString());
        expr.setExpression("(int32) reflec_1");
        assertEquals("reflec_1", expr.toString());

        expr = new PossiblyInvalidExpression("reflec_1", namespace, ProductData.TYPE_FLOAT32);
        assertEquals("(float32) reflec_1", expr.toString());
        expr.setExpression("reflec_2");
        assertEquals("(float32) reflec_2", expr.toString());
        expr.setExpression("ref");
        assertEquals("(float32) ref", expr.toString());
        expr.setExpression("(int32) reflec_1");
        assertEquals("(float32) reflec_1", expr.toString());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#setTypeString(java.lang.String)}.
     */
    @Test
    public void testSetTypeString()
    {
        PossiblyInvalidExpression expr = new PossiblyInvalidExpression("reflec_1", namespace);
        expr.setTypeString(ProductData.TYPESTRING_FLOAT32);
        assertEquals("(float32) reflec_1", expr.toString());
        expr.setTypeString(ProductData.TYPESTRING_FLOAT64);
        assertEquals("(float64) reflec_1", expr.toString());
        expr.setTypeString(ProductData.TYPESTRING_UINT8);
        assertEquals("(uint8) reflec_1", expr.toString());
        expr.setTypeString(ProductData.TYPESTRING_INT32);
        // int32 is the natural type
        assertEquals("reflec_1", expr.toString());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#setType(int)}.
     */
    @Test
    public void testSetType()
    {
        PossiblyInvalidExpression expr = new PossiblyInvalidExpression("reflec_1", namespace);
        expr.setType(ProductData.TYPE_FLOAT32);
        assertEquals("(float32) reflec_1", expr.toString());
        expr.setType(ProductData.TYPE_FLOAT64);
        assertEquals("(float64) reflec_1", expr.toString());
        expr.setType(ProductData.TYPE_UINT8);
        assertEquals("(uint8) reflec_1", expr.toString());
        expr.setType(ProductData.TYPE_INT32);
        // int32 is the natural type
        assertEquals("reflec_1", expr.toString());
    }

    /**
     * Test method for
     * {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#parseExpression(java.lang.String)}.
     */
    @Test
    public void testParseExpression()
    {
        final String[] expressions = new String[] { "reflec_1", "reflec_2", "(int16) reflec_1", "(badType) reflec_1",
                "(int64) (int32) reflec_1", "(float64) (float32) reflec_1", "foo", "(foo) bar" };
        final PossiblyInvalidExpression expr = new PossiblyInvalidExpression(namespace);
        for (String exp : expressions) {
            expr.parseExpression(exp);
            assertEquals(exp, expr.toString());
        }
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#getExpression()}.
     */
    @Test
    public void testGetExpression()
    {
        assertEquals("reflec_1", new PossiblyInvalidExpression("reflec_1", namespace).getExpression());
        assertEquals("reflec_1", new PossiblyInvalidExpression("(int32) reflec_1", namespace).getExpression());
        assertEquals("reflec_1", new PossiblyInvalidExpression("(float32) reflec_1", namespace).getExpression());
        assertEquals("(badCast) reflec_1",
                new PossiblyInvalidExpression("(badCast) reflec_1", namespace).getExpression());
        assertEquals("reflec_", new PossiblyInvalidExpression("reflec_", namespace).getExpression());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#getTypeString()}.
     */
    @Test
    public void testGetTypeString()
    {
        assertEquals("int32", new PossiblyInvalidExpression("reflec_1", namespace).getTypeString());
        assertEquals("int16", new PossiblyInvalidExpression("(int16) reflec_1", namespace).getTypeString());
        assertEquals("float32", new PossiblyInvalidExpression("(float32) reflec_1", namespace).getTypeString());
        assertEquals("int16", new PossiblyInvalidExpression("(int16) (int8) reflec_1", namespace).getTypeString());
        assertEquals("int16", new PossiblyInvalidExpression("(int16) reflec_", namespace).getTypeString());
        assertEquals("", new PossiblyInvalidExpression("(badCast) reflec_1", namespace).getTypeString());
        assertEquals("", new PossiblyInvalidExpression("reflec_bad", namespace).getTypeString());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#getType()}.
     */
    @Test
    public void testGetType()
    {
        assertEquals(ProductData.TYPE_INT32, new PossiblyInvalidExpression("reflec_1", namespace).getType());
        assertEquals(ProductData.TYPE_INT16, new PossiblyInvalidExpression("(int16) reflec_1", namespace).getType());
        assertEquals(ProductData.TYPE_FLOAT32, new PossiblyInvalidExpression("(float32) reflec_1", namespace).getType());
        assertEquals(ProductData.TYPE_INT16,
                new PossiblyInvalidExpression("(int16) (int8) reflec_1", namespace).getType());
        assertEquals(ProductData.TYPE_INT16, new PossiblyInvalidExpression("(int16) reflec_", namespace).getType());
        assertEquals(ProductData.TYPE_UNDEFINED,
                new PossiblyInvalidExpression("(badCast) reflec_1", namespace).getType());
        assertEquals(ProductData.TYPE_UNDEFINED, new PossiblyInvalidExpression("reflec_bad", namespace).getType());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#getExpressionWithCast()}.
     */
    @Test
    public void testGetExpressionWithCast()
    {
        final String[] expressions = new String[] { "reflec_1", "reflec_2", "(int16) reflec_1", "(badType) reflec_1",
                "(int64) (int32) reflec_1", "(float64) (float32) reflec_1", "foo", "(foo) bar" };
        for (String exp : expressions) {
            assertEquals(exp, new PossiblyInvalidExpression(exp, namespace).getExpressionWithCast());
        }
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#getExpressionNaturalType()}.
     */
    @Test
    public void testGetExpressionNaturalType()
    {
        assertEquals(ProductData.TYPE_UNDEFINED, new PossiblyInvalidExpression(namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_INT32,
                new PossiblyInvalidExpression("reflec_1", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_INT32,
                new PossiblyInvalidExpression("reflec_2", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_UINT32,
                new PossiblyInvalidExpression("reflec_3", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_FLOAT64,
                new PossiblyInvalidExpression("reflec_4", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_INT32,
                new PossiblyInvalidExpression("(int32) reflec_1", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_INT32,
                new PossiblyInvalidExpression("(float64) reflec_2", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_UINT32,
                new PossiblyInvalidExpression("(uint8) reflec_3", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_FLOAT64,
                new PossiblyInvalidExpression("(int8) reflec_4", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_UNDEFINED,
                new PossiblyInvalidExpression("(int32) reflec_bad", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_UNDEFINED,
                new PossiblyInvalidExpression("(int3) reflec_2", namespace).getExpressionNaturalType());
        assertEquals(ProductData.TYPE_UNDEFINED,
                new PossiblyInvalidExpression("(int3) reflec_bad", namespace).getExpressionNaturalType());
    }

    /**
     * Test method for {@link org.esa.beam.framework.barithm.PossiblyInvalidExpression#isValid()}.
     */
    @Test
    public void testIsValid()
    {
        assertTrue(new PossiblyInvalidExpression(namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_1", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_2", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_3", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_4", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("reflec_5", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("reflec_6", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_1 + reflec_2", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_1 * reflec_3", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("reflec_1 / reflec_4", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("(int32) reflec_1", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("(float32) reflec_1", namespace).isValid());
        assertTrue(new PossiblyInvalidExpression("(float64) reflec_1 + reflec_2", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(int8) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(int16) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(uint8) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(uint16) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(uint32) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("reflec_", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(badCast) reflec_1", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(badCast) reflec_", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(int8) reflec_ + reflec_", namespace).isValid());
        assertFalse(new PossiblyInvalidExpression("(int8) reflec_1++", namespace).isValid());

        PossiblyInvalidExpression expr = new PossiblyInvalidExpression(namespace);
        assertTrue(expr.isValid());
        expr.setExpression("reflec_1");
        assertTrue(expr.isValid());
        expr.setExpression("reflec_2");
        assertTrue(expr.isValid());
        expr.setExpression("reflec_");
        assertFalse(expr.isValid());
        expr.setExpression("reflec_1");
        assertTrue(expr.isValid());
        expr.setType(ProductData.TYPE_FLOAT32);
        assertTrue(expr.isValid());
        expr.setType(ProductData.TYPE_FLOAT64);
        assertTrue(expr.isValid());
        expr.setType(ProductData.TYPE_INT8);
        assertFalse(expr.isValid());
        expr.setType(ProductData.TYPE_INT32);
        assertTrue(expr.isValid());
        expr.setType(ProductData.TYPE_UNDEFINED);
        assertFalse(expr.isValid());
        expr.setTypeString(ProductData.TYPESTRING_INT32);
        assertTrue(expr.isValid());
        expr.setTypeString(ProductData.TYPESTRING_FLOAT32);
        assertTrue(expr.isValid());
        expr.setTypeString(ProductData.TYPESTRING_FLOAT64);
        assertTrue(expr.isValid());
        expr.setTypeString(ProductData.TYPESTRING_INT8);
        assertFalse(expr.isValid());
        expr.setTypeString(ProductData.TYPESTRING_INT32);
        assertTrue(expr.isValid());
        expr.setExpression("reflec_bad");
        expr.setType(ProductData.TYPE_UNDEFINED);
        assertFalse(expr.isValid());
        expr.setType(ProductData.TYPE_INT32);
        assertFalse(expr.isValid());
        expr.setExpression("reflec_1");
        assertTrue(expr.isValid());

    }

    private class TestProduct extends Product
    {
        public TestProduct()
        {
            super("testProduct", ProductData.TYPESTRING_INT32, 100, 100);
            addBand(new TestBand("reflec_1", ProductData.TYPE_INT32, 100, 100));
            addBand(new TestBand("reflec_2", ProductData.TYPE_INT32, 100, 100));
            addBand(new TestBand("reflec_3", ProductData.TYPE_UINT32, 100, 100));
            addBand(new TestBand("reflec_4", ProductData.TYPE_FLOAT64, 100, 100));
        }

    }

    private class TestBand extends Band
    {

        /**
         * @param name
         * @param dataType
         * @param width
         * @param height
         */
        public TestBand(String name, int dataType, int width, int height)
        {
            super(name, dataType, width, height);
        }

        @Override
        public int getGeophysicalDataType()
        {
            return getDataType();
        }

    }

}