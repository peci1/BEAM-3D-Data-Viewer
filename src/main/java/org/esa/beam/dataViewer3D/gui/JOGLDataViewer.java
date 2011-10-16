/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import jahuwaldt.gl.Matrix;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.esa.beam.dataViewer3D.data.axis.Axis;
import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;
import org.esa.beam.dataViewer3D.data.grid.Grid;
import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.type.NumericType;
import org.esa.beam.dataViewer3D.utils.NumberTypeUtils;

import com.sun.opengl.util.j2d.TextRenderer;

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
    private final double          nearClippingPlane = 1;
    private final double          farClippingPlane  = 10000;
    /** Vertical field of view in degrees. */
    private final double          fovVertical       = 45;

    private double[]              transform         = Matrix.identity();
    private double[]              center            = new double[] { 0, 0, 0 };
    private double[]              minPoint          = new double[] { 0, 0, 0 };
    private double[]              maxPoint          = new double[] { 0, 0, 0 };
    private double[]              projectionMatrix  = new double[16];

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

                // have lines antialiased
                gl.glEnable(GL.GL_LINE_SMOOTH);
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

                gl.glLineWidth(1.5f);
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

                Axis<?>[] aces = new Axis<?>[] { coordinatesSystem.getAces()[0], coordinatesSystem.getAces()[1],
                        coordinatesSystem.getAces()[2] };

                double maxLength = NumberTypeUtils.max(aces[0].getLength(), aces[1].getLength(), aces[2].getLength())
                        .doubleValue();

                // Perspective.
                float widthHeightRatio = (float) getWidth() / (float) getHeight();
                glu.gluPerspective(fovVertical, widthHeightRatio, nearClippingPlane, farClippingPlane);
                glu.gluLookAt(0, 0, 1.25 * maxLength / (2 * Math.tan(0.5 * fovVertical * (2 * Math.PI) / 360)), 0, 0,
                        0, 0, 1, 0);

                gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projectionMatrix, 0);

                // Change back to model view matrix.
                gl.glMatrixMode(GL.GL_MODELVIEW);
                gl.glLoadMatrixd(transform, 0);

                // the drawing methods
                drawGrid(gl, coordinatesSystem.getGrid());
                drawDataset(gl);
                drawAces(gl, glu, aces);
            }

            private void drawAces(GL gl, GLU glu, Axis<?>[] aces)
            {
                // in order to set different line widths we must call begin/end methods between the width changes
                // gl.glBegin(GL.GL_LINES);
                {
                    TextRenderer textRend = new TextRenderer(Font.decode("Arial-bold-14"), false, true, null, true);
                    int[] viewport = new int[4];
                    double[] point = new double[3];
                    double[] vertices = new double[3];
                    Rectangle2D textBounds;
                    gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

                    for (int i = 0; i < 3; i++) {
                        vertices[0] = aces[0].getMin().doubleValue();
                        vertices[1] = aces[1].getMin().doubleValue();
                        vertices[2] = aces[2].getMin().doubleValue();
                        gl.glBegin(GL.GL_LINES);
                        gl.glColor3f(0f, 0f, 0f);
                        gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                        vertices[i] = vertices[i] + aces[i].getLength().doubleValue();
                        gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                        gl.glEnd();

                        gl.glLineWidth(1f);

                        vertices[0] = aces[0].getMin().doubleValue();
                        vertices[1] = aces[1].getMin().doubleValue();
                        vertices[2] = aces[2].getMin().doubleValue();

                        textRend.beginRendering(getWidth(), getHeight());
                        gl.glColor3f(0f, 0f, 0f);
                        glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                viewport, 0, point, 0);
                        String originText = "[" + aces[0].getTickLabels()[0] + "; " + aces[1].getTickLabels()[0] + "; "
                                + aces[2].getTickLabels()[0] + "]";
                        textBounds = textRend.getBounds(originText);
                        textRend.draw(originText, (int) (point[0] - textBounds.getWidth()),
                                (int) (point[1] - textBounds.getHeight()));
                        textRend.endRendering();

                        Number[] ticks = aces[i].getTicks();
                        String[] tickLabels = aces[i].getTickLabels();

                        for (int l = 1; l < ticks.length; l++) {
                            vertices[0] = aces[0].getMin().doubleValue();
                            vertices[1] = aces[1].getMin().doubleValue();
                            vertices[2] = aces[2].getMin().doubleValue();
                            vertices[i] = ticks[l].doubleValue();
                            gl.glBegin(GL.GL_LINES);
                            gl.glColor3f(0f, 0f, 0f);
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            vertices[(i + 2) % 3] += aces[i].getTickLength().doubleValue();
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            vertices[(i + 2) % 3] -= aces[i].getTickLength().doubleValue();
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            vertices[(i + 1) % 3] += aces[i].getTickLength().doubleValue();
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            gl.glEnd();

                            vertices[0] = aces[0].getMin().doubleValue();
                            vertices[1] = aces[1].getMin().doubleValue();
                            vertices[2] = aces[2].getMin().doubleValue();
                            vertices[i] = ticks[l].doubleValue();

                            textRend.beginRendering(getWidth(), getHeight());
                            gl.glColor3f(0f, 0f, 0f);
                            glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                    viewport, 0, point, 0);
                            textBounds = textRend.getBounds(tickLabels[l]);
                            textRend.draw(tickLabels[l], (int) (point[0] - textBounds.getWidth()),
                                    (int) (point[1] - textBounds.getHeight()));
                            textRend.endRendering();
                        }
                        gl.glLineWidth(1.5f);
                    }
                }
                // gl.glEnd();
            }

            private void drawDataset(GL gl)
            {
                float maxDistance = (float) Math.sqrt(256 * 256 + 256 * 256 + 20 * 20); // TODO dev stuff

                gl.glBegin(GL.GL_POINTS);
                if (dataSet instanceof DataSet3D<?, ?, ?>) {
                    DataSet3D<?, ?, ?> dataSet3D = (DataSet3D<?, ?, ?>) dataSet;
                    for (Iterator<? extends DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>>> it = dataSet3D
                            .pointIterator(); it.hasNext();) {
                        DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>> point = it
                                .next();
                        // TODO dev stuff
                        float color = (float) Math.sqrt(Math.pow(point.getX().getNumber().doubleValue(), 2)
                                + Math.pow(point.getY().getNumber().doubleValue(), 2)
                                + Math.pow(point.getZ().getNumber().doubleValue(), 2))
                                / maxDistance;
                        // TODO dev stuff
                        gl.glColor3f(point.getX().getNumber().floatValue() / 256f, point.getY().getNumber()
                                .floatValue() / 256f, color);
                        gl.glVertex3d(point.getX().getNumber().doubleValue(), point.getY().getNumber().doubleValue(),
                                point.getZ().getNumber().doubleValue());
                    }
                } else {

                }
                gl.glEnd();
            }

            private void drawGrid(GL gl, Grid grid)
            {
                gl.glBegin(GL.GL_LINES);
                gl.glColor3f(0.5f, 0.5f, 0.5f);

                for (double[] line : grid.getGridLines()) {
                    gl.glVertex3dv(line, 0);
                    gl.glVertex3dv(line, 3);
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
            minPoint[0] = d3.getMinX().doubleValue();
            minPoint[1] = d3.getMinY().doubleValue();
            minPoint[2] = d3.getMinZ().doubleValue();

            maxPoint[0] = d3.getMaxX().doubleValue();
            maxPoint[1] = d3.getMaxY().doubleValue();
            maxPoint[2] = d3.getMaxZ().doubleValue();

            center[0] = d3.getMinX().doubleValue() + (d3.getMaxX().doubleValue() - d3.getMinX().doubleValue()) / 2;
            center[1] = d3.getMinY().doubleValue() + (d3.getMaxY().doubleValue() - d3.getMinY().doubleValue()) / 2;
            center[2] = d3.getMinZ().doubleValue() + (d3.getMaxZ().doubleValue() - d3.getMinZ().doubleValue()) / 2;
            transform = Matrix.translate(-center[0], -center[1], -center[2], Matrix.identity());
        } else if (dataSet instanceof DataSet4D<?, ?, ?, ?>) {
            DataSet4D<?, ?, ?, ?> d4 = (DataSet4D<?, ?, ?, ?>) dataSet;
            minPoint[0] = d4.getMinX().doubleValue();
            minPoint[1] = d4.getMinY().doubleValue();
            minPoint[2] = d4.getMinZ().doubleValue();

            maxPoint[0] = d4.getMaxX().doubleValue();
            maxPoint[1] = d4.getMaxY().doubleValue();
            maxPoint[2] = d4.getMaxZ().doubleValue();

            center[0] = d4.getMinX().doubleValue() + (d4.getMaxX().doubleValue() - d4.getMinX().doubleValue()) / 2;
            center[1] = d4.getMinY().doubleValue() + (d4.getMaxY().doubleValue() - d4.getMinY().doubleValue()) / 2;
            center[2] = d4.getMinZ().doubleValue() + (d4.getMaxZ().doubleValue() - d4.getMinZ().doubleValue()) / 2;
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
