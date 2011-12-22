/**
 * 
 */
package org.esa.beam.framework.param.editors;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.esa.beam.framework.param.ParamValidateException;
import org.esa.beam.framework.param.Parameter;
import org.esa.beam.framework.ui.GridBagUtils;
import org.jfree.ui.FontChooserPanel;

/**
 * Editor for {@link Font} values.
 * <p>
 * Has a dependecy of JFreeChart.
 * 
 * @author Martin Pecka
 */
public class FontEditor extends TextFieldEditor
{

    protected JPanel component;

    /**
     * @param parameter
     */
    public FontEditor(Parameter parameter)
    {
        super(parameter);
    }

    @Override
    protected void initUI()
    {
        super.initUI();

        component = GridBagUtils.createPanel();
        final GridBagConstraints gbc = GridBagUtils.createConstraints("fill=HORIZONTAL,weightx=1,anchor=NORTHWEST");
        GridBagUtils.addToPanel(component, getTextComponent(), gbc, "gridx=0,gridy=0,fill=BOTH");
        final JButton selectButton = new JButton("Select...");/* I18N */
        selectButton.addActionListener(new FontSelectionAction());
        GridBagUtils.addToPanel(component, selectButton, gbc, "gridx=1,gridy=0");

        getTextComponent().setEnabled(false);
        getTextComponent().setFont(getFont());
    }

    @Override
    public void updateUI()
    {
        super.updateUI();
        getTextComponent().setEnabled(false);
        getTextComponent().setFont(getFont());
    }

    /**
     * @return The value of this editor.
     */
    public Font getFont()
    {
        return (Font) getParameter().getValue();
    }

    @Override
    public JComponent getComponent()
    {
        return component;
    }

    /**
     * Action for font selection.
     * 
     * @author Martin Pecka
     */
    protected class FontSelectionAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            FontChooserPanel panel = new FontChooserPanel((Font) getParameter().getValue());
            int result = JOptionPane.showConfirmDialog(getComponent().getRootPane(), panel, "Font Selection",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); /* I18N */

            if (result == JOptionPane.OK_OPTION) {
                try {
                    getParameter().setValue(panel.getSelectedFont());
                } catch (ParamValidateException e1) {
                    JOptionPane.showMessageDialog(getComponent().getRootPane(), "The selected font is invalid.",
                            "Invalid font", JOptionPane.ERROR_MESSAGE); /* I18N */
                }
            }
        }
    }
}
