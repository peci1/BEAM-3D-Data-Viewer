/**
 * 
 */
package org.esa.beam.framework.param.validators;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

import org.esa.beam.framework.param.AbstractParamValidator;
import org.esa.beam.framework.param.ParamFormatException;
import org.esa.beam.framework.param.ParamParseException;
import org.esa.beam.framework.param.ParamValidateException;
import org.esa.beam.framework.param.Parameter;

/**
 * Validator fof {@link Font}.
 * 
 * @author Martin Pecka
 */
public class FontValidator extends AbstractParamValidator
{

    /** The accessible system font families. */
    private final static String[] families = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                   .getAvailableFontFamilyNames();
    private final static Collator collator = Collator.getInstance(Locale.getDefault());

    static {
        // sort the font families to enable binary search
        collator.setStrength(Collator.PRIMARY);
        Arrays.sort(families, collator);
    }

    @Override
    public Object parse(Parameter parameter, String text) throws ParamParseException
    {
        final Font result = Font.decode(text);
        if (result == null)
            throw new ParamParseException(parameter, "Invalid font specification.");/* I18N */
        return result;
    }

    @Override
    public String format(Parameter parameter, Object value) throws ParamFormatException
    {
        final Font font = (Font) value;
        final StringBuilder result = new StringBuilder();
        result.append(font.getFamily()).append(", ");
        if (font.isBold()) {
            if (font.isItalic()) {
                result.append("bold italic, "); /* I18N */
            } else {
                result.append("bold, ");/* I18N */
            }
        } else if (font.isItalic()) {
            result.append("italic, ");/* I18N */
        }
        result.append(font.getSize()).append("pt");/* I18N */
        return result.toString();
    }

    @Override
    public void validate(Parameter parameter, Object value) throws ParamValidateException
    {
        final String family = ((Font) value).getFamily();
        final int index = Arrays.binarySearch(families, family, collator);
        if (index < 0 || index >= families.length || !families[index].equals(family))
            throw new ParamValidateException(parameter, "Invalid font family.");/* I18N */
    }

}
