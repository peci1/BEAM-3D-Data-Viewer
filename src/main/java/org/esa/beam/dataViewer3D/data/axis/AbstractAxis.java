/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.castToType;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.multiply;

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
    protected String                 label, defaultLabel;

    /** Minimum and maximum values displayed on the axis. */
    protected N                      min, max;

    /** The length of the axis. */
    protected N                      length;

    protected final TickGenerator<N> tickGenerator;

    /** Array of values at which ticks are found. */
    protected N[]                    ticks;

    /**
     * Array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>. <code>null</code> value
     * means that the tick has no label.
     */
    protected String[]               tickLabels;

    /**
     * The length of a tick (in the same units as is the axis). If set to a high value, this means we want a grid
     * to be drawn.
     */
    protected N                      tickLength;

    /** The scale of this axis. */
    protected double                 scale    = 1d;

    /** Whether to use log-scaling. */
    protected boolean                logScale = false;

    /**
     * @param label The label of this axis (also used as the default axis).
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     * @param scale The display scale of the axis.
     * @param logScale Whether to use log-scaling.
     */
    public AbstractAxis(String label, N min, N max, TickGenerator<N> tickGenerator, double scale, boolean logScale)
    {
        this.label = label;
        setDefaultLabel(label);
        this.min = min;
        this.max = max;
        if (min == max) {
            if (min.doubleValue() != 0)
                this.max = multiply(2, min);
            else
                this.max = castToType(max, 1);
        }
        updateLength();

        this.tickGenerator = tickGenerator;

        setScale(scale);
        setLogScale(logScale);

        updateTicks();
    }

    @Override
    public String getLabel()
    {
        if (label != null)
            return label;
        return defaultLabel;
    }

    @Override
    public void setLabel(String label)
    {
        this.label = label;
    }

    @Override
    public void setDefaultLabel(String label)
    {
        final StringBuilder builder = new StringBuilder(label);
        if (scale != 1 || logScale) {
            builder.append(" (");
            if (scale != 1) {
                builder.append("scaled ").append(scale).append("-times"); /* I18N */
                if (logScale)
                    builder.append(", ");
            }
            if (logScale)
                builder.append("log-scaled"); /* I18N */
            builder.append(")");
        }

        this.defaultLabel = builder.toString();
    }

    /**
     * Update the length of this axis.
     */
    protected void updateLength()
    {
        length = NumberTypeUtils.sub(max, min);
    }

    @Override
    public double getScale()
    {
        return scale;
    }

    @Override
    public void setScale(double scale)
    {
        this.scale = scale;
    }

    @Override
    public boolean isLogScale()
    {
        return logScale;
    }

    @Override
    public void setLogScale(boolean logScale)
    {
        this.logScale = logScale;
        this.tickGenerator.setLogScaled(logScale);
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
    public double getLengthScaled()
    {
        return Math.abs(applyScaling(max.doubleValue()) - applyScaling(min.doubleValue()));
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
    public void updateTicks()
    {
        tickGenerator.setBounds(this.min, this.max);
        tickGenerator.generateTicks();
        this.ticks = tickGenerator.getTicks();
        this.tickLabels = tickGenerator.getTickLabels();
        this.tickLength = tickGenerator.getTickLength();
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
