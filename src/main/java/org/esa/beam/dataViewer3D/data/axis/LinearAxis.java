/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickGenerator;

/**
 * A linear scale axis.
 * 
 * @author Martin Pecka
 * @param <N> The type of data this axis is related to.
 */
public class LinearAxis<N extends Number> extends AbstractAxis<N>
{

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     */
    public LinearAxis(String label, N min, N max, TickGenerator<N> tickGenerator)
    {
        super(label, min, max, tickGenerator);
    }

    @Override
    public boolean isLogScale()
    {
        return false;
    }

}
