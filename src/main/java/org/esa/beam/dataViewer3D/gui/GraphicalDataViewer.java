/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import java.io.File;

/**
 * A data viewer displaying the files graphically.
 * 
 * @author Martin Pecka
 */
public interface GraphicalDataViewer extends DataViewer
{
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
}
