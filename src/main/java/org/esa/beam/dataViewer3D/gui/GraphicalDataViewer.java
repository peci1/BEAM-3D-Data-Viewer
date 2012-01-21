/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import java.awt.print.Printable;
import java.io.File;

/**
 * A data viewer displaying the files graphically.
 * 
 * @author Martin Pecka
 */
public interface GraphicalDataViewer extends DataViewer, Printable
{
    /**
     * @return The rendering parameters. Change the values directly to affect rendering.
     */
    RenderParams getRenderParams();

    /**
     * Save the viewer's displayed data to the given file.
     * <p>
     * This method is nonblocking, the caller can capture its finish using the given callback.
     * 
     * @param file The file to save to.
     * @param imageType Type of the image (informal name of the format).
     * @param callback The callback called after the image is saved.
     */
    void saveImage(final File file, final String imageType, final ImageCaptureCallback callback);

    /**
     * Save the viewer's displayed data to system clipboard.
     * <p>
     * This method is nonblocking, the caller can capture its finish using the given callback.
     * 
     * @param callback The callback called after the image is saved.
     */
    void copyImageToClipboard(final ImageCaptureCallback callback);

    /**
     * Set the data set to be empty with the given fictive bounds.
     * 
     * @param minX The fictive minimum x value.
     * @param maxX The fictive maximum x value.
     * @param minY The fictive minimum y value.
     * @param maxY The fictive maximum y value.
     * @param minZ The fictive minimum z value.
     * @param maxZ The fictive maximum z value.
     */
    void setDataSetToEmpty(double minX, double maxX, double minY, double maxY, double minZ, double maxZ);

    /**
     * Set the message to be shown when the data set is <code>null</code> or empty.
     * 
     * @param message The message to show.
     */
    void setNoDataMessage(String message);

    /**
     * Return the message to be shown when the data set is <code>null</code> or empty.
     * 
     * @return The message to be shown when the data set is <code>null</code> or empty.
     */
    String getNoDataMessage();

    /**
     * Reset the transformation (rotation, translation, zoom) to the default state.
     */
    void resetTransformation();

    /**
     * Zooms the view in.
     */
    void zoomIn();

    /**
     * Zooms the view out.
     */
    void zoomOut();

    /**
     * Zooms the view to see all the data.
     */
    void setAutoRange();
}
