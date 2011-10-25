/**
 * 
 */
package org.esa.beam.framework.barithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esa.beam.framework.datamodel.ProductData;
import org.esa.beam.framework.dataop.barithm.BandArithmetic;
import org.esa.beam.framework.dataop.barithm.RasterDataSymbol;

import com.bc.jexp.Namespace;
import com.bc.jexp.ParseException;
import com.bc.jexp.Parser;
import com.bc.jexp.Term;
import com.bc.jexp.impl.ParserImpl;

/**
 * A band maths expression with a data type, which can contain an additional cast to a "larger" data type. The
 * expression can be invalid.
 * <p>
 * An expression with a cast is of the form <code>(cast_to_type) expression</code> or just <code>expression</code>,
 * where <code>expression</code> is a valid band maths expression and <code>cast_to_type</code> is one of
 * {@link ProductData}.TYPESTRING_*. An expression cannot be 'down-cast' - you cannot cast it to a datatype which cannot
 * hold all the values from the bands the expression consists of.
 * <p>
 * Casting to/from string and time types isn't supported.
 * 
 * @author Martin Pecka
 */
public class PossiblyInvalidExpression
{
    /** The regex used to detect the cast expression. */
    protected static final Pattern CAST_REGEX = Pattern.compile("^\\s*\\((" + ProductData.TYPESTRING_INT8 + "|"
                                                      + ProductData.TYPESTRING_UINT8 + "|"
                                                      + ProductData.TYPESTRING_INT16 + "|"
                                                      + ProductData.TYPESTRING_UINT16 + "|"
                                                      + ProductData.TYPESTRING_INT32 + "|"
                                                      + ProductData.TYPESTRING_UINT32 + "|"
                                                      + ProductData.TYPESTRING_FLOAT32 + "|"
                                                      + ProductData.TYPESTRING_FLOAT64 + ")\\) ?");

    /** The band arithmetics expression. */
    protected String               expression;
    /** The expression with the possible cast prepended. */
    protected String               expressionWithCast;
    /** The string identifier of the data type. One of {@link ProductData}.TYPESTRING_*. */
    protected String               typeString;
    /** The data type of this expression. One of {@link ProductData}.TYPE_*. */
    protected int                  type;
    /** The 'natural' type of the expression (the type without casting the expression). */
    protected int                  naturalType;
    /** True if the expression is valid. */
    protected boolean              isValid;
    /** The namespace for resolving bands and variables. */
    protected final Namespace      namespace;

    /**
     * Create empty typed expression.
     * 
     * @param namespace The namespace for resolving bands and variables.
     */
    public PossiblyInvalidExpression(Namespace namespace)
    {
        this.namespace = namespace;

        this.expression = "";
        this.naturalType = ProductData.TYPE_UNDEFINED;
        this.type = this.naturalType;
        this.typeString = "";
        this.expressionWithCast = "";
        this.isValid = true;
    }

    /**
     * Create a typed expression from the given expression. The type will be autodetermined or read from the cast.
     * 
     * @param bandArihmeticExpression The expression this one will represent (may contain cast).
     * @param namespace The namespace for resolving bands and variables.
     */
    public PossiblyInvalidExpression(String bandArihmeticExpression, Namespace namespace)
    {
        this.namespace = namespace;

        this.expressionWithCast = bandArihmeticExpression;

        this.expression = removeCastFromExpression(bandArihmeticExpression);
        detectNaturalTypeOfExpression();

        final String cast = detectCast(bandArihmeticExpression);
        if (cast != null) {
            this.type = ProductData.getType(cast);
            this.typeString = cast;
        } else {
            this.type = this.naturalType;
            this.typeString = ProductData.getTypeString(type);
            if (this.typeString == null)
                this.typeString = "";
        }

        validate();
    }

    /**
     * Create a typed expression and cast it to the given type.
     * 
     * @param bandArithmeticExpression The expression this one will represent (may contain cast).
     * @param namespace The namespace for resolving bands and variables.
     * @param castToType The type to cast to.
     */
    public PossiblyInvalidExpression(String bandArithmeticExpression, Namespace namespace, int castToType)
    {
        this(bandArithmeticExpression, namespace);
        setType(castToType);
    }

    /**
     * Create an independent copy of the given expression.
     * 
     * @param source The expression to take values from.
     */
    public PossiblyInvalidExpression(PossiblyInvalidExpression source)
    {
        this.expression = source.expression;
        this.expressionWithCast = source.expressionWithCast;
        this.namespace = source.namespace;
        this.isValid = source.isValid;
        this.naturalType = source.naturalType;
        this.type = source.type;
        this.typeString = source.typeString;
    }

    /**
     * True if this expression is empty.
     * 
     * @return True if this expression is empty.
     */
    public boolean isEmpty()
    {
        return expressionWithCast.length() == 0;
    }

    /**
     * Set the expression part without cast. If the given expression contains a cast, it will be ignored.
     * <p>
     * To set this expression's value from a string with a cast, use {@link #parseExpression(String)}.
     * 
     * @param expression The expression this object will represent (without a cast).
     */
    public void setExpression(String expression)
    {
        this.expression = removeCastFromExpression(expression);
        detectNaturalTypeOfExpression();
        if (naturalType != ProductData.TYPE_UNDEFINED && !TypeUtils.isValidCast(naturalType, type)) {
            type = naturalType;
            typeString = ProductData.getTypeString(type);
        }
        updateExpressionWithCast();
        validate();
    }

    /**
     * @param typeString The string form of the (casting) type of this expression.
     */
    public void setTypeString(String typeString)
    {
        this.typeString = typeString;
        Integer newType = ProductData.getType(typeString);
        if (newType == null) {
            this.type = ProductData.TYPE_UNDEFINED;
        } else {
            this.type = newType;
        }

        updateExpressionWithCast();
        validate();
    }

    /**
     * @param type The type of this expression.
     */
    public void setType(int type)
    {
        this.type = type;
        String newTypeString = ProductData.getTypeString(type);
        if (newTypeString == null)
            this.typeString = "";
        else
            this.typeString = newTypeString;
        updateExpressionWithCast();
        validate();
    }

    /**
     * Set the value of this expression from the given one.
     * 
     * @param expressionWithCast The expression to parse.
     */
    public void parseExpression(String expressionWithCast)
    {
        this.expressionWithCast = expressionWithCast;

        final String expression = removeCastFromExpression(expressionWithCast);
        this.expression = expression;
        detectNaturalTypeOfExpression();

        String cast = detectCast(expressionWithCast);
        Integer type;
        if (cast == null) {
            type = this.naturalType;
        } else {
            type = ProductData.getType(cast);
            if (type == null)
                type = ProductData.TYPE_UNDEFINED;
        }
        setType(type);
        validate();
    }

    /**
     * @return The expression without the cast.
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * @return The string form of the type of this expression. Returns "" for zero-length expressions.
     */
    public String getTypeString()
    {
        return typeString;
    }

    /**
     * @return The type of this expression. Returns TYPE_UNDEFINED for zero-length expressions.
     */
    public int getType()
    {
        return type;
    }

    /**
     * @return The expression with the cast string prepended.
     */
    public String getExpressionWithCast()
    {
        return expressionWithCast;
    }

    /**
     * @return The type this expression would have without the cast. Returns TYPE_UNDEFINED for zero-length expressions.
     */
    public int getExpressionNaturalType()
    {
        return naturalType;
    }

    /**
     * True if the expression is valid.
     * 
     * @return True if the expression is valid.
     */
    public boolean isValid()
    {
        return isValid;
    }

    /**
     * Validate the given expression and return the result.
     * 
     * @param expression The expression to validate.
     * @param canContainCast If the expression can contain cast.
     * @return The result of the validation.
     */
    protected boolean validateExpression(final String expression, final boolean canContainCast)
    {
        if (expression.length() == 0)
            return true;

        final String exprWithoutCast = removeCastFromExpression(expression);
        final String cast = detectCast(expression);

        final String expr = (canContainCast ? expression : exprWithoutCast);

        if (!canContainCast && cast != null)
            return false;

        final Parser parser = new ParserImpl(namespace, false);
        try {
            parser.parse(exprWithoutCast);
        } catch (ParseException e) {
            return false;
        }

        // if the expression cannot contain cast, we have finished
        if (!canContainCast)
            return true;

        if (cast == null)
            return true;

        int castType = ProductData.getType(cast);
        int bandType = detectNaturalTypeOfExpression(expr);

        if (!TypeUtils.isValidCast(bandType, castType))
            return false;

        return true;
    }

    /**
     * Detect the natural type of the current expression and save it into <code>naturalType</code>.
     */
    protected void detectNaturalTypeOfExpression()
    {
        this.naturalType = detectNaturalTypeOfExpression(expression);
    }

    /**
     * Detect the natural type of the given expression and return it.
     * 
     * @param expression The expression to detect natural type of.
     * 
     * @return The detected type. Returns TYPE_UNDEFINED for zero-length or invalid expressions.
     */
    protected int detectNaturalTypeOfExpression(String expression)
    {
        if (expression.length() == 0)
            return ProductData.TYPE_UNDEFINED;

        final String expr = removeCastFromExpression(expression);
        int commonType = ProductData.TYPE_UNDEFINED;
        final Parser parser = new ParserImpl(namespace, false);
        try {
            final Term term = parser.parse(expr);
            final RasterDataSymbol[] refRasterDataSymbols = BandArithmetic.getRefRasterDataSymbols(term);
            for (final RasterDataSymbol refRasterDataSymbol : refRasterDataSymbols) {
                if (commonType == ProductData.TYPE_UNDEFINED)
                    commonType = refRasterDataSymbol.getRaster().getDataType();
                else
                    commonType = TypeUtils.getSmallestCommonType(commonType, refRasterDataSymbol.getRaster()
                            .getDataType());
            }
        } catch (ParseException e) {
            return ProductData.TYPE_UNDEFINED;
        }
        return commonType;
    }

    /**
     * Update expressionWithCast to correspond to the other fields.
     */
    protected void updateExpressionWithCast()
    {
        if (expression.length() == 0) {
            expressionWithCast = "";
        } else if (type != naturalType && typeString.length() > 0) {
            expressionWithCast = "(" + typeString + ") " + expression;
        } else {
            expressionWithCast = expression;
        }
    }

    /**
     * Return the typeString of the data type used as a cast in the given expression, or <code>null</code> if no cast is
     * used in the expression.
     * 
     * @param expression The expression to find cast in.
     * @return The typeString of the cast in the expression, or <code>null</code> if no cast is in the expression.
     */
    protected String detectCast(String expression)
    {
        Matcher matcher = CAST_REGEX.matcher(expression);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    /**
     * Return the given expression without any casts.
     * 
     * @param expression The expression to remove cast from.
     * @return The expression without any casts.
     */
    protected String removeCastFromExpression(String expression)
    {
        Matcher matcher = CAST_REGEX.matcher(expression);
        if (matcher.find())
            return matcher.replaceFirst("");
        else
            return expression;
    }

    /**
     * Updates the value of isValid based on the current value.
     */
    protected void validate()
    {
        if (expression.length() == 0) {
            this.isValid = true;
            return;
        }

        if (this.type == ProductData.TYPE_UNDEFINED || this.typeString.length() == 0) {
            this.isValid = false;
            return;
        }
        this.isValid = validateExpression(expressionWithCast, true);
    }

    @Override
    public String toString()
    {
        return expressionWithCast;
    }

    /**
     * This method cannot be overridden in order for the BEAM integration to work correctly.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object other)
    {
        return super.equals(other);
    }

    /**
     * This method cannot be overridden in order for the BEAM integration to work correctly.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final int hashCode()
    {
        return super.hashCode();
    }
}
