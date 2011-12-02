/**
 * 
 */
package org.esa.beam.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A document listener performing a single action on remove/insert/update.
 * 
 * @author Martin Pecka
 */
public abstract class UniversalDocumentListener implements DocumentListener
{

    /**
     * The action to be performed.
     */
    protected abstract void update();

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        update();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        update();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        update();
    }

}
