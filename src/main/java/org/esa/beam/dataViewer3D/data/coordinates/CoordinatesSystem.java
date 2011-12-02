/**
 * 
 */
package org.esa.beam.dataViewer3D.data.coordinates;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.add;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.multiply;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.sub;

import org.esa.beam.dataViewer3D.data.axis.Axis;
import org.esa.beam.dataViewer3D.data.axis.ScalableAxis;
import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickCountTickGenerator;
import org.esa.beam.dataViewer3D.data.color.AbstractColorProvider;
import org.esa.beam.dataViewer3D.data.color.ColorProvider;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;
import org.esa.beam.dataViewer3D.data.grid.Grid;
import org.esa.beam.dataViewer3D.data.grid.GridFromTicks;

/**
 * A system of aces and grids.
 * 
 * @author Martin Pecka
 */
public abstract class CoordinatesSystem
{

    /** The grid to show. */
    private Grid          grid          = null;

    /** A color provider coloring the points. */
    private ColorProvider colorProvider = AbstractColorProvider.getDefaultColorProvider();

    /** Whether to show a grid. */
    protected boolean     showGrid      = false;

    /**
     * Create a coordinate system with the given grid.
     * 
     * @param grid The grid to use (may be <code>null</code>).
     */
    public CoordinatesSystem(Grid grid)
    {
        this.grid = grid;
    }

    /**
     * Return the aces of this coordinate system.
     * <p>
     * Changes in the returned list don't alter this coordinate system.
     * 
     * @return The aces of this coordinate system.
     */
    public abstract Axis<?>[] getAces();

    /**
     * Return the grid of this coordinate system.
     * <p>
     * May be <code>null</code>!
     * 
     * @return The grid.
     */
    public Grid getGrid()
    {
        return grid;
    }

    /**
     * Set the grid of this corrdinate system.
     * 
     * @param grid The new grid. May be <code>null</code>.
     */
    public void setGrid(Grid grid)
    {
        this.grid = grid;
    }

    /**
     * Whether to show a grid.
     * 
     * @param show Whether to show a grid.
     */
    public void setShowGrid(boolean show)
    {
        showGrid = show;
    }

    /**
     * Create a dummy coordinates system.
     * 
     * @return The coordinates system.
     */
    public static CoordinatesSystem createNoDataCoordinatesSystem()
    {
        Axis<Double> axisX = new ScalableAxis<Double>("x", 0d, 5d, TickCountTickGenerator.createForSampleValue(0d, 10));
        Axis<Double> axisY = new ScalableAxis<Double>("y", 0d, 5d, TickCountTickGenerator.createForSampleValue(0d, 10));
        Axis<Double> axisZ = new ScalableAxis<Double>("z", 0d, 5d, TickCountTickGenerator.createForSampleValue(0d, 10));

        Grid grid = new GridFromTicks(axisX, axisY, axisZ);

        return new CoordinatesSystem3D<Double, Double, Double>(axisX, axisY, axisZ, grid);
    }

    /**
     * Create a default implementation of a 3D or 4D coordinate system for the given data set.
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static CoordinatesSystem createDefaultCoordinatesSystem(DataSet dataSet)
    {
        if (dataSet instanceof DataSet3D<?, ?, ?>)
            return createDefaultCoordinatesSystem((DataSet3D<?, ?, ?>) dataSet);
        else
            return createDefaultCoordinatesSystem((DataSet4D<?, ?, ?, ?>) dataSet);
    }

    /**
     * Create a default implementation of a 3D coordinate system for the given data set.
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number> CoordinatesSystem3D<X, Y, Z> createDefaultCoordinatesSystem(
            DataSet3D<X, Y, Z> dataSet)
    {
        return createCoordinatesSystem(null, null, 1, false, null, null, 1, false, null, null, 1, false, dataSet);
    }

    /**
     * Create a default implementation of a 3D coordinate system for the given data set with the option to specify some
     * maxima or minima.
     * 
     * @param minX Minimum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxX Maximum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xScale Scale of the x axis.
     * @param xLogScale Use log-scaling for x axis?
     * @param minY Minimum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxY Maximum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yScale Scale of the y axis.
     * @param yLogScale Use log-scaling for y axis?
     * @param minZ Minimum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxZ Maximum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zScale Scale of the z axis.
     * @param zLogScale Use log-scaling for z axis?
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number> CoordinatesSystem3D<X, Y, Z> createCoordinatesSystem(
            X minX, X maxX, double xScale, boolean xLogScale, Y minY, Y maxY, double yScale, boolean yLogScale, Z minZ,
            Z maxZ, double zScale, boolean zLogScale, DataSet3D<X, Y, Z> dataSet)
    {
        return createCoordinatesSystem(minX, maxX, "x", xScale, xLogScale, minY, maxY, "y", yScale, yLogScale, minZ,
                maxZ, "z", zScale, zLogScale, dataSet);
    }

    /**
     * Create a default implementation of a 3D coordinate system for the given data set with the option to specify some
     * maxima or minima.
     * 
     * @param minX Minimum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxX Maximum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xLabel Label for x axis.
     * @param xScale Scale of the x axis.
     * @param xLogScale Use log-scaling for x axis?
     * @param minY Minimum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxY Maximum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yLabel Label for y axis.
     * @param yScale Scale of the y axis.
     * @param yLogScale Use log-scaling for y axis?
     * @param minZ Minimum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxZ Maximum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zLabel Label for z axis.
     * @param zScale Scale of the z axis.
     * @param zLogScale Use log-scaling for z axis?
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number> CoordinatesSystem3D<X, Y, Z> createCoordinatesSystem(
            X minX, X maxX, String xLabel, double xScale, boolean xLogScale, Y minY, Y maxY, String yLabel,
            double yScale, boolean yLogScale, Z minZ, Z maxZ, String zLabel, double zScale, boolean zLogScale,
            DataSet3D<X, Y, Z> dataSet)
    {
        Axis<X> axisX = new ScalableAxis<X>(xLabel, minX != null ? minX : dataSet.getMinX(), maxX != null ? maxX : add(
                dataSet.getMinX(), multiply(1.25, sub(dataSet.getMaxX(), dataSet.getMinX()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10));
        Axis<Y> axisY = new ScalableAxis<Y>(yLabel, minY != null ? minY : dataSet.getMinY(), maxY != null ? maxY : add(
                dataSet.getMinY(), multiply(1.25, sub(dataSet.getMaxY(), dataSet.getMinY()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10));
        Axis<Z> axisZ = new ScalableAxis<Z>(zLabel, minZ != null ? minZ : dataSet.getMinZ(), maxZ != null ? maxZ : add(
                dataSet.getMinZ(), multiply(1.25, sub(dataSet.getMaxZ(), dataSet.getMinZ()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinZ(), 10));

        Grid grid = new GridFromTicks(axisX, axisY, axisZ);

        return new CoordinatesSystem3D<X, Y, Z>(axisX, axisY, axisZ, grid);
    }

    /**
     * Create a default implementation of a 4D coordinate system for the given data set.
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> CoordinatesSystem4D<X, Y, Z, W> createDefaultCoordinatesSystem(
            DataSet4D<X, Y, Z, W> dataSet)
    {
        return createCoordinatesSystem(null, null, 1, false, null, null, 1, false, null, null, 1, false, null, null,
                false, dataSet);
    }

    /**
     * Create a default implementation of a 3D coordinate system for the given data set with the option to specify some
     * maxima or minima.
     * 
     * @param minX Minimum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxX Maximum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xScale Scale of the x axis.
     * @param xLogScale Use log-scaling for x axis?
     * @param minY Minimum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxY Maximum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yScale Scale of the y axis.
     * @param yLogScale Use log-scaling for y axis?
     * @param minZ Minimum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxZ Maximum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zScale Scale of the z axis.
     * @param zLogScale Use log-scaling for z axis?
     * @param minW Minimum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxW Maximum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param wLogScale Use log-scaling for w axis?
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> CoordinatesSystem4D<X, Y, Z, W> createCoordinatesSystem(
            X minX, X maxX, double xScale, boolean xLogScale, Y minY, Y maxY, double yScale, boolean yLogScale, Z minZ,
            Z maxZ, double zScale, boolean zLogScale, W minW, W maxW, boolean wLogScale, DataSet4D<X, Y, Z, W> dataSet)
    {
        return createCoordinatesSystem(minX, maxX, "x", xScale, xLogScale, minY, maxY, "y", yScale, yLogScale, minZ,
                maxZ, "z", zScale, zLogScale, minW, maxW, "w", wLogScale, dataSet);
    }

    /**
     * Create a default implementation of a 3D coordinate system for the given data set with the option to specify some
     * maxima or minima.
     * 
     * @param minX Minimum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxX Maximum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xLabel Label for x axis.
     * @param xScale Scale of the x axis.
     * @param xLogScale Use log-scaling for x axis?
     * @param minY Minimum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxY Maximum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yLabel Label for y axis.
     * @param yScale Scale of the y axis.
     * @param yLogScale Use log-scaling for y axis?
     * @param minZ Minimum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxZ Maximum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zLabel Label for z axis.
     * @param zScale Scale of the z axis.
     * @param zLogScale Use log-scaling for z axis?
     * @param minW Minimum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param maxW Maximum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param wLabel Label for w axis.
     * @param wLogScale Use log-scaling for w axis?
     * 
     * @param dataSet The data set to generate coordinate system for.
     * @return The created coordinates system.
     */
    public static <X extends Number, Y extends Number, Z extends Number, W extends Number> CoordinatesSystem4D<X, Y, Z, W> createCoordinatesSystem(
            X minX, X maxX, String xLabel, double xScale, boolean xLogScale, Y minY, Y maxY, String yLabel,
            double yScale, boolean yLogScale, Z minZ, Z maxZ, String zLabel, double zScale, boolean zLogScale, W minW,
            W maxW, String wLabel, boolean wLogScale, DataSet4D<X, Y, Z, W> dataSet)
    {
        Axis<X> axisX = new ScalableAxis<X>(xLabel, minX != null ? minX : dataSet.getMinX(), maxX != null ? maxX : add(
                dataSet.getMinX(), multiply(1.25, sub(dataSet.getMaxX(), dataSet.getMinX()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10), xScale, xLogScale);
        Axis<Y> axisY = new ScalableAxis<Y>(yLabel, minY != null ? minY : dataSet.getMinY(), maxY != null ? maxY : add(
                dataSet.getMinY(), multiply(1.25, sub(dataSet.getMaxY(), dataSet.getMinY()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10), yScale, yLogScale);
        Axis<Z> axisZ = new ScalableAxis<Z>(zLabel, minZ != null ? minZ : dataSet.getMinZ(), maxZ != null ? maxZ : add(
                dataSet.getMinZ(), multiply(1.25, sub(dataSet.getMaxZ(), dataSet.getMinZ()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinZ(), 10), zScale, zLogScale);
        // TODO change to color axis
        Axis<W> axisW = new ScalableAxis<W>(wLabel, minW != null ? minW : dataSet.getMinW(), maxW != null ? maxW
                : dataSet.getMaxW(), TickCountTickGenerator.createForSampleValue(dataSet.getMinW(), 10), wLogScale);

        Grid grid = new GridFromTicks(axisX, axisY, axisZ);

        return new CoordinatesSystem4D<X, Y, Z, W>(axisX, axisY, axisZ, axisW, grid);
    }

    /**
     * @return A color provider coloring the points.
     */
    public ColorProvider getColorProvider()
    {
        return colorProvider;
    }

    /**
     * Override to set the bounds.
     * 
     * @param colorProvider A color provider coloring the points.
     */
    public void setColorProvider(ColorProvider colorProvider)
    {
        this.colorProvider = colorProvider;
    }

}
