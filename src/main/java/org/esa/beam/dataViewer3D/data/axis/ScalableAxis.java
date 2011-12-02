/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickGenerator;

/**
 * A simple axis with options to set scale.
 * 
 * @author Martin Pecka
 * @param <N> The type of data this axis is related to.
 */
public class ScalableAxis<N extends Number> extends AbstractAxis<N>
{

    /** The value of minimum as a double. */
    protected double minAsDouble    = 0;

    /** The length of the axis as a double. */
    protected double lengthAsDouble = 1;

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     * @param scale The display scale of the axis.
     * @param logScale Whether to use log-scaling.
     */
    public ScalableAxis(String label, N min, N max, TickGenerator<N> tickGenerator, double scale, boolean logScale)
    {
        super(label, min, max, tickGenerator, scale, logScale);
        minAsDouble = min.doubleValue();
        lengthAsDouble = length.doubleValue();
    }

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     * @param scale The display scale of the axis.
     */
    public ScalableAxis(String label, N min, N max, TickGenerator<N> tickGenerator, double scale)
    {
        this(label, min, max, tickGenerator, scale, false);
    }

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     * @param logScale Whether to use log-scaling.
     */
    public ScalableAxis(String label, N min, N max, TickGenerator<N> tickGenerator, boolean logScale)
    {
        this(label, min, max, tickGenerator, 1, logScale);
    }

    /**
     * @param label The label of this axis.
     * @param min Minimum value displayed on the axis.
     * @param max Maximum value displayed on the axis.
     * @param tickGenerator The object that generates all the ticks of this axis.
     */
    public ScalableAxis(String label, N min, N max, TickGenerator<N> tickGenerator)
    {
        this(label, min, max, tickGenerator, 1, false);
    }

    @Override
    protected void updateLength()
    {
        super.updateLength();
        lengthAsDouble = length.doubleValue();
    }

    @Override
    public void setMin(N min)
    {
        super.setMin(min);
        minAsDouble = min.doubleValue();
    }

    @Override
    public double applyScaling(double coordinate)
    {
        double result = coordinate;
        if (logScale) {
            double ratio = 1 + 9 * (result - minAsDouble) / lengthAsDouble;
            result = minAsDouble + Math.log10(ratio) * lengthAsDouble;
        }
        return result * scale;
    }

}
