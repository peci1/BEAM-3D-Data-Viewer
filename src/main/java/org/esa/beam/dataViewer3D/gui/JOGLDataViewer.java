/**
 * 
 */
package org.esa.beam.dataViewer3D.gui;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;

import org.esa.beam.dataViewer3D.data.axis.CoordinatesSystem;
import org.esa.beam.dataViewer3D.data.dataset.DataSet;
import org.esa.beam.dataViewer3D.data.dataset.DataSet3D;
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

    public JOGLDataViewer()
    {
        setLayout(new BorderLayout());

        glEventListener = new GLEventListener() {
            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
            {
                GL gl = drawable.getGL();
                GLU glu = new GLU();
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(GL.GL_PROJECTION);
                gl.glLoadIdentity();
                gl.glOrtho(0, 600, 0, 600, -128, 128);
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
            }

            @Override
            public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
            {
                display(drawable);
            }

            @Override
            public void display(GLAutoDrawable drawable)
            {
                GL gl = drawable.getGL();
                gl.glClear(GL.GL_COLOR_BUFFER_BIT);

                gl.glBegin(GL.GL_POINTS);
                if (dataSet instanceof DataSet3D<?, ?, ?>) {
                    DataSet3D<?, ?, ?> dataSet3D = (DataSet3D<?, ?, ?>) dataSet;
                    for (Iterator<? extends DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>>> it = dataSet3D
                            .pointIterator(); it.hasNext();) {
                        DataPoint3D<? extends NumericType<?>, ? extends NumericType<?>, ? extends NumericType<?>> point = it
                                .next();
                        gl.glVertex3d(point.getX().getNumber().doubleValue(), point.getY().getNumber().doubleValue(),
                                point.getZ().getNumber().doubleValue());
                        System.err.println(point);
                    }
                } else {

                }
                gl.glEnd();
            }
        };

        GLCapabilities capabilities = new GLCapabilities();
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(glEventListener);

        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Update the view.
     */
    public void update()
    {
        glEventListener.display(canvas);
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
