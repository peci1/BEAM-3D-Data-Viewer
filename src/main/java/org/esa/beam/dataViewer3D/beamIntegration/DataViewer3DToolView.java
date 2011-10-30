/**
 * 
 */
package org.esa.beam.dataViewer3D.beamIntegration;

import static org.esa.beam.dataViewer3D.utils.NumberTypeUtils.castToType;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.esa.beam.dataViewer3D.data.color.ColorProvider;
import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.AbstractDataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;
import org.esa.beam.dataViewer3D.data.source.BandDataSource;
import org.esa.beam.dataViewer3D.gui.DataViewer;
import org.esa.beam.dataViewer3D.gui.GraphicalDataViewer;
import org.esa.beam.dataViewer3D.gui.ImageCaptureCallback;
import org.esa.beam.dataViewer3D.gui.JOGLDataViewer;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.ProductNodeList;
import org.esa.beam.framework.ui.AbstractDialog;
import org.esa.beam.framework.ui.application.support.AbstractToolView;
import org.esa.beam.framework.ui.product.ProductTreeListenerAdapter;
import org.esa.beam.util.io.BeamFileChooser;
import org.esa.beam.visat.VisatApp;
import org.jfree.ui.ExtensionFileFilter;

import com.bc.ceres.core.ProgressMonitor;
import com.bc.ceres.swing.TableLayout;
import com.bc.ceres.swing.progress.DialogProgressMonitor;

/**
 * A tool view that displays the 3D data viewer.
 * 
 * @author Martin Pecka
 */
public class DataViewer3DToolView extends AbstractToolView
{

    /** The container for the DataViewer or its placeholder button */
    protected final JPanel              viewerPane     = new JPanel();
    /** The bands this data viewer uses to generate the source data. */
    protected final List<Band>          involvedBands  = new LinkedList<Band>();
    /** The expression denoting the mask for valid pixels to be read. */
    protected String                    maskExpression = "";
    /** The maximum number of displayed points. */
    protected Integer                   maxPoints      = null;
    /** The data viewer used to display the data. */
    protected final GraphicalDataViewer dataViewer     = createDataViewer();
    /** The popup menu of the viewer panel. */
    protected final JPopupMenu          popupMenu      = new JPopupMenu();
    /** The button for updating the view. */
    protected AbstractButton            updateViewButton;

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
        updateViewButton = new JButton(new UpdateViewAction());
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

        setupPopupMenu();

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
     * Set the given bands as the source for this viewer.
     * <p>
     * This method doesn't update the view.
     * <p>
     * Call
     * {@link #createCoordinatesSystemForCurrentDataSet(Double, Double, String, Double, Double, String, Double, Double, String, Double, Double, String, ColorProvider)}
     * if needed.
     * 
     * @param bandX The band used for the x axis.
     * @param bandY The band used for the y axis.
     * @param bandZ The band used for the z axis.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     */
    public void setBands(Band bandX, Band bandY, Band bandZ, ProgressMonitor progressMonitor)
    {
        setBands(bandX, bandY, bandZ, null, progressMonitor);
    }

    /**
     * Set the given bands as the source for this viewer.
     * <p>
     * This method doesn't update the view.
     * <p>
     * Call
     * {@link #createCoordinatesSystemForCurrentDataSet(Double, Double, String, Double, Double, String, Double, Double, String, Double, Double, String, ColorProvider)}
     * if needed.
     * 
     * @param bandX The band used for the x axis.
     * @param bandY The band used for the y axis.
     * @param bandZ The band used for the z axis.
     * @param bandW The band used for the w axis.
     * @param progressMonitor The progress monitor, which will be notified about progress, if not <code>null</code>.
     */
    public void setBands(Band bandX, Band bandY, Band bandZ, Band bandW, ProgressMonitor progressMonitor)
    {
        DataSet dataSet;
        try {
            if (bandW == null) {
                // TODO allow adjusting the precision
                dataSet = AbstractDataSet.createFromDataSources(maxPoints, BandDataSource.createForBand(bandX, 10),
                        BandDataSource.createForBand(bandY, 10), BandDataSource.createForBand(bandZ, 10),
                        progressMonitor);
            } else {
                // TODO allow adjusting the precision
                dataSet = AbstractDataSet.createFromDataSources(maxPoints, BandDataSource.createForBand(bandX, 10),
                        BandDataSource.createForBand(bandY, 10), BandDataSource.createForBand(bandZ, 10),
                        BandDataSource.createForBand(bandW, 10), progressMonitor);
            }
        } catch (CancellationException e) {
            return;
        }

        involvedBands.clear();
        involvedBands.add(bandX);
        involvedBands.add(bandY);
        involvedBands.add(bandZ);
        if (bandW != null)
            involvedBands.add(bandW);

        dataViewer.setDataSet(dataSet);
        updateViewButton.setEnabled(true);
    }

    /**
     * Create a default coordinates system for the current data set, where the bounds are computed from the data set's
     * minima and maxima.
     */
    public void createDefaultCoordinatesSystemForCurrentDataSet()
    {
        dataViewer.setCoordinatesSystem(CoordinatesSystem.createDefaultCoordinatesSystem(dataViewer.getDataSet()));
        dataViewer.getCoordinatesSystem().setShowGrid(true);
    }

    /**
     * Create a coordinates system for the current data set. Non-null parameters are used as bounds, <code>null</code>
     * parameters are autocomputed from the data set's minima and maxima.
     * 
     * @param xMin Minimum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xMax Maximum value for x axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param xLabel Label of the x axis.
     * @param yMin Minimum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yMax Maximum value for y axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param yLabel Label of the y axis.
     * @param zMin Minimum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zMax Maximum value for z axis. <code>null</code> means to autocompute the value based on the dataset data.
     * @param zLabel Label of the z axis.
     * @param wMin Minimum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     *            Pass <code>null</code> if the data set is only 3-dimensional.
     * @param wMax Maximum value for w axis. <code>null</code> means to autocompute the value based on the dataset data.
     *            Pass <code>null</code> if the data set is only 3-dimensional.
     * @param wLabel Label of the w axis. Pass <code>null</code> if the data set is only 3-dimensional
     * @param colorProvider The color provider for coloring data points.
     */
    @SuppressWarnings("unchecked")
    public <X extends Number, Y extends Number, Z extends Number, W extends Number> void createCoordinatesSystemForCurrentDataSet(
            Double xMin, Double xMax, String xLabel, Double yMin, Double yMax, String yLabel, Double zMin, Double zMax,
            String zLabel, Double wMin, Double wMax, String wLabel, ColorProvider colorProvider)
    {
        if (dataViewer.getDataSet() instanceof DataSet3D<?, ?, ?>) {
            DataSet3D<X, Y, Z> dataSet = (DataSet3D<X, Y, Z>) dataViewer.getDataSet();
            dataViewer.setCoordinatesSystem(CoordinatesSystem.createCoordinatesSystem(
                    castToType(dataSet.getMinX(), xMin), castToType(dataSet.getMinX(), xMax), xLabel,
                    castToType(dataSet.getMinY(), yMin), castToType(dataSet.getMaxY(), yMax), yLabel,
                    castToType(dataSet.getMinZ(), zMin), castToType(dataSet.getMaxZ(), zMax), zLabel, dataSet));
        } else {
            DataSet4D<X, Y, Z, W> dataSet = (DataSet4D<X, Y, Z, W>) dataViewer.getDataSet();
            dataViewer.setCoordinatesSystem(CoordinatesSystem.createCoordinatesSystem(
                    castToType(dataSet.getMinX(), xMin), castToType(dataSet.getMinX(), xMax), xLabel,
                    castToType(dataSet.getMinY(), yMin), castToType(dataSet.getMaxY(), yMax), yLabel,
                    castToType(dataSet.getMinZ(), zMin), castToType(dataSet.getMaxZ(), zMax), zLabel,
                    castToType(dataSet.getMinW(), wMin), castToType(dataSet.getMaxW(), wMax), wLabel, dataSet));
        }
        dataViewer.getCoordinatesSystem().setShowGrid(true);
        dataViewer.getCoordinatesSystem().setColorProvider(colorProvider);
    }

    /**
     * Update the view (reread the source bands and display the new data).
     */
    public void updateView()
    {
        updateView(true);
    }

    /**
     * Update the view (redraw).
     * 
     * @param reloadData If true, also reload the source data.
     */
    public void updateView(boolean reloadData)
    {
        if (reloadData) {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    final ProgressMonitor progressMonitor = createBandsLoadingProgressMonitor();
                    if (involvedBands.size() == 3) {
                        setBands(involvedBands.get(0), involvedBands.get(2), involvedBands.get(2), progressMonitor);
                    } else if (involvedBands.size() == 4) {
                        setBands(involvedBands.get(0), involvedBands.get(2), involvedBands.get(2),
                                involvedBands.get(3), progressMonitor);
                    }
                    dataViewer.update();
                }
            }).start();
        } else {
            dataViewer.update();
        }
    }

    /**
     * Creates the data viewer instance to be used within this class.
     * 
     * @return The data viewer.
     */
    private final GraphicalDataViewer createDataViewer()
    {
        GraphicalDataViewer viewer = createDataViewerImpl();
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
    protected GraphicalDataViewer createDataViewerImpl()
    {
        return new JOGLDataViewer();
    }

    /**
     * Create the right-click popup menu and register the needed mouse listeners.
     */
    protected void setupPopupMenu()
    {
        final JMenuItem saveImageMenuItem = new JMenuItem("Save as image", KeyEvent.VK_S); /* I18N */
        saveImageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final BeamFileChooser fileChooser = new BeamFileChooser();
                fileChooser.setDialogTitle("Export as image");/* I18N */
                fileChooser.setFileFilter(new ExtensionFileFilter("PNG images", "png"));
                if (fileChooser.showSaveDialog(getPaneWindow()) == JFileChooser.APPROVE_OPTION) {
                    dataViewer.saveImage(fileChooser.getSelectedFile(), "PNG", new ImageCaptureCallback() {

                        @Override
                        public void onOk()
                        {
                            VisatApp.getApp().setStatusBarMessage(
                                    "Image successfully saved to " + fileChooser.getSelectedFile().toString()); /* I18N */
                        }

                        @Override
                        public void onException(Exception e)
                        {
                            onFail();
                            VisatApp.getApp().getLogger().severe(e.toString());
                        }

                        @Override
                        public void onFail()
                        {
                            VisatApp.getApp()
                                    .showErrorDialog("Image not saved", "There was an error saving the image."); /* I18N */
                        }

                    });
                }
            }
        });
        final JMenuItem copyToClipboardMenuItem = new JMenuItem("Copy to clipboard", KeyEvent.VK_C); /* I18N */
        copyToClipboardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dataViewer.copyImageToClipboard(new ImageCaptureCallback() {
                    @Override
                    public void onOk()
                    {
                        VisatApp.getApp().setStatusBarMessage("Image copied to the clipboard."); /* I18N */
                    }

                    @Override
                    public void onFail()
                    {
                        VisatApp.getApp().showErrorDialog("Message not copied",
                                "There was an error copying the image to the clipboard."); /* I18N */
                    }

                    @Override
                    public void onException(Exception e)
                    {
                        onFail();
                        VisatApp.getApp().getLogger().severe(e.toString());
                    }
                });
            }
        });

        final JMenuItem resetViewItem = new JMenuItem("Reset the view", KeyEvent.VK_R);/* I18N */
        resetViewItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                dataViewer.resetTransformation();
            }
        });

        popupMenu.add(saveImageMenuItem);
        popupMenu.add(copyToClipboardMenuItem);
        popupMenu.add(resetViewItem);

        MouseAdapter popupMouseAdapter = new MouseAdapter() {
            private int       pixelsMoved                      = 0;
            private final int pixelsMovedToDisablePopupShowing = 25;
            private boolean   showPopup                        = false;
            private Integer   prevX                            = null, prevY = null;

            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                showPopup = false;
            }

            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (!SwingUtilities.isRightMouseButton(e))
                    return;

                int diffX = (prevX == null ? 0 : e.getX() - prevX);
                int diffY = (prevY == null ? 0 : e.getY() - prevY);
                prevX = e.getX();
                prevY = e.getY();
                pixelsMoved += Math.abs(diffX) + Math.abs(diffY);

                if (pixelsMoved >= pixelsMovedToDisablePopupShowing) {
                    showPopup = false;
                    if (popupMenu.isShowing()) {
                        popupMenu.setVisible(false);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                mouseDragged(e);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    showPopup = false;
                    return;
                }

                pixelsMoved = 0;
                showPopup = true;
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e)
            {
                if (pixelsMoved >= pixelsMovedToDisablePopupShowing) {
                    showPopup = false;
                    if (popupMenu.isShowing()) {
                        popupMenu.setVisible(false);
                    }
                    return;
                }
                if (showPopup && e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
        ((JComponent) dataViewer).addMouseListener(popupMouseAdapter);
        ((JComponent) dataViewer).addMouseMotionListener(popupMouseAdapter);
        ((JComponent) dataViewer).addMouseWheelListener(popupMouseAdapter);
    }

    /**
     * Return the progress monitor for monitoring the loading of bands.
     * 
     * @return The progress monitor for monitoring the loading of bands.
     */
    protected ProgressMonitor createBandsLoadingProgressMonitor()
    {
        ProgressMonitor progressMonitor = new DialogProgressMonitor(getControl(), "Reading data", ModalityType.MODELESS); /* I18N */
        return progressMonitor;
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
            final Product[] prods = VisatApp.getApp().getProductManager().getProducts();
            final ProductNodeList<Product> products = new ProductNodeList<Product>();
            for (Product prod : prods) {
                products.add(prod);
            }

            final BandSelectionDialog bandSelectionDialog = new BandSelectionDialog(VisatApp.getApp(), VisatApp
                    .getApp().getSelectedProduct(), products, involvedBands, maskExpression, "");
            if (bandSelectionDialog.show() == AbstractDialog.ID_OK) {
                if (!(viewerPane.getComponent(0) instanceof DataViewer)) {
                    // the "select bands" button is displayed, so we remove it and display a message instead
                    viewerPane.removeAll();
                    final JLabel label = new JLabel("Loading..."); /* I18N */
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    viewerPane.add(label, BorderLayout.CENTER);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        maxPoints = bandSelectionDialog.getMaxPoints();

                        final ProgressMonitor progressMonitor = createBandsLoadingProgressMonitor();

                        final List<Band> bands = bandSelectionDialog.getBands();
                        if (bands != null && bands.size() > 0) {
                            if (bands.size() == 3) {
                                setBands(bands.get(0), bands.get(1), bands.get(2), progressMonitor);
                            } else if (bands.size() == 4) {
                                setBands(bands.get(0), bands.get(1), bands.get(2), bands.get(3), progressMonitor);
                            }
                            maskExpression = bandSelectionDialog.getMaskExpression();
                            createCoordinatesSystemForCurrentDataSet(bandSelectionDialog.getxMin(),
                                    bandSelectionDialog.getxMax(), bandSelectionDialog.getXLabel(),
                                    bandSelectionDialog.getyMin(), bandSelectionDialog.getyMax(),
                                    bandSelectionDialog.getYLabel(), bandSelectionDialog.getzMin(),
                                    bandSelectionDialog.getzMax(), bandSelectionDialog.getZLabel(),
                                    bandSelectionDialog.getwMin(), bandSelectionDialog.getwMax(),
                                    bandSelectionDialog.getWLabel(), bandSelectionDialog.getColorProvider());

                            // if the viewer hasn't been displayed yet
                            if (!(viewerPane.getComponent(0) instanceof DataViewer)) {
                                viewerPane.removeAll();
                                viewerPane.add((JComponent) dataViewer); // dataViewer is surely a JComponent (see
                                                                         // createDataViewer() )
                            }
                            updateView(false);
                        }
                    }
                }).start();
            }
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
