/**
 * 
 */
package org.esa.beam.dataViewer3D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.AbstractDataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.source.RandomDataSource;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.gui.JOGLDataViewer;

/**
 * A prototype of a window displaying the 3D data viewer.
 * 
 * @author Martin Pecka
 */
public class PrototypeDisplayWindow extends JFrame
{

    /** */
    private static final long serialVersionUID = -7351115430750561646L;

    /**
     * Run the sample prototype window.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                final PrototypeDisplayWindow window = new PrototypeDisplayWindow();
                window.setDefaultCloseOperation(EXIT_ON_CLOSE);
                window.setTitle("BEAM 3D Data Viewer");
                window.createAndSetupGUI();
                window.pack();
                window.setVisible(true);
            }
        });
    }

    /**
     * Populate the window with Swing components.
     */
    public void createAndSetupGUI()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu dataMenu = new JMenu("Data");
        menuBar.add(dataMenu);

        JMenuItem setupBandsMenuItem = new JMenuItem("Setup displayed bands");
        dataMenu.add(setupBandsMenuItem);

        JMenuItem updateMenuItem = new JMenuItem("Update view");
        dataMenu.add(updateMenuItem);

        JPanel dataViewerWrapper = new JPanel(new BorderLayout());
        dataViewerWrapper.setPreferredSize(new Dimension(600, 600));
        add(dataViewerWrapper, BorderLayout.CENTER);

        // final TextDataViewer viewer = new TextDataViewer();
        // JScrollPane scrollPane = new JScrollPane(viewer);
        // scrollPane.setPreferredSize(new Dimension(550, 550));
        // dataViewerWrapper.add(scrollPane);
        // viewer.setText("Loading data");
        // new Thread(new Runnable() {
        // @Override
        // public void run()
        // {
        // viewer.setDataSet(createDataSet());
        // viewer.setCoordinatesSystem(createCoordinatesSystem(viewer.getDataSet()));
        // System.gc();
        // }
        // }).start();

        final JOGLDataViewer viewer = new JOGLDataViewer();
        dataViewerWrapper.add(viewer);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                viewer.setDataSet(createDataSet());
                viewer.setCoordinatesSystem(createCoordinatesSystem(viewer.getDataSet()));
                viewer.getCoordinatesSystem().setShowGrid(true);
                System.gc();
            }
        }).start();
    }

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

        return AbstractDataSet.createFromDataSources(20000, src1, src2, src3, null);
    }

    private CoordinatesSystem createCoordinatesSystem(DataSet dataSet)
    {
        return CoordinatesSystem.createDefaultCoordinatesSystem(dataSet);
    }
}
