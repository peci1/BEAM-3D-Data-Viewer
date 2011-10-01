/**
 * 
 */
package org.esa.beam.dataViewer3D.data.axis;

import org.esa.beam.dataViewer3D.data.axis.tickgenerator.TickCountTickGenerator;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;

/**
 * A system of aces and grids.
 * 
 * @author Martin Pecka
 */
public abstract class CoordinatesSystem
{

    // TODO grid capability

    /**
     * Return the aces of this coordinate system.
     * <p>
     * Changes in the returned list don't alter this coordinate system.
     * 
     * @return The aces of this coordinate system.
     */
    public abstract Axis<?>[] getAces();

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
        Axis<X> axisX = new LinearAxis<X>("x", dataSet.getMinX(), dataSet.getMaxX(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10));
        Axis<Y> axisY = new LinearAxis<Y>("y", dataSet.getMinY(), dataSet.getMaxY(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10));
        Axis<Z> axisZ = new LinearAxis<Z>("Z", dataSet.getMinZ(), dataSet.getMaxZ(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinZ(), 10));

        return new CoordinatesSystem3D<X, Y, Z>(axisX, axisY, axisZ);
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
        Axis<X> axisX = new LinearAxis<X>("x", dataSet.getMinX(), dataSet.getMaxX(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinX(), 10));
        Axis<Y> axisY = new LinearAxis<Y>("y", dataSet.getMinY(), dataSet.getMaxY(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinY(), 10));
        Axis<Z> axisZ = new LinearAxis<Z>("Z", dataSet.getMinZ(), dataSet.getMaxZ(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinZ(), 10));
        Axis<W> axisW = new LinearAxis<W>("W", dataSet.getMinW(), dataSet.getMaxW(),
                TickCountTickGenerator.createForSampleValue(dataSet.getMinW(), 10));

        return new CoordinatesSystem4D<X, Y, Z, W>(axisX, axisY, axisZ, axisW);
    }
}
