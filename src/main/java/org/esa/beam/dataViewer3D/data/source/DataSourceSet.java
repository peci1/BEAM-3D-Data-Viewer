/**
 * 
 */
package org.esa.beam.dataViewer3D.data.source;

/**
 * A set of data sources all of the same type.
 * 
 * @author Martin Pecka
 */
public interface DataSourceSet
{
    /**
     * Get the data source with the given index (index must be 0 to the number of dimensions).
     * 
     * @param index Index of the data source.
     * @return The data source.
     */
    DataSource<?> getDataSource(int index);
}
