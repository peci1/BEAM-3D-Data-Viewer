/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;

/**
 * A component able to display 3D or 4D data points.
 * 
 * @author Martin Pecka
 */
public interface DataViewer
{
    /**
     * Return the data set used as source for this viewer.
     * 
     * @return The data set used as source for this viewer.
     */
    DataSet getDataSet();

    /**
     * Set a new data source for the viewer.
     * 
     * @param dataSet The new data source.
     */
    void setDataSet(DataSet dataSet);

    /**
     * Return the coordinates system of this viewer.
     * 
     * @return The coordinates system of this viewer.
     */
    CoordinatesSystem getCoordinatesSystem();

    /**
     * Set the coordinates system to be used by this viewer.
     * 
     * @param system The new coordinates system to set.
     */
    void setCoordinatesSystem(CoordinatesSystem system);

    /**
     * Update the view.
     */
    void update();

    /**
     * @return The title of the viewer.
     */
    String getTitle();

    /**
     * Set the title of the viewer.
     * 
     * @param title The title.
     */
    void setTitle(String title);
}
