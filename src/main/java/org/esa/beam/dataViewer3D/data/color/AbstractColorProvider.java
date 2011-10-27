/**
 * 
 */
package org.esa.beam.dataViewer3D.data.color;

import java.awt.Color;

/**
 * Abstract color provider that implements bounds handling.
 * 
 * @author Martin Pecka
 */
public abstract class AbstractColorProvider implements ColorProvider
{

    /** The bounds. */
    protected double min, max;
    /** The difference between max and min. */
    protected double difference;

    @Override
    public void setBounds(double min, double max)
    {
        if (min > max)
            throw new IllegalArgumentException("min > max");
        this.min = min;
        this.max = max;
        this.difference = max - min;
    }

    @Override
    public double getMin()
    {
        return min;
    }

    @Override
    public double getMax()
    {
        return max;
    }

    @Override
    public Color getColor(double sample)
    {
        return getColor(sample, 1);
    }

    /**
     * Return a default implementation of a color provider.
     * 
     * @return A color provider.
     */
    public static ColorProvider getDefaultColorProvider()
    {
        return new HSVColorProvider();
    }
}
