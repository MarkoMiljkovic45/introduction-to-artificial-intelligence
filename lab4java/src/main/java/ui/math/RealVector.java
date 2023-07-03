package ui.math;

import java.util.function.Function;

public class RealVector {
    private final double[] data;

    public RealVector(double[] data) {
        this.data = data;
    }

    public int getDimension() {
        return data.length;
    }

    public double getEntry(int index) {
        return data[index];
    }

    public void mapToSelf(Function<Double, Double> function) {
        int dataLen = data.length;

        for (int i = 0; i < dataLen; i++) {
            data[i] = function.apply(data[i]);
        }
    }

    public RealVector append(RealVector other) {
        int thisLen = getDimension();
        int otherLen = other.getDimension();

        int newLen = thisLen + otherLen;
        double[] newData = new double[newLen];

        System.arraycopy(data, 0, newData, 0, thisLen);
        System.arraycopy(other.data, 0, newData, thisLen, otherLen);

        return new RealVector(newData);
    }

    public RealVector append(double value) {
        int len = getDimension();

        int newLen = len + 1;
        double[] newData = new double[newLen];

        System.arraycopy(data, 0, newData, 0, len);
        newData[len] = value;

        return new RealVector(newData);
    }

    public RealVector subtract(RealVector other) {
        int len = getDimension();

        if (len != other.getDimension()) {
            throw new IllegalArgumentException("Vector dimension mismatch");
        }

        double[] newData = new double[len];

        for (int i = 0; i < len; i++) {
            newData[i] = data[i] - other.data[i];
        }

        return new RealVector(newData);
    }

    /**
     * Element-by-element multiply
     *
     * @param other vector
     * @return new vector where v[i] = this[i] * other[i]
     */
    public RealVector ebeMultiply(RealVector other) {
        int len = getDimension();

        if (len != other.getDimension()) {
            throw new IllegalArgumentException("Vector dimension mismatch");
        }

        double[] newData = new double[len];

        for (int i = 0; i < len; i++) {
            newData[i] = data[i] * other.data[i];
        }

        return new RealVector(newData);
    }

    public double getSum() {
        double sum = 0;

        for (double value: data) {
            sum += value;
        }

        return sum;
    }
}
