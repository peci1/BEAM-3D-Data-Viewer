/**
 * 
 */
package org.esa.beam.dataViewer3D.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.esa.beam.dataViewer3D.data.point.DataPoint3D;
import org.esa.beam.dataViewer3D.data.point.DataPoint4D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint3D;
import org.esa.beam.dataViewer3D.data.point.SimpleDataPoint4D;
import org.esa.beam.dataViewer3D.data.source.DataSource;
import org.esa.beam.dataViewer3D.data.type.ByteType;
import org.esa.beam.dataViewer3D.data.type.DoubleType;
import org.esa.beam.dataViewer3D.data.type.FloatType;
import org.esa.beam.dataViewer3D.data.type.IntType;
import org.esa.beam.dataViewer3D.data.type.NumericType;

/**
 * Common functions for test classes.
 * 
 * @author Martin Pecka
 */
public class Common
{

    /**
     * Return some test 3D data points. Every 12 points should contain two pairs of equal points.
     * 
     * @param size The number of generated points.
     * @return The test sample.
     */
    public static DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[] getTestData3D(int size)
    {
        @SuppressWarnings("unchecked")
        DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[] data = (DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>[]) new DataPoint3D<?, ?, ?>[size];
        for (int i = 0; i < size; i++) {
            switch ((i + 1) % 12) {
                case 0:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null));
                    break;

                case 1:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null));
                    break;

                case 2:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null));
                    break;
                case 3:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MIN_VALUE + i / 12),
                            new DoubleType(Double.MIN_VALUE + i / 12, null));
                    break;
                case 4:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MAX_VALUE - i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.MAX_VALUE - i / 12, null));
                    break;
                case 5:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.NaN, null));
                    break;
                case 6:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (-5 + i / 12)), new IntType(-6 + i / 12), new DoubleType(-6.5 + i / 12,
                                    null));
                    break;
                case 7:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(10000000 + i / 12), new DoubleType(
                                    Double.MIN_NORMAL + i / 12, null));
                    break;
                case 8:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 20));
                    break;
                case 9:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 1));
                    break;
                case 10:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(4 + i / 12), new DoubleType(-10d + i / 12,
                                    null));
                    break;
                case 11:
                    data[i] = new SimpleDataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null));
                    break;

                default:
                    break;
            }
        }
        return data;
    }

    /**
     * Return some test 4D data points. Every 12 points should contain two pairs of equal points.
     * 
     * @param size The number of generated points.
     * @return The test sample.
     */
    public static DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] getTestData4D(
            int size)
    {
        @SuppressWarnings("unchecked")
        DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[] data = (DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>[]) new DataPoint4D<?, ?, ?, ?>[size];
        for (int i = 0; i < size; i++) {
            switch ((i + 1) % 12) {
                case 0:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null), new FloatType(0f + i / 12, null));
                    break;
                case 1:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null), new FloatType(6.6f + i / 12, null));
                    break;
                case 2:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (0 + i / 12)), new IntType(0 + i / 12), new DoubleType(0d + i / 12,
                                    null), new FloatType(0f + i / 12, null));
                    break;
                case 3:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MIN_VALUE + i / 12),
                            new DoubleType(Double.MIN_VALUE + i / 12, null), new FloatType(Float.MIN_VALUE + i / 12,
                                    null));
                    break;
                case 4:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MAX_VALUE - i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.MAX_VALUE - i / 12, null), new FloatType(Float.MAX_VALUE - i / 12,
                                    null));
                    break;
                case 5:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (Byte.MIN_VALUE + i / 12)), new IntType(Integer.MAX_VALUE - i / 12),
                            new DoubleType(Double.NaN, null), new FloatType(Float.POSITIVE_INFINITY, null));
                    break;
                case 6:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (-5 + i / 12)), new IntType(-6 + i / 12), new DoubleType(-6.5 + i / 12,
                                    null), new FloatType(-6.6f + i / 12, null));
                    break;
                case 7:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(10000000 + i / 12), new DoubleType(
                                    Double.MIN_NORMAL + i / 12, null), new FloatType(Float.MIN_NORMAL + i / 12, null));
                    break;
                case 8:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 20), new FloatType(6.4999999f + i / 12, 20));
                    break;
                case 9:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (2 + i / 12)), new IntType(3 + i / 12), new DoubleType(
                                    3.99999999999 + i / 12, 1), new FloatType(6.499999999f + i / 12, 1));
                    break;
                case 10:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (1 + i / 12)), new IntType(4 + i / 12), new DoubleType(-10d + i / 12,
                                    null), new FloatType(-11f + i / 12, null));
                    break;
                case 11:

                    data[i] = new SimpleDataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>(
                            new ByteType((byte) (5 + i / 12)), new IntType(6 + i / 12), new DoubleType(6.5 + i / 12,
                                    null), new FloatType(6.6f + i / 12, null));
                    break;
            }
        }
        return data;
    }

    public static DataSource<Byte> getTestDataSourceX(final int size)
    {
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(Common.getTestData3D(size));
        return new DataSource<Byte>() {

            @Override
            public Iterator<Byte> iterator()
            {
                return new Iterator<Byte>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Byte next()
                    {
                        return it.next().getX().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Byte>> numericTypeIterator()
            {
                return new Iterator<NumericType<Byte>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Byte> next()
                    {
                        return it.next().getX();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static DataSource<Integer> getTestDataSourceY(final int size)
    {
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(Common.getTestData3D(size));
        return new DataSource<Integer>() {

            @Override
            public Iterator<Integer> iterator()
            {
                return new Iterator<Integer>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Integer next()
                    {
                        return it.next().getY().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Integer>> numericTypeIterator()
            {
                return new Iterator<NumericType<Integer>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Integer> next()
                    {
                        return it.next().getY();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static DataSource<Double> getTestDataSourceZ(final int size)
    {
        final List<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> data = Arrays
                .asList(Common.getTestData3D(size));
        return new DataSource<Double>() {

            @Override
            public Iterator<Double> iterator()
            {
                return new Iterator<Double>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Double next()
                    {
                        return it.next().getZ().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Double>> numericTypeIterator()
            {
                return new Iterator<NumericType<Double>>() {
                    Iterator<DataPoint3D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Double> next()
                    {
                        return it.next().getZ();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static DataSource<Float> getTestDataSourceW(final int size)
    {
        final List<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> data = Arrays
                .asList(Common.getTestData4D(size));
        return new DataSource<Float>() {

            @Override
            public Iterator<Float> iterator()
            {
                return new Iterator<Float>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Float next()
                    {
                        return it.next().getW().getNumber();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            public Iterator<NumericType<Float>> numericTypeIterator()
            {
                return new Iterator<NumericType<Float>>() {
                    Iterator<DataPoint4D<NumericType<Byte>, NumericType<Integer>, NumericType<Double>, NumericType<Float>>> it = data.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public NumericType<Float> next()
                    {
                        return it.next().getW();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}