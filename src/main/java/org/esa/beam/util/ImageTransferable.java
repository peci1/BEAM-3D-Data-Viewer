/*
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
 * 
 * Project Info: http://www.jfree.org/jfreechart/index.html
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 * 
 * -------------------
 * ChartSelection.java
 * -------------------
 * (C) Copyright 2009, by Object Refinery Limited.
 * 
 * Original Author: David Gilbert (for Object Refinery Limited);
 * Contributor(s): Martin Pecka;
 * 
 * Changes
 * -------
 * 08-Apr-2009 : Version 1, with inspiration from patch 1460845 (DG);
 * 11-Oct-2011: Changed to handle basically all images, not only charts.
 */

package org.esa.beam.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A class used to represent a {@link BufferedImage} on the clipboard.
 */
public class ImageTransferable implements Transferable
{

    /** The data flavor. */
    final DataFlavor    dataFlavor = DataFlavor.imageFlavor;

    /** The represented image. */
    final BufferedImage image;

    /**
     * Creates a new image transferable.
     * 
     * @param image The represented image.
     */
    public ImageTransferable(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] { this.dataFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return this.dataFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (isDataFlavorSupported(flavor)) {
            return this.image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

}
