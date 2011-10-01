/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

/**
 * An axis of a data set.
 * 
 * @author Martin Pecka
 * @param <N> The type of data this axis is related to.
 */
public interface Axis<N extends Number>
{
    /**
     * Return the label of the axis.
     * 
     * @return The label of the axis.
     */
    String getLabel();

    /**
     * Return the minimum value this axis displays.
     * 
     * @return The minimum value this axis displays.
     */
    N getMin();

    /**
     * Return the maximum value this axis displays.
     * 
     * @return The maximum value this axis displays.
     */
    N getMax();

    /**
     * Is the scale of this axis logarithmic?
     * 
     * @return Whether the scale of this axis is logarithmic.
     */
    boolean isLogScale();

    /**
     * Return an array of values at which ticks are found.
     * 
     * @return An array of values at which ticks are found.
     */
    N[] getTicks();

    /**
     * Return an array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>.
     * <code>null</code> value means that the tick has no label.
     * 
     * @return An array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>.
     *         <code>null</code> value means that the tick has no label.
     */
    String[] getTickLabels();
}
