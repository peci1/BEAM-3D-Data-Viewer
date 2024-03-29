/**
 * 
 */
package org.esa.beam.dataViewer3D.data.color;

import java.awt.Color;

/**
 * A provider for colors based on a linear value scale.
 * 
 * @author Martin Pecka
 */
public interface ColorProvider
{
    /**
     * Set the bounds of the values this provider can handle.
     * 
     * @param min The minimum value.
     * @param max The maximum value.
     * 
     * @throw IllegalArgumentException If min &gt; max.
     */
    void setBounds(double min, double max);

    /**
     * Set the bounds of the values this provider can handle. The provider will take the discreteness of the values into
     * account.
     * 
     * @param min The minimum value.
     * @param max The maximum value.
     * 
     * @throw IllegalArgumentException If min &gt; max.
     */
    void setBounds(long min, long max);

    /**
     * Return the lower bound.
     * 
     * @return The lower bound.
     */
    double getMin();

    /**
     * Return the upper bound.
     * 
     * @return The upper bound.
     */
    double getMax();

    /**
     * Get the color corresponding to the given sample. If the sample is out of the bounds, returns the value for
     * minimum or maximum.
     * 
     * @param sample The sample to get color for.
     * @return The color.
     */
    Color getColor(double sample);

    /**
     * Get the color corresponding to the given sample.
     * 
     * @param sample The sample to get color for. If the sample is out of the bounds, returns the value for minimum or
     *            maximum.
     * @param weight The 'weight' of the sample - a number between 0 and 1 determining how important the sample is.
     * @return The color.
     */
    Color getColor(double sample, double weight);
}
