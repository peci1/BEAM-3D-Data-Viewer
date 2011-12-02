/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis.tickgenerator._long;

/**
 * A tick generator that generates ticks based on the intended number of ticks placing them uniformly along the axis.
 * 
 * @author Martin Pecka
 */
public class TickCountTickGenerator extends
        org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickCountTickGenerator<Long>
{

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays labels according to
     * <code>showLabels</code>.
     * 
     * @param numTicks The intended number of ticks.
     * @param showLabels <code>true</code> values mean that the given label should be displayed.
     * 
     * @throws IllegalArgumentException If <code>numTicks != showLabels.length</code>.
     */
    public TickCountTickGenerator(int numTicks, boolean[] showLabels)
    {
        super(numTicks, showLabels);
    }

    /**
     * Create the generator which creates <code>numTicks</code> ticks and displays all labels.
     * 
     * @param numTicks The intended number of ticks.
     */
    public TickCountTickGenerator(int numTicks)
    {
        super(numTicks);
    }

    @Override
    protected void computeTicks()
    {
        final double difference = max - min;

        // if we have more ticks than integers between min and max, then we have to create less ticks
        if (difference < numTicks) {
            if (difference > Integer.MAX_VALUE)
                numTicks = Integer.MAX_VALUE;
            else
                numTicks = (int) Math.max(difference - 1, 0);
        }

        ticks = new Long[numTicks + 2];
        tickLabels = new String[numTicks + 2];

        ticks[0] = min;
        ticks[numTicks + 1] = max;

        tickLabels[0] = min.toString();
        tickLabels[numTicks + 1] = max.toString();

        double step = difference / (numTicks + 1);

        for (int i = 1; i < numTicks + 1; i++) {
            // the result will always fit into long - max is higher than all values
            if (!logScaled)
                ticks[i] = min + Math.round(i * step);
            else
                ticks[i] = (long) (min + (Math.pow(10, i / (numTicks + 1d)) - 1) / 9d * difference);
            if (showLabels[i - 1])
                tickLabels[i] = ticks[i].toString();
        }
    }

}