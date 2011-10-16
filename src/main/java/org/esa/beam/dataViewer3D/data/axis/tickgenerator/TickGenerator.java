/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis.tickgenerator;

/**
 * Generates ticks based on the minimum and maximum bounds of the axis.
 * <p>
 * Intended use is the following:
 * <p>
 * <code><pre>
 * TickGenerator<Integer> generator = ... ;//get an instance
 * generator.setBounds(min, max);
 * generator.generateTicks();
 * axis.ticks = generator.getTicks();
 * axis.tickLabels = generator.getTickLabels();
 * </pre></code>
 * 
 * @author Martin Pecka
 * @param <N> The type of data this generator works with.
 */
public abstract class TickGenerator<N extends Number>
{

    /** The minimum and maximum values of the axis this generator generates ticks for. */
    protected N        min = null, max = null;

    /** Array of values at which ticks are found. */
    protected N[]      ticks;

    /**
     * Array of tick labels - label at index <code>i</code> corresponds to tick <code>i</code>. <code>null</code> value
     * means that the tick has no label.
     */
    protected String[] tickLabels;

    /**
     * @return The ticks.
     */
    public N[] getTicks()
    {
        return ticks;
    }

    /**
     * @return The tickLabels.
     */
    public String[] getTickLabels()
    {
        return tickLabels;
    }

    /**
     * Return the length of a tick (in the same units as is the axis). If set to a high value, this means we want a grid
     * to be drawn.
     * 
     * @return The length of a tick.
     */
    public abstract N getTickLength();

    /**
     * Set the minimum and maximum values of the axis this generator generates ticks for.
     * 
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * 
     * @throws IllegalArgumentException If <code>min &gt;= max</code>.
     */
    public void setBounds(N min, N max)
    {
        if (min.doubleValue() > max.doubleValue()) {
            throw new IllegalArgumentException(getClass() + ": Trying to set minimum bound higher than maximum.");
        }

        this.min = min;
        this.max = max;
    }

    /**
     * Generates the ticks and tick labels, so that you can then call {@link #getTicks()} and {@link #getTickLabels()}
     * to get the result.
     * 
     * @throws IllegalStateException if no bounds have been set yet.
     */
    public void generateTicks()
    {
        if (min == null || max == null)
            throw new IllegalStateException(getClass() + ": You must set bounds before computing ticks.");

        computeTicks();
    }

    /**
     * Performs the sole tick position computation.
     */
    protected abstract void computeTicks();
}