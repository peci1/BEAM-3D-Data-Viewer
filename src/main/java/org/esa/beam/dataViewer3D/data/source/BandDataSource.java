/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.util.Iterator;

/**
 * A data source taking the input data from a band.
 * 
 * @author Martin Pecka
 * @param <N> Type of the band values.
 * 
 *            TODO write the whole class
 */
public abstract class BandDataSource<N extends Number> implements DataSource<N>
{

    @Override
    public Iterator<N> iterator()
    {
        return null;
        // TODO Auto-generated method stub
    }

    @Override
    public long size()
    {
        return 0;
        // TODO Auto-generated method stub
    }

}
