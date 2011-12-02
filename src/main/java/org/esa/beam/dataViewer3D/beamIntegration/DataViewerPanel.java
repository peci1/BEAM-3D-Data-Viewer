/**
 * 
 */
package org.esa.beam.dataViewer3D.beamIntegration;

import org.esa.beam.dataViewer3D.gui.GraphicalDataViewer;

/**
 * A panel displaying a graphical data viewer.
 * 
 * @author Martin Pecka
 */
public interface DataViewerPanel
{
    /**
     * Return the data viewer.
     * 
     * @return The data viewer.
     */
    GraphicalDataViewer getDataViewer();
}
