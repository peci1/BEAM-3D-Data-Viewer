/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import jahuwaldt.gl.Matrix;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.esa.beam.dataViewer3D.data.axis.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;
import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * A data viewer displaying the data in a 3D form.
 * 
 * @author Martin Pecka
 */
public class JOGLDataViewer extends JPanel implements DataViewer
{

    /** */
    private static final long     serialVersionUID  = -4470588776978542020L;

    private DataSet               dataSet           = null;
    private CoordinatesSystem     coordinatesSystem = null;
    private final GLCanvas        canvas;
    private final GLEventListener glEventListener;

    private double[]              transform         = Matrix.identity();
    private double[]              center            = new double[] { 0, 0, 0 };

    public JOGLDataViewer()
    {
        setLayout(new BorderLayout());

        glEventListener = new GLEventListener() {
            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
            {
                GL gl = drawable.getGL();
                gl.glViewport(0, 0, width, height);
            }

            @Override
            public void init(GLAutoDrawable drawable)
            {
                GL gl = drawable.getGL();

                // set erase color
                gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // white

                // set drawing color and point size
                gl.glColor3f(0.0f, 0.0f, 0.0f);
                gl.glPointSize(4.0f); // a 'dot' is 4 by 4 pixels

                canvas.display();
            }

            @Override
            public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
            {
                canvas.display();
            }

            @Override
            public void display(GLAutoDrawable drawable)
            {
                GL gl = drawable.getGL();
                GLU glu = new GLU();

                gl.glClear(GL.GL_COLOR_BUFFER_BIT);

                gl.glMatrixMode(GL.GL_PROJECTION);
                gl.glLoadIdentity();

                // Perspective.
                float widthHeightRatio = (float) getWidth() / (float) getHeight();
                glu.gluPerspective(45, widthHeightRatio, 1, 10000);
                glu.gluLookAt(0, 0, 1000, 0, 0, 0, 0, 1, 0);

                // Change back to model view matrix.
                gl.glMatrixMode(GL.GL_MODELVIEW);
                gl.glLoadMatrixd(transform, 0);

                float maxDistance = (float) Math.sqrt(600 * 600 + 600 * 600 + 254 * 254);

                gl.glBegin(GL.GL_POINTS);
                if (dataSet instanceof DataSet3D<?, ?, ?>) {
                    DataSet3D<?, ?, ?> dataSet3D = (DataSet3D<?, ?, ?>) dataSet;
                    for (Iterator<? extends DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>>> it = dataSet3D
                            .pointIterator(); it.hasNext();) {
                        DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>> point = it
                                .next();
                        float color = (float) Math.sqrt(Math.pow(point.getX().getNumber().doubleValue(), 2)
                                + Math.pow(point.getY().getNumber().doubleValue(), 2)
                                + Math.pow(point.getZ().getNumber().doubleValue(), 2))
                                / maxDistance;
                        gl.glColor3f(point.getX().getNumber().floatValue() / 600f, point.getY().getNumber()
                                .floatValue() / 600f, color);
                        gl.glVertex3d(point.getX().getNumber().doubleValue(), point.getY().getNumber().doubleValue(),
                                point.getZ().getNumber().doubleValue());
                    }
                } else {

                }
                gl.glEnd();
            }
        };

        GLCapabilities capabilities = new GLCapabilities();
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(glEventListener);
        MouseAdapter mouseListener = new MouseAdapter() {
            private Integer      prevX      = null, prevY = null;
            private final double pxToAngle  = 0.03;
            private int          zRotSignum = 1;

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    prevX = e.getX();
                    prevY = e.getY();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (!SwingUtilities.isLeftMouseButton(e))
                    return;

                int diffX = (prevX == null ? 0 : e.getX() - prevX);
                int diffY = (prevY == null ? 0 : e.getY() - prevY);
                prevX = e.getX();
                prevY = e.getY();

                if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) > 0) {
                    // translate
                    Matrix.translate(diffX, -diffY, 0, transform);
                } else {
                    // rotate

                    if ((e.getModifiersEx() & MouseEvent.ALT_DOWN_MASK) == 0) {
                        double[] transformedCenter = Matrix.transform(transform, center);

                        Matrix.translate(-transformedCenter[0], -transformedCenter[1], -transformedCenter[2], transform);
                        Matrix.rotateX(diffY * pxToAngle, transform);
                        Matrix.rotateY(diffX * pxToAngle, transform);
                        Matrix.translate(transformedCenter[0], transformedCenter[1], transformedCenter[2], transform);
                    } else {
                        // let the left-right movement define the direction of the rotation
                        int prevSign = zRotSignum;
                        zRotSignum = (int) Math.signum(diffX);
                        if (zRotSignum == 0)
                            zRotSignum = prevSign;

                        Matrix.rotateZ(Math.sqrt(diffX * diffX + diffY * diffY) * -zRotSignum * pxToAngle, transform);
                    }
                }

                update();
            }
        };
        canvas.addMouseMotionListener(mouseListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                // zoom
                double shift = e.getWheelRotation()
                        * ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == 0 ? 10 : 100);

                Matrix.translate(0, 0, shift, transform);

                update();
            }
        });

        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Update the view.
     */
    public void update()
    {
        canvas.display();
    }

    @Override
    public DataSet getDataSet()
    {
        return dataSet;
    }

    @Override
    public void setDataSet(DataSet dataSet)
    {
        Object old = this.dataSet;
        this.dataSet = dataSet;

        if (dataSet instanceof DataSet3D<?, ?, ?>) {
            DataSet3D<?, ?, ?> d3 = (DataSet3D<?, ?, ?>) dataSet;
            center[0] = (d3.getMaxX().doubleValue() - d3.getMinX().doubleValue()) / 2;
            center[1] = (d3.getMaxY().doubleValue() - d3.getMinY().doubleValue()) / 2;
            center[2] = (d3.getMaxZ().doubleValue() - d3.getMinZ().doubleValue()) / 2;
            transform = Matrix.translate(-center[0], -center[1], -center[2], Matrix.identity());
        } else if (dataSet instanceof DataSet4D<?, ?, ?, ?>) {
            DataSet4D<?, ?, ?, ?> d4 = (DataSet4D<?, ?, ?, ?>) dataSet;
            center[0] = (d4.getMaxX().doubleValue() - d4.getMinX().doubleValue()) / 2;
            center[1] = (d4.getMaxY().doubleValue() - d4.getMinY().doubleValue()) / 2;
            center[2] = (d4.getMaxZ().doubleValue() - d4.getMinZ().doubleValue()) / 2;
            transform = Matrix.translate(-center[0], -center[1], -center[2], Matrix.identity());
        }

        if (old != null)
            update();
    }

    @Override
    public CoordinatesSystem getCoordinatesSystem()
    {
        return coordinatesSystem;
    }

    @Override
    public void setCoordinatesSystem(CoordinatesSystem coordinatesSystem)
    {
        Object old = this.coordinatesSystem;
        this.coordinatesSystem = coordinatesSystem;
        if (old != null)
            update();
    }

}
