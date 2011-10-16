/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis.tickgenerator;

import java.util.Arrays;

import org.esa.beam.dataViewer3D.utils.NumberTypeUtils;

/**
 * A tick generator that generates ticks based on the intended number of ticks placing them uniformly along the axis.
 * 
 * @author Martin Pecka
 * @param <N> The type of numbers the generator operates on.
 */
public abstract class TickCountTickGenerator<N extends Number> extends TickGenerator<N>
{

    /** The intended number of ticks. */
    protected int       numTicks;

    /** <code>true</code> values mean that the given label should be displayed. */
    protected boolean[] showLabels;

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays all labels.
     * 
     * @param numTicks The intended number of ticks.
     */
    protected TickCountTickGenerator(int numTicks)
    {
        this(numTicks, null);
    }

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays labels according to
     * <code>showLabels</code>.
     * 
     * @param numTicks The intended number of ticks.
     * @param showLabels <code>true</code> values mean that the given label should be displayed.
     * 
     * @throws IllegalArgumentException If <code>numTicks != showLabels.length</code>.
     */
    protected TickCountTickGenerator(int numTicks, boolean[] showLabels)
    {
        this.numTicks = numTicks;
        this.showLabels = showLabels;

        if (showLabels == null) {
            this.showLabels = new boolean[numTicks];
            Arrays.fill(this.showLabels, true);
        }

        if (this.showLabels.length != this.numTicks)
            throw new IllegalArgumentException(getClass() + ": The length of showLabels must be the same as numTicks.");
    }

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays all labels. The data type of the
     * values is determined from the given sample value.
     * 
     * @param sampleValue A number of the same data type as the type which ticks should have.
     * @param numTicks The intended number of ticks.
     * 
     * @return The tick generator.
     */
    public static <N extends Number> TickCountTickGenerator<N> createForSampleValue(N sampleValue, int numTicks)
    {
        return createForSampleValue(sampleValue, numTicks, null);
    }

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays labels according to
     * <code>showLabels</code>. The data type of the values is determined from the given sample value.
     * 
     * @param sampleValue A number of the same data type as the type which ticks should have.
     * @param numTicks The intended number of ticks.
     * @param showLabels <code>true</code> values mean that the given label should be displayed.
     * 
     * @return The tick generator.
     */
    @SuppressWarnings("unchecked")
    public static <N extends Number> TickCountTickGenerator<N> createForSampleValue(N sampleValue, int numTicks,
            boolean[] showLabels)
    {
        if (sampleValue instanceof Integer) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._int.TickCountTickGenerator(
                    numTicks, showLabels);
        } else if (sampleValue instanceof Float) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._float.TickCountTickGenerator(
                    numTicks, showLabels);
        } else if (sampleValue instanceof Double) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._double.TickCountTickGenerator(
                    numTicks, showLabels);
        } else if (sampleValue instanceof Long) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._long.TickCountTickGenerator(
                    numTicks, showLabels);
        } else if (sampleValue instanceof Byte) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._byte.TickCountTickGenerator(
                    numTicks, showLabels);
        } else if (sampleValue instanceof Short) {
            return (TickCountTickGenerator<N>) new org.esa.beam.dataViewer3D.data.axis.tickgenerator._short.TickCountTickGenerator(
                    numTicks, showLabels);
        }

        throw new UnsupportedOperationException(TickCountTickGenerator.class + ": unsupported numeric type: "
                + sampleValue.getClass());
    }

    @Override
    public N getTickLength()
    {
        if (min instanceof Integer || min instanceof Byte || min instanceof Long || min instanceof Short)
            return NumberTypeUtils.castToType(min,
                    NumberTypeUtils.max(1, NumberTypeUtils.multiply(0.05, NumberTypeUtils.sub(max, min))));
        else
            return NumberTypeUtils.multiply(0.05, NumberTypeUtils.sub(max, min));
    }
}