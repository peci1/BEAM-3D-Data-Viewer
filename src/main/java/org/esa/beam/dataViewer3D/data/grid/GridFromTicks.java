/**
 * 
 */
package org.esa.beam.dataViewer3D.data.grid;

import org.esa.beam.dataViewer3D.data.axis.Axis;

/**
 * A grid where all grid lines are created from the ticks on the given aces.
 * 
 * @author Martin Pecka
 */
public class GridFromTicks extends Grid
{

    /**
     * Create a grid based on the given aces.
     * 
     * @param xAxis The x axis.
     * @param yAxis The y axis.
     * @param zAxis The z axis.
     */
    public GridFromTicks(Axis<?> xAxis, Axis<?> yAxis, Axis<?> zAxis)
    {
        gridLines = new double[2 * xAxis.getTicks().length + 2 * yAxis.getTicks().length + 2 * zAxis.getTicks().length][6];

        double xMax = xAxis.getMax().doubleValue();
        double yMax = yAxis.getMax().doubleValue();
        double zMax = zAxis.getMax().doubleValue();

        double xMin = xAxis.getMin().doubleValue();
        double yMin = yAxis.getMin().doubleValue();
        double zMin = zAxis.getMin().doubleValue();
        double[] mins = new double[] { xMin, yMin, zMin };

        int i = 0;
        int axisNr = 0;
        for (Axis<?> axis : new Axis<?>[] { xAxis, yAxis, zAxis }) {
            for (Number n : axis.getTicks()) {
                double dn = n.doubleValue();
                gridLines[i][0] = xMin;
                gridLines[i][1] = yMin;
                gridLines[i][2] = zMin;
                gridLines[i][3] = xMax;
                gridLines[i][4] = yMax;
                gridLines[i][5] = zMax;
                gridLines[i][axisNr] = gridLines[i][axisNr + 3] = dn;
                gridLines[i][(axisNr + 1) % 3 + 3] = mins[(axisNr + 1) % 3];
                i++;
                gridLines[i][0] = xMin;
                gridLines[i][1] = yMin;
                gridLines[i][2] = zMin;
                gridLines[i][3] = xMax;
                gridLines[i][4] = yMax;
                gridLines[i][5] = zMax;
                gridLines[i][axisNr] = gridLines[i][axisNr + 3] = dn;
                gridLines[i][(axisNr + 2) % 3 + 3] = mins[(axisNr + 2) % 3];
                i++;
            }
            axisNr++;
        }
    }
}
