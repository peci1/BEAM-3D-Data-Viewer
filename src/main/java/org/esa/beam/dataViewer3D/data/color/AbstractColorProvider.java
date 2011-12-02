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
    protected double  min, max;
    /** The difference between max and min. */
    protected double  difference;
    /** Whether the values are discrete. */
    protected boolean isDiscrete = false;

    @Override
    public void setBounds(double min, double max)
    {
        isDiscrete = false;
        innerSetBounds(min, max);
    }

    @Override
    public void setBounds(long min, long max)
    {
        isDiscrete = true;
        innerSetBounds(min, max);
    }

    /**
     * Set the bounds without changing isDiscrete.
     * 
     * @param min The minimum value.
     * @param max The maximum value.
     */
    protected void innerSetBounds(double min, double max)
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
