/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis.tickgenerator._float;


/**
 * A tick generator that generates ticks based on the intended number of ticks placing them uniformly along the axis.
 * 
 * @author Martin Pecka
 */
public class TickCountTickGenerator extends
        org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickCountTickGenerator<Float>
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
        ticks = new Float[numTicks + 2];
        tickLabels = new String[numTicks + 2];

        ticks[0] = min;
        ticks[numTicks + 1] = max;

        tickLabels[0] = String.format("%1$1.4G", min);
        tickLabels[numTicks + 1] = String.format("%1$1.4G", max);

        double difference = max - min;

        double step = difference / (numTicks + 1);

        for (int i = 1; i < numTicks + 1; i++) {
            if (!logScaled)
                ticks[i] = (float) (min + i * step);
            else
                ticks[i] = (float) (min + (Math.pow(10, i / (numTicks + 1d)) - 1) / 9d * difference);
            if (showLabels[i - 1])
                tickLabels[i] = String.format("%1$1.4G", ticks[i]);
        }
    }

}