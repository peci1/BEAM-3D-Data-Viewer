/**
 * 
 */
package org.esa.beam.framework.barithm;

import java.util.Arrays;
import java.util.HashMap;

import org.esa.beam.framework.datamodel.ProductData;

/**
 * Utilities for operating with {@link ProductData} data types.
 * 
 * @author Martin Pecka
 */
public class TypeUtils
{
    /** The map of types valid to be casts for the key type. */
    private static final HashMap<Integer, Integer[]> validCasts = new HashMap<Integer, Integer[]>(12);

    private static final HashMap<Integer, Double[]>  typeBounds = new HashMap<Integer, Double[]>(12);

    static {
        validCasts.put(ProductData.TYPE_INT8, new Integer[] { ProductData.TYPE_INT8, ProductData.TYPE_INT16,
            ProductData.TYPE_INT32, ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_UINT8, new Integer[] { ProductData.TYPE_UINT8, ProductData.TYPE_INT16,
            ProductData.TYPE_UINT16, ProductData.TYPE_INT32, ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT32,
            ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_INT16, new Integer[] { ProductData.TYPE_INT16, ProductData.TYPE_INT32,
            ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_UINT16, new Integer[] { ProductData.TYPE_UINT16, ProductData.TYPE_INT32,
            ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_INT32, new Integer[] { ProductData.TYPE_INT32, ProductData.TYPE_FLOAT32,
            ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_UINT32, new Integer[] { ProductData.TYPE_UINT32, ProductData.TYPE_FLOAT32,
            ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_FLOAT32, new Integer[] { ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64 });
        validCasts.put(ProductData.TYPE_FLOAT64, new Integer[] { ProductData.TYPE_FLOAT64 });

        typeBounds.put(ProductData.TYPE_INT8, new Double[] { (double) Byte.MIN_VALUE, (double) Byte.MAX_VALUE });
        typeBounds.put(ProductData.TYPE_UINT8, new Double[] { 0d, (double) Byte.MAX_VALUE - (double) Byte.MIN_VALUE });
        typeBounds.put(ProductData.TYPE_INT16, new Double[] { (double) Short.MIN_VALUE, (double) Short.MAX_VALUE });
        typeBounds.put(ProductData.TYPE_UINT16,
                new Double[] { 0d, (double) Short.MAX_VALUE - (double) Short.MIN_VALUE });
        typeBounds.put(ProductData.TYPE_INT32, new Double[] { (double) Integer.MIN_VALUE, (double) Integer.MAX_VALUE });
        typeBounds.put(ProductData.TYPE_UINT32, new Double[] { 0d,
            (double) Integer.MAX_VALUE - (double) Integer.MIN_VALUE });
        typeBounds
                .put(ProductData.TYPE_FLOAT32, new Double[] { (double) (-Float.MAX_VALUE), (double) Float.MAX_VALUE });
        typeBounds.put(ProductData.TYPE_FLOAT64, new Double[] { -Double.MAX_VALUE, Double.MAX_VALUE });
    }

    private TypeUtils()
    {
        // static class
    }

    /**
     * Return true if the given castTo type is a valid cast for castFrom type.
     * 
     * @param castFrom The type to cast from.
     * @param castTo The type to cast to.
     * 
     * @return True if the given types are compatible for casting.
     */
    public static boolean isValidCast(int castFrom, int castTo)
    {
        if (castFrom == ProductData.TYPE_UNDEFINED)
            return true;

        Integer[] casts = validCasts.get(castFrom);
        if (casts == null)
            return false;
        for (Integer i : casts)
            if (i == castTo)
                return true;
        return false;
    }

    /**
     * Return the smallest type that can contain values from both the given types.
     * 
     * @param type1 The first type.
     * @param type2 The second type.
     * @return The smallest type that can contain values from both the given types.
     */
    public static int getSmallestCommonType(int type1, int type2)
    {
        if (type1 == type2)
            return type1;

        Integer[] casts1 = validCasts.get(type1), casts2 = validCasts.get(type2);
        if (casts1 == null || casts2 == null)
            return ProductData.TYPE_UNDEFINED;

        for (int i : casts1) {
            for (int j : casts2) {
                if (j == i)
                    return i;
            }
        }

        // all types should be castable to FLOAT64
        throw new IllegalStateException("Couldn't find smallest common type.");
    }

    /**
     * Return the minimum and maximum number representable in the given type.
     * 
     * @param type The type to find bounds of.
     * @return The array containing minimum and maximum.
     */
    public static Double[] getTypeBounds(int type)
    {
        return Arrays.copyOf(typeBounds.get(type), 2);
    }

    /**
     * Return the smallest data type containing both given values, but "at least" the preferredDataType.
     * 
     * @param preferredDataType The smallest data type returned.
     * @param min The minimum value to represent or <code>null</code> to ignore the lower bound.
     * @param max The maximum value to represent or <code>null</code> to ignore the upper bound.
     * @return The data type containing both values.
     * 
     * @throws IllegalArgumentException If min &gt; max.
     */
    public static int getIdealDataTypeForBounds(int preferredDataType, Double min, Double max)
    {
        if (min != null && max != null && min > max)
            throw new IllegalArgumentException("min > max");

        Double[] preferred = typeBounds.get(preferredDataType);
        Double minimum = min == null ? null : Math.min(min, preferred[0]);
        Double maximum = max == null ? null : Math.max(max, preferred[1]);

        if ((minimum == null || (minimum >= preferred[0] && minimum <= preferred[1]))
                && (maximum == null || (maximum >= preferred[0] && maximum <= preferred[1])))
            return preferredDataType;

        int[] possibleTypes;
        if (minimum == null || minimum >= 0)
            possibleTypes = new int[] { ProductData.TYPE_UINT8, ProductData.TYPE_INT8, ProductData.TYPE_UINT16,
                    ProductData.TYPE_INT16, ProductData.TYPE_UINT32, ProductData.TYPE_INT32, ProductData.TYPE_FLOAT32,
                    ProductData.TYPE_FLOAT64 };
        else
            possibleTypes = new int[] { ProductData.TYPE_INT8, ProductData.TYPE_INT16, ProductData.TYPE_INT32,
                    ProductData.TYPE_FLOAT32, ProductData.TYPE_FLOAT64 };

        for (int type : possibleTypes) {
            preferred = getTypeBounds(type);
            if ((minimum == null || (minimum >= preferred[0] && minimum <= preferred[1]))
                    && (maximum == null || (maximum >= preferred[0] && maximum <= preferred[1])))
                return type;
        }

        throw new IllegalStateException("Couldn't find ideal data type for min=" + min + " and max=" + max);
    }
}
