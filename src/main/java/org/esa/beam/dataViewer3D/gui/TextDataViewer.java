/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import java.awt.Font;

import javax.swing.JTextArea;

import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.point.DataPoint;

/**
 * A data viewer displaying the data in text form.
 * 
 * @author Martin Pecka
 */
public class TextDataViewer extends JTextArea implements DataViewer
{

    private DataSet           dataSet           = null;
    private CoordinatesSystem coordinatesSystem = null;
    private String            title             = null;

    /** */
    private static final long serialVersionUID  = -7435211483329068806L;

    public TextDataViewer()
    {
        setFont(Font.decode("Courier New"));
    }

    @Override
    public DataSet getDataSet()
    {
        return dataSet;
    }

    @Override
    public void setDataSet(DataSet dataSet)
    {
        this.dataSet = dataSet;
        StringBuilder sb = new StringBuilder();
        if (title != null)
            sb.append("Chart title: ").append(title).append("\n\n"); /* I18N */
        sb.append("Coordinates system:\n").append(getCoordinatesSystem()).append("\n\n"); /* I18N */
        for (DataPoint point : dataSet) {
            sb.append(point + "\n");
        }
        setText(sb.toString());
    }

    @Override
    public CoordinatesSystem getCoordinatesSystem()
    {
        return coordinatesSystem;
    }

    @Override
    public void setCoordinatesSystem(CoordinatesSystem coordinatesSystem)
    {
        this.coordinatesSystem = coordinatesSystem;
        setDataSet(dataSet);// to update the view
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public void update()
    {
        // TODO
    }

}
