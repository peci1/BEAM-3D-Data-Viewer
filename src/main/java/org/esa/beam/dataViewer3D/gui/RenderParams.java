/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import java.awt.Color;
import java.awt.Font;

/**
 * Parameters for chart rendering.
 * 
 * @author Martin Pecka
 */
public class RenderParams
{
    public boolean         showTitle       = true;
    public Font            titleFont       = Font.decode("Arial");
    public Color           titleColor      = Color.black;
    public final Font[]    axisTitleFont   = new Font[] { titleFont, titleFont, titleFont, titleFont };
    public final Color[]   axisTitleColor  = new Color[] { titleColor, titleColor, titleColor, titleColor };
    public final boolean[] axisShowLabels  = new boolean[] { true, true, true, true };
    public final boolean[] axisShowTicks   = new boolean[] { true, true, true, true };
    public final Font[]    axisLabelFont   = new Font[] { titleFont, titleFont, titleFont, titleFont };
    public Color           backgroundColor = Color.white;
    public boolean         showGrid        = true;
}
