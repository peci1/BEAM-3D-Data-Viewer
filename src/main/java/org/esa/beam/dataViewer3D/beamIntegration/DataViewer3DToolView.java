/**
 * 
 */
package org.esa.beam.dataViewer3D.beamIntegration;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.AbstractDataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.source.RandomDataSource;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.gui.DataViewer;
import org.esa.beam.dataViewer3D.gui.JOGLDataViewer;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.ui.application.support.AbstractToolView;
import org.esa.beam.framework.ui.product.ProductTreeListenerAdapter;
import org.esa.beam.visat.VisatApp;

import com.bc.ceres.swing.TableLayout;

/**
 * A tool view that displays the 3D data viewer.
 * 
 * @author Martin Pecka
 */
public class DataViewer3DToolView extends AbstractToolView
{

    /** The container for the DataViewer or its placeholder button */
    protected final JPanel     viewerPane    = new JPanel();
    /** The bands this data viewer uses to generate the source data. */
    protected final List<Band> involvedBands = new LinkedList<Band>();
    /** The data viewer used to display the data. */
    protected final DataViewer dataViewer    = createDataViewer();

    @Override
    protected JComponent createControl()
    {
        // create the top toolbar with control buttons
        final TableLayout layout = new TableLayout(2);
        layout.setTableAnchor(TableLayout.Anchor.WEST);
        layout.setTableFill(TableLayout.Fill.HORIZONTAL);
        layout.setTablePadding(new Insets(2, 2, 2, 2));
        layout.setTableWeightX(1.0);
        final JPanel buttons = new JPanel(layout);

        // AbstractButton selectBandsButton = ToolButtonFactory.createButton(new SelectBandsAction(), false);
        final AbstractButton selectBandsButton = new JButton(new SelectBandsAction());
        selectBandsButton.setText("Select bands"); /* I18N */// TODO change to icon
        selectBandsButton.setToolTipText("Select bands to create the interactive view from");
        selectBandsButton.setName(getContext().getPane().getControl().getName() + ".selectBands.button");
        selectBandsButton.setEnabled(false);

        // AbstractButton updateViewButton = ToolButtonFactory.createButton(new UpdateViewAction(), false);
        final AbstractButton updateViewButton = new JButton(new UpdateViewAction());
        updateViewButton.setText("Update view"); /* I18N */// TODO change to icon
        updateViewButton.setToolTipText("Update the view to reflect changes in the source bands");
        updateViewButton.setName(getContext().getPane().getControl().getName() + ".updateView.button");
        updateViewButton.setEnabled(false);

        buttons.add(selectBandsButton);
        buttons.add(updateViewButton);

        // create the viewer pane and fill it with the placeholder button at the startup time
        final AbstractButton noBandsPlaceholderButton = new JButton(new SelectBandsAction());
        noBandsPlaceholderButton.setText("Click here to select bands."); /* I18N */
        noBandsPlaceholderButton.setToolTipText("Click here to select bands.");/* I18N */
        noBandsPlaceholderButton.setEnabled(false);

        viewerPane.setLayout(new BorderLayout());
        viewerPane.add(noBandsPlaceholderButton, BorderLayout.CENTER);

        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttons, BorderLayout.NORTH);
        mainPanel.add(viewerPane, BorderLayout.CENTER);

        // update the enabled state of the buttons whenever a product is opened/closed
        VisatApp.getApp().addProductTreeListener(new ProductTreeListenerAdapter() {

            @Override
            public void productAdded(Product product)
            {
                updateState();
            }

            @Override
            public void productRemoved(Product product)
            {
                updateState();
            }

            /**
             * Update the state of the control buttons to reflect if there is at least one product open.
             */
            private void updateState()
            {
                boolean enabled = VisatApp.getApp().getProductTree().getModel().getRoot() != null;
                selectBandsButton.setEnabled(enabled);
                noBandsPlaceholderButton.setEnabled(enabled);
            }
        });

        return mainPanel;
    }

    /**
     * Set the given bands as the source for this viewer and update the view.
     * 
     * @param bandX The band used for the x axis.
     * @param bandY The band used for the y axis.
     * @param bandZ The band used for the z axis.
     */
    public void setBands(Band bandX, Band bandY, Band bandZ)
    {
        setBands(bandX, bandY, bandZ, null);
    }

    /**
     * Set the given bands as the source for this viewer and update the view.
     * 
     * @param bandX The band used for the x axis.
     * @param bandY The band used for the y axis.
     * @param bandZ The band used for the z axis.
     * @param bandW The band used for the w axis.
     */
    public void setBands(Band bandX, Band bandY, Band bandZ, Band bandW)
    {
        involvedBands.clear();
        involvedBands.add(bandX);
        involvedBands.add(bandY);
        involvedBands.add(bandZ);
        if (bandW != null)
            involvedBands.add(bandW);

        dataViewer.setDataSet(createDataSet());
        dataViewer.setCoordinatesSystem(CoordinatesSystem.createDefaultCoordinatesSystem(dataViewer.getDataSet()));
        dataViewer.getCoordinatesSystem().setShowGrid(true);

        viewerPane.removeAll();
        viewerPane.add((JComponent) dataViewer); // dataViewer is surely a JComponent (see createDataViewer() )

        updateView();
    }

    /**
     * Update the view (reread the source bands and display the new data).
     */
    public void updateView()
    {
        dataViewer.update();
    }

    /**
     * Creates the data viewer instance to be used within this class.
     * 
     * @return The data viewer.
     */
    private final DataViewer createDataViewer()
    {
        DataViewer viewer = createDataViewerImpl();
        if (!(viewer instanceof JComponent))
            throw new IllegalStateException(getClass()
                    + ": The viewer returned by createDataViewerImpl() must extend JComponent.");
        return viewer;
    }

    /**
     * Create an instance of the data viewer to be used within this class.
     * <p>
     * The returned object must extend {@link JComponent}!
     * 
     * @return The viewer instance.
     */
    protected DataViewer createDataViewerImpl()
    {
        return new JOGLDataViewer();
    }

    // TODO remove, dev only
    private DataSet createDataSet()
    {
        // final int size = Integer.MAX_VALUE / 2000;
        final int size = 20000;

        DataSource<Float> src1 = new RandomDataSource<Float>(size, -256f, 256f) {
            Random random = new Random();

            @Override
            protected Float getRandomValue()
            {
                return (float) (random.nextDouble() * (max - min) + min);
            }

            @Override
            protected NumericType<Float> getNumericType(Float number)
            {
                return new FloatType(number, 10);
            }
        };

        DataSource<Double> src2 = new RandomDataSource<Double>(size, 0d, 256d) {
            Random random = new Random();

            @Override
            protected Double getRandomValue()
            {
                return random.nextDouble() * (max - min) + min;
            }

            @Override
            protected NumericType<Double> getNumericType(Double number)
            {
                return new DoubleType(number, 2);
            }
        };

        DataSource<Byte> src3 = new RandomDataSource<Byte>(size, (byte) 0, (byte) 20) {
            Random random = new Random();

            @Override
            protected Byte getRandomValue()
            {
                return ((Long) Math.round(random.nextDouble() * (max - min) + min)).byteValue();
            }

            @Override
            protected NumericType<Byte> getNumericType(Byte number)
            {
                return new ByteType(number);
            }
        };

        return AbstractDataSet.createFromDataSources(20000, src1, src2, src3);
    }

    /**
     * The action for selecting the source bands.
     * 
     * @author Martin Pecka
     */
    private class SelectBandsAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -1042044060810157729L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setBands(null, null, null); // TODO dev stuff
        }

    }

    /**
     * The action that updates the view.
     * 
     * @author Martin Pecka
     */
    private class UpdateViewAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = 6339280477468610781L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            updateView();
        }

    }
}
