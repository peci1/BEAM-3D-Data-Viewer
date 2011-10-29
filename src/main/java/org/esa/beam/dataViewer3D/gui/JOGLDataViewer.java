/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import jahuwaldt.gl.Matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.esa.beam.dataViewer3D.data.axis.Axis;
import org.esa.beam.dataViewer3D.data.color.ColorProvider;
import org.esa.beam.dataViewer3D.data.coordinates.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
import org.esa.beam.dataViewer3D.data.dataset.DataSet4D;
import org.esa.beam.dataViewer3D.data.grid.Grid;
import org.esa.beam.dataViewer3D.utils.NumberTypeUtils;
import org.esa.beam.util.ImageTransferable;

import com.sun.opengl.util.j2d.TextRenderer;

/**
 * A data viewer displaying the data in a 3D form.
 * 
 * @author Martin Pecka
 */
public class JOGLDataViewer extends JPanel implements GraphicalDataViewer
{

    /** */
    private static final long     serialVersionUID  = -4470588776978542020L;

    /** The data set this viewer displays. */
    private DataSet               dataSet           = null;
    /** The coordinates system used for displaying the data and drawing the grid. */
    private CoordinatesSystem     coordinatesSystem = null;
    /** The OpenGL canvas we draw onto. */
    private final GLCanvas        canvas;
    /** The listener for OpenGL events - handles redrawing and so on. */
    private final GLEventListener glEventListener;
    /** The distance of the near clipping plane (in OpenGL units). */
    private final double          nearClippingPlane = 1;
    /** The distance of the far clipping plane (in OpenGL units). */
    private final double          farClippingPlane  = 10000;
    /** Vertical field of view in degrees. */
    private final double          fovVertical       = 45;

    /** The affine transformation that affects the display of the data set. */
    private double[]              transform         = Matrix.identity();
    /** The maximum length of an axis. */
    private double                maxLength;
    /** The zoom ratio. */
    private double                zoom              = 1d;
    /** The center of the data set (the camera will target it after initialization). */
    private double[]              center            = new double[] { 0, 0, 0 };
    /**
     * The coordinates of the "lowest" point (the point doesn't have to exist, it is just a collection of minimum values
     * in every coordinate).
     */
    private double[]              minPoint          = new double[] { 0, 0, 0 };
    /**
     * The coordinates of the "highest" point (the point doesn't have to exist, it is just a collection of maximum
     * values in every coordinate).
     */
    private double[]              maxPoint          = new double[] { 0, 0, 0 };
    /** The matrix used for setting up camera. */
    private double[]              projectionMatrix  = new double[16];
    /** The task for saving the last rendered image. Executed if it isn't <code>null</code>. */
    private AfterDrawCallback     saveImageTask     = null;

    /**
     * Create a new data viewer using OpenGL for rendering the data set.
     */
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

                maxLength = NumberTypeUtils.max(coordinatesSystem.getAces()[0].getLength(),
                        coordinatesSystem.getAces()[1].getLength(), coordinatesSystem.getAces()[2].getLength())
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
                drawDataset(gl, coordinatesSystem.getColorProvider());
                drawAces(gl, glu, coordinatesSystem);

                // call the image saving task
                processAfterDrawCallbacks(gl);
            }

            /**
             * Draw the given aces onto the canvas.
             * 
             * @param gl The {@link javax.media.opengl.GL} instance to use.
             * @param glu The {@link javax.media.opengl.glu.GLU} instance to use.
             * @param coordinatesSystem The coordinates system containing the aces to draw.
             */
            private void drawAces(GL gl, GLU glu, CoordinatesSystem coordinatesSystem)
            {
                // in order to set different line widths we must call begin/end methods between the width changes
                // gl.glBegin(GL.GL_LINES);
                {
                    Axis<?>[] aces = coordinatesSystem.getAces();
                    TextRenderer textRend = new TextRenderer(Font.decode("Arial-12"), false, true, null, true);
                    TextRenderer boldTextRend = new TextRenderer(Font.decode("Arial-bold-12"), false, true, null, true);
                    final double sqrtZoom = Math.sqrt(Math.min(1, zoom));
                    int[] viewport = new int[4];
                    double[] point = new double[3], axisStart = new double[3], axisEnd = new double[3];
                    double[] vertices = new double[3];
                    Rectangle2D textBounds;
                    gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

                    vertices[0] = aces[0].getMin().doubleValue();
                    vertices[1] = aces[1].getMin().doubleValue();
                    vertices[2] = aces[2].getMin().doubleValue();

                    boldTextRend.beginRendering(getWidth(), getHeight());
                    {
                        gl.glMatrixMode(GL.GL_MODELVIEW);
                        gl.glColor3f(0f, 0f, 0f);
                        glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                viewport, 0, point, 0);
                        String originText = "[" + aces[0].getTickLabels()[0] + "; " + aces[1].getTickLabels()[0] + "; "
                                + aces[2].getTickLabels()[0] + "]";
                        textBounds = boldTextRend.getBounds(originText);
                        gl.glTranslated(point[0] - sqrtZoom * textBounds.getWidth(),
                                point[1] - sqrtZoom * textBounds.getHeight(), 0);
                        gl.glScaled(sqrtZoom, sqrtZoom, sqrtZoom);
                        boldTextRend.draw(originText, 0, 0);
                    }
                    boldTextRend.endRendering();

                    for (int i = 0; i < 3; i++) {
                        vertices[0] = aces[0].getMin().doubleValue();
                        vertices[1] = aces[1].getMin().doubleValue();
                        vertices[2] = aces[2].getMin().doubleValue();

                        glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                viewport, 0, axisStart, 0);

                        gl.glLineWidth(1.5f);

                        gl.glBegin(GL.GL_LINES);
                        {
                            gl.glColor3f(0.2f, 0.2f, 0.2f);
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            vertices[i] = vertices[i] + aces[i].getLength().doubleValue();
                            gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                        }
                        gl.glEnd();

                        glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                viewport, 0, axisEnd, 0);

                        gl.glLineWidth(0.5f);

                        Number[] ticks = aces[i].getTicks();
                        String[] tickLabels = aces[i].getTickLabels();

                        for (int l = 1; l < ticks.length; l++) {
                            vertices[0] = aces[0].getMin().doubleValue();
                            vertices[1] = aces[1].getMin().doubleValue();
                            vertices[2] = aces[2].getMin().doubleValue();
                            vertices[i] = ticks[l].doubleValue();
                            gl.glBegin(GL.GL_LINES);
                            {
                                gl.glColor3f(0.4f, 0.4f, 0.4f);
                                gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                                vertices[(i + 2) % 3] += aces[i].getTickLength().doubleValue();
                                gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                                vertices[(i + 2) % 3] -= aces[i].getTickLength().doubleValue();
                                gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                                vertices[(i + 1) % 3] += aces[i].getTickLength().doubleValue();
                                gl.glVertex3d(vertices[0], vertices[1], vertices[2]);
                            }
                            gl.glEnd();

                            vertices[0] = aces[0].getMin().doubleValue();
                            vertices[1] = aces[1].getMin().doubleValue();
                            vertices[2] = aces[2].getMin().doubleValue();
                            vertices[i] = ticks[l].doubleValue();

                            TextRenderer rend = textRend;
                            if (l == ticks.length - 1)
                                rend = boldTextRend;
                            rend.beginRendering(getWidth(), getHeight());
                            {
                                gl.glMatrixMode(GL.GL_MODELVIEW);
                                gl.glColor3f(0.3f, 0.3f, 0.3f);
                                if (l == ticks.length - 1)
                                    gl.glColor3f(0f, 0f, 0f);
                                glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix,
                                        0, viewport, 0, point, 0);
                                textBounds = rend.getBounds(tickLabels[l]);
                                gl.glTranslated(point[0] - sqrtZoom * textBounds.getWidth(), point[1] - sqrtZoom
                                        * textBounds.getHeight(), 0);
                                gl.glScaled(sqrtZoom, sqrtZoom, sqrtZoom);
                                rend.draw(tickLabels[l], 0, 0);
                            }
                            rend.endRendering();
                        }

                        double labelAngle = 90 - Math.atan2(axisEnd[0] - axisStart[0], axisEnd[1] - axisStart[1])
                                / (2 * Math.PI) * 360;
                        if (labelAngle > 90)
                            labelAngle -= 180;

                        vertices[0] = aces[0].getMin().doubleValue();
                        vertices[1] = aces[1].getMin().doubleValue();
                        vertices[2] = aces[2].getMin().doubleValue();
                        vertices[i] = (aces[i].getMin().doubleValue() + aces[i].getMax().doubleValue()) / 2d;

                        boldTextRend.beginRendering(getWidth(), getHeight());
                        {
                            gl.glMatrixMode(GL.GL_MODELVIEW);
                            gl.glLoadIdentity();
                            gl.glColor3f(0f, 0f, 0f);
                            glu.gluProject(vertices[0], vertices[1], vertices[2], transform, 0, projectionMatrix, 0,
                                    viewport, 0, point, 0);
                            textBounds = boldTextRend.getBounds(aces[i].getLabel());
                            gl.glTranslated(point[0] - 5, point[1] - 5, 0.1);
                            gl.glRotated(labelAngle, 0, 0, 1);
                            gl.glTranslated(-textBounds.getWidth() / 2, -textBounds.getHeight() / 2, 0);
                            boldTextRend.draw(aces[i].getLabel(), 0, 0);
                        }
                        boldTextRend.endRendering();
                    }
                    if (aces.length == 4) {
                        gl.glMatrixMode(GL.GL_PROJECTION);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();
                        gl.glMatrixMode(GL.GL_MODELVIEW);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();

                        {
                            final double xRatioFromPx = 2d / getWidth(), yRatioFromPx = 2d / getHeight();
                            final double textLeft = -0.9;
                            final double labelTop = (getHeight() - 8) * yRatioFromPx - 1, labelMargin = 8 * yRatioFromPx;

                            boldTextRend.beginRendering(getWidth(), getHeight());
                            {
                                gl.glColor3f(0f, 0f, 0f);
                                textBounds = boldTextRend.getBounds(aces[3].getLabel());
                                boldTextRend.draw(aces[3].getLabel(), (int) ((textLeft + 1) / xRatioFromPx),
                                        (int) ((labelTop + 1) / yRatioFromPx - textBounds.getHeight()));
                            }
                            boldTextRend.endRendering();

                            final double width = 100 * xRatioFromPx, height = 15 * yRatioFromPx, top = labelTop
                                    - labelMargin - textBounds.getHeight() * yRatioFromPx;

                            boldTextRend.beginRendering(getWidth(), getHeight());
                            {
                                gl.glColor3f(0f, 0f, 0f);
                                textBounds = boldTextRend.getBounds(aces[3].getMin().toString());
                                boldTextRend.draw(aces[3].getMin().toString(), (int) ((textLeft + 1) / xRatioFromPx),
                                        (int) ((top + 1) / yRatioFromPx - textBounds.getHeight()));
                            }
                            boldTextRend.endRendering();

                            final double left = textLeft + (textBounds.getWidth() + 8) * xRatioFromPx;
                            final int numParts = 20;
                            final ColorProvider colorProvider = coordinatesSystem.getColorProvider();
                            final double colorProviderRange = colorProvider.getMax() - colorProvider.getMin();

                            gl.glBegin(GL.GL_QUAD_STRIP);
                            {
                                double percentDone = 0;
                                for (int i = 0; i <= numParts; i++) {
                                    percentDone = ((double) i) / numParts;
                                    Color color = colorProvider.getColor(colorProvider.getMin() + colorProviderRange
                                            * percentDone);
                                    gl.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                                    gl.glVertex2d(left + width * percentDone, top);
                                    gl.glVertex2d(left + width * percentDone, top - height);
                                }
                            }
                            gl.glEnd();

                            boldTextRend.beginRendering(getWidth(), getHeight());
                            {
                                gl.glColor3f(0f, 0f, 0f);
                                textBounds = boldTextRend.getBounds(aces[3].getMax().toString());
                                boldTextRend.draw(aces[3].getMax().toString(),
                                        (int) ((left + width + 1) / xRatioFromPx + 8),
                                        (int) ((top + 1) / yRatioFromPx - textBounds.getHeight()));
                            }
                            boldTextRend.endRendering();
                        }

                        gl.glMatrixMode(GL.GL_PROJECTION);
                        gl.glPopMatrix();
                        gl.glMatrixMode(GL.GL_MODELVIEW);
                        gl.glPopMatrix();
                    }
                }
                // gl.glEnd();
            }

            /**
             * Draw points from the data set.
             * 
             * @param gl The {@link javax.media.opengl.GL} instance to use.
             * @param colorProvider The color provider for coloring data points.
             */
            private void drawDataset(GL gl, ColorProvider colorProvider)
            {
                gl.glBegin(GL.GL_POINTS);
                if (dataSet instanceof DataSet3D<?, ?, ?>) {
                    @SuppressWarnings("unchecked")
                    final DataSet3D<? extends Number, ? extends Number, ? extends Number> dataSet3D = (DataSet3D<? extends Number, ? extends Number, ? extends Number>) dataSet;

                    float maxDistance = (float) Math.sqrt(Math.pow(dataSet3D.getMaxX().doubleValue()
                            - dataSet3D.getMinX().doubleValue(), 2)
                            + Math.pow(dataSet3D.getMaxY().doubleValue() - dataSet3D.getMinY().doubleValue(), 2)
                            + Math.pow(dataSet3D.getMaxZ().doubleValue() - dataSet3D.getMinZ().doubleValue(), 2));

                    final Iterator<? extends Number> xIt = dataSet3D.xIterator();
                    final Iterator<? extends Number> yIt = dataSet3D.yIterator();
                    final Iterator<? extends Number> zIt = dataSet3D.zIterator();
                    double x, y, z;
                    while (xIt.hasNext() && yIt.hasNext() && zIt.hasNext()) {
                        x = xIt.next().doubleValue();
                        y = yIt.next().doubleValue();
                        z = zIt.next().doubleValue();
                        float colorValue = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))
                                / maxDistance;
                        final Color color = colorProvider.getColor(colorValue);
                        gl.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                        gl.glVertex3d(x, y, z);
                    }
                } else {
                    @SuppressWarnings("unchecked")
                    final DataSet4D<? extends Number, ? extends Number, ? extends Number, ? extends Number> dataSet4D = (DataSet4D<? extends Number, ? extends Number, ? extends Number, ? extends Number>) dataSet;

                    final Iterator<? extends Number> xIt = dataSet4D.xIterator();
                    final Iterator<? extends Number> yIt = dataSet4D.yIterator();
                    final Iterator<? extends Number> zIt = dataSet4D.zIterator();
                    final Iterator<? extends Number> wIt = dataSet4D.wIterator();

                    double x, y, z, w;

                    while (xIt.hasNext() && yIt.hasNext() && zIt.hasNext() && wIt.hasNext()) {
                        x = xIt.next().doubleValue();
                        y = yIt.next().doubleValue();
                        z = zIt.next().doubleValue();
                        w = wIt.next().doubleValue();
                        final Color color = colorProvider.getColor(w);
                        gl.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                        gl.glVertex3d(x, y, z);
                    }
                }
                gl.glEnd();
            }

            /**
             * Draw the grid.
             * 
             * @param gl The {@link javax.media.opengl.GL} instance to use.
             * @param grid The grid to draw.
             */
            private void drawGrid(GL gl, Grid grid)
            {
                gl.glLineWidth(0.5f);
                gl.glBegin(GL.GL_LINES);
                gl.glColor3f(0.8f, 0.8f, 0.8f);

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
                    Matrix.translate(diffX * maxLength / getWidth() * zoom / Math.pow(zoom, 1d / 4), -diffY * maxLength
                            / getHeight() * zoom / Math.pow(zoom, 1d / 4), 0, transform);
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
                final double multiplier = (100 + e.getWheelRotation()
                        * ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == 0 ? 10 : 50)) / 100d;

                if (zoom * multiplier < 0.001)
                    return;

                Matrix.scale(multiplier, multiplier, multiplier, transform);
                zoom *= multiplier;

                update();
            }
        });

        add(canvas, BorderLayout.CENTER);
    }

    // beacause canvas "eats" the mouse events
    @Override
    public synchronized void addMouseListener(MouseListener l)
    {
        canvas.addMouseListener(l);
    }

    @Override
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
    }

    @Override
    public CoordinatesSystem getCoordinatesSystem()
    {
        return coordinatesSystem;
    }

    @Override
    public void setCoordinatesSystem(CoordinatesSystem coordinatesSystem)
    {
        this.coordinatesSystem = coordinatesSystem;
    }

    @Override
    public void saveImage(final File file, final String imageType, final ImageCaptureCallback callback)
    {
        final AfterDrawCallback afterDraw = new AfterDrawCallback() {
            @Override
            public void call(GL gl)
            {
                final BufferedImage img = captureViewer(gl);
                try {
                    ImageIO.write(img, imageType, file);
                } catch (IOException e) {
                    callback.onException(e);
                    return;
                }
                callback.onOk();
            }
        };
        addAfterDrawCallback(afterDraw);
        update();
    }

    @Override
    public void copyImageToClipboard(final ImageCaptureCallback callback)
    {
        final AfterDrawCallback afterDraw = new AfterDrawCallback() {
            @Override
            public void call(GL gl)
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable transferable = new ImageTransferable(captureViewer(gl));
                clipboard.setContents(transferable, null);
                callback.onOk();
            }
        };
        addAfterDrawCallback(afterDraw);
        update();
    }

    /**
     * Add a callback to be called when the redraw of the viewer has finished.
     * 
     * @param callback The callback to call.
     */
    protected synchronized void addAfterDrawCallback(AfterDrawCallback callback)
    {
        if (this.saveImageTask == null)
            this.saveImageTask = callback;
        else {
            AfterDrawCallback call = this.saveImageTask;
            while (call.next != null)
                call = call.next;
            call.next = callback;
        }
    }

    /**
     * Call all the callbacks that should be called after the viewer has been redrawn.
     * 
     * @param gl The GL instance used for drawing.
     */
    protected void processAfterDrawCallbacks(GL gl)
    {
        if (this.saveImageTask != null) {
            // whole method could be synchronized, but to increase performance, we only use synchronization when we have
            // something to call... it is no error to call a callback one redraw later if it has been added just before
            // the entrance to the synchronized section
            synchronized (this) {
                AfterDrawCallback callback = this.saveImageTask;
                while (callback != null) {
                    callback.call(gl);
                    callback = callback.next;
                }
            }
        }
    }

    /**
     * Get the image the viewer displays.
     * <p>
     * Be sure to call this function right after a redraw has been finished.
     * 
     * @param gl The GL instance used to draw the viewer.
     * 
     * @author Felix Gers (http://www.felixgers.de/teaching/jogl/imagingProg.html)
     * 
     * @return The image the viewer displays.
     */
    protected BufferedImage captureViewer(GL gl)
    {
        ByteBuffer pixelsRGB = ByteBuffer.allocateDirect(getWidth() * getHeight() * 3);

        gl.glReadBuffer(GL.GL_BACK);
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
        gl.glReadPixels(0, 0, getWidth(), getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, pixelsRGB);

        int[] pixelInts = new int[getWidth() * getHeight()];

        // Convert RGB bytes to ARGB ints with no transparency.
        // Flip image vertically by reading the
        // rows of pixels in the byte buffer in reverse
        // - (0,0) is at bottom left in OpenGL.

        // Points to first byte (red) in each row.
        int p = getWidth() * getHeight() * 3;
        int q; // Index into ByteBuffer
        int i = 0; // Index into target int[]
        int w3 = getWidth() * 3; // Number of bytes in each row

        for (int row = 0; row < getHeight(); row++) {
            p -= w3;
            q = p;
            for (int col = 0; col < getWidth(); col++) {
                int iR = pixelsRGB.get(q++);
                int iG = pixelsRGB.get(q++);
                int iB = pixelsRGB.get(q++);
                pixelInts[i++] = 0xFF000000 | ((iR & 0x000000FF) << 16) | ((iG & 0x000000FF) << 8) | (iB & 0x000000FF);
            }
        }

        // Create a new BufferedImage from the pixeldata.
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, getWidth(), getHeight(), pixelInts, 0, getWidth());

        return bufferedImage;
    }

    /**
     * A callback called once when the viewer is redrawn.
     * 
     * @author Martin Pecka
     */
    protected abstract static class AfterDrawCallback
    {
        /** The next callback to call. */
        private AfterDrawCallback next = null;

        /**
         * Perform the callback's task.
         * 
         * @param gl The GL instance used for drawing.
         */
        public abstract void call(GL gl);

        /**
         * @return The next.
         */
        public AfterDrawCallback getNext()
        {
            return next;
        }

        /**
         * @param next The next to set.
         */
        public void setNext(AfterDrawCallback next)
        {
            if (next != null)
                this.next = next;
        }
    }
}
