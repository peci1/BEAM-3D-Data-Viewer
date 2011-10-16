/**
 * 
 */
package org.esa.beam.dataViewer3D.data.coordinates;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.add;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.multiply;
import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.sub;

import org.esa.beam.dataViewer3D.data.axis.Axis;
import org.esa.beam.dataViewer3D.data.axis.LinearAxis;
import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickCountTickGenerator;
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
    private Grid      grid     = null;

    /** Whether to show a grid. */
    protected boolean showGrid = false;

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
        Axis<X> axisX = new LinearAxis<X>("x", dataSet.getMinX(), add(dataSet.getMinX(),
                multiply(1.25, sub(dataSet.getMaxX(), dataSet.getMinX()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10));
        Axis<Y> axisY = new LinearAxis<Y>("y", dataSet.getMinY(), add(dataSet.getMinY(),
                multiply(1.25, sub(dataSet.getMaxY(), dataSet.getMinY()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10));
        Axis<Z> axisZ = new LinearAxis<Z>("z", dataSet.getMinZ(), add(dataSet.getMinZ(),
                multiply(1.25, sub(dataSet.getMaxZ(), dataSet.getMinZ()))),
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
        Axis<X> axisX = new LinearAxis<X>("x", dataSet.getMinX(), add(dataSet.getMinX(),
                multiply(1.25, sub(dataSet.getMaxX(), dataSet.getMinX()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10));
        Axis<Y> axisY = new LinearAxis<Y>("y", dataSet.getMinY(), add(dataSet.getMinY(),
                multiply(1.25, sub(dataSet.getMaxY(), dataSet.getMinY()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10));
        Axis<Z> axisZ = new LinearAxis<Z>("z", dataSet.getMinZ(), add(dataSet.getMinZ(),
                multiply(1.25, sub(dataSet.getMaxZ(), dataSet.getMinZ()))),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinZ(), 10));
        // TODO change to color axis
        Axis<W> axisW = new LinearAxis<W>("w", dataSet.getMinW(), dataSet.getMaxW(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinW(), 10));

        Grid grid = new GridFromTicks(axisX, axisY, axisZ);

        return new CoordinatesSystem4D<X, Y, Z, W>(axisX, axisY, axisZ, axisW, grid);
    }
}
