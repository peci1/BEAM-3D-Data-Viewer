/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

import java.util.Iterator;

import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * A set of 3 data sources.
 * 
 * @author Martin Pecka
 * @param <X> The type of x coordinate.
 * @param <Y> The type of y coordinate.
 * @param <Z> The type of z coordinate.
 */
public interface DataSourceSet3D<X extends Number, Y extends Number, Z extends Number> extends DataSourceSet
{
    /**
     * @return The data source for x coordinate.
     */
    DataSource<X> getXSource();

    /**
     * @return The data source for y coordinate.
     */
    DataSource<Y> getYSource();

    /**
     * @return The data source for z coordinate.
     */
    DataSource<Z> getZSource();

    /**
     * Return the iterator that iterates through the data points and returns them as specific 3D points.
     * 
     * @return The iterator that iterates through the data points and returns them as specific 3D points.
     */
    Iterator<DataPoint3D<NumericType<X>, NumericType<Y>, NumericType<Z>>> pointIterator();

    /**
     * Iterator over x values.
     * 
     * @return The iterator.
     */
    Iterator<X> xIterator();

    /**
     * Iterator over y values.
     * 
     * @return The iterator.
     */
    Iterator<Y> yIterator();

    /**
     * Iterator over z values.
     * 
     * @return The iterator.
     */
    Iterator<Z> zIterator();
}
