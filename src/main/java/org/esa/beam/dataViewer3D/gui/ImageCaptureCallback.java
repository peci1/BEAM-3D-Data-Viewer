/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

/**
 * A callback called when an image has been captured and persisted somehow.
 * 
 * @author Martin Pecka
 */
public interface ImageCaptureCallback
{
    /**
     * Called when the image has been successfully persisted.
     */
    void onOk();

    /**
     * Called when an exception was thrown during the persisting.
     * 
     * @param e The exception thrown.
     */
    void onException(Exception e);

    /**
     * Called when the persisting failed without an exception.
     */
    void onFail();
}