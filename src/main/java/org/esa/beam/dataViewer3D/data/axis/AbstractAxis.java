/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickGenerator;

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

    /** Array of values at which ticks are found. */
    protected N[]      ticks;

    /**
     * Array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>. <code>null</code> value
     * means that the tick has no label.
     */
    protected String[] tickLabels;

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
        tickGenerator.setBounds(min, max);
        tickGenerator.generateTicks();
        this.ticks = tickGenerator.getTicks();
        this.tickLabels = tickGenerator.getTickLabels();
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
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Axis [label=").append(label).append(", min=").append(min).append(", max=").append(max)
                .append("]");
        return builder.toString();
    }

}
