/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickGenerator;
import org.esa.beam.dataViewer3D.utils.NumberTypeUtils;

/**
 * The base for all axis implementations.
 * 
 * @author Martin Pecka
 * @param <N> The type of data this axis is related to.
 */
public abstract class AbstractAxis<N extends Number> implements Axis<N>
{

    /** The label of this axis. */
    protected String   label;

    /** Minimum and maximum values displayed on the axis. */
    protected N        min, max;

    /** The length of the axis. */
    protected N        length;

    /** Array of values at which ticks are found. */
    protected N[]      ticks;

    /**
     * Array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>. <code>null</code> value
     * means that the tick has no label.
     */
    protected String[] tickLabels;

    /**
     * The length of a tick (in the same units as is the axis). If set to a high value, this means we want a grid
     * to be drawn.
     */
    protected N        tickLength;

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     */
    public AbstractAxis(String label, N min, N max, TickGenerator<N> tickGenerator)
    {
        this.label = label;
        this.min = min;
        this.max = max;
        updateLength();
        tickGenerator.setBounds(min, max);
        tickGenerator.generateTicks();
        this.ticks = tickGenerator.getTicks();
        this.tickLabels = tickGenerator.getTickLabels();
        this.tickLength = tickGenerator.getTickLength();
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    /**
     * @param label The label of this axis.
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    /**
     * Update the length of this axis.
     */
    protected void updateLength()
    {
        length = NumberTypeUtils.sub(max, min);
    }

    @Override
    public N getMin()
    {
        return min;
    }

    /**
     * @param min Minimum value displayed on the axis.
     */
    public void setMin(N min)
    {
        this.min = min;
        updateLength();
    }

    @Override
    public N getMax()
    {
        return max;
    }

    /**
     * @param max Maximum value displayed on the axis.
     */
    public void setMax(N max)
    {
        this.max = max;
        updateLength();
    }

    @Override
    public N getLength()
    {
        return length;
    }

    @Override
    public N[] getTicks()
    {
        return ticks;
    }

    @Override
    public String[] getTickLabels()
    {
        return tickLabels;
    }

    @Override
    public N getTickLength()
    {
        return this.tickLength;
    }

    @Override
    public void setTickLength(N length)
    {
        this.tickLength = length;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Axis [label=").append(label).append(", min=").append(min).append(", max=").append(max)
                .append("]");
        return builder.toString();
    }

}
