/**
 * 
 */
package org.esa.beam.dataViewer3D.data.point;

import static org.junit.Assert.fail;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author Martin Pecka
 */
public abstract class SimpleDataPointTestCommon
{

    protected void testHashCodeHelper(DataPoint[] points)
    {
        int hash;
        LinkedHashMap<Integer, List<DataPoint>> sameHashes = new LinkedHashMap<Integer, List<DataPoint>>();

        for (DataPoint point : points) {
            hash = point.hashCode();
            if (sameHashes.get(hash) == null) {
                sameHashes.put(hash, new LinkedList<DataPoint>());
            }
            boolean contains = false;
            for (DataPoint p : sameHashes.get(hash)) {
                if (p.equals(point)) {
                    contains = true;
                    break;
                }
            }
            if (!contains)
                sameHashes.get(hash).add(point);
        }

        int i = 0;
        for (Entry<Integer, List<DataPoint>> e : sameHashes.entrySet()) {
            if (e.getValue().size() > 1) {
                i += e.getValue().size();
            }
        }

        if ((double) i / points.length > 0.05)
            fail("More than 5% of different items with colliding hash codes: " + (double) i / points.length * 100
                    + "%!");
    }

}
