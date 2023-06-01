package ui.math;

public class MatrixUtils {

    public static RealVector createRealVector(double[] data) {
        return new RealVector(data);
    }

    public static RealMatrix createRealMatrix(double[][] data) {
        return new RealMatrix(data);
    }
}
