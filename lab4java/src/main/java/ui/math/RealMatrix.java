package ui.math;

public class RealMatrix {
    private final double[][] data;

    public RealMatrix(double[][] data) {
        this.data = data;
    }

    public int getRows() {
        return data.length;
    }

    public int getCols() {
        if (getRows() == 0)
            return 0;
        else
            return data[0].length;
    }

    /**
     * Multiplies the row vector with this matrix
     *
     * @param vector row vector
     * @return new row vector = vector * this
     */
    public RealVector preMultiply(RealVector vector) {
        int rows = getRows();
        int cols = getCols();
        int vectorLen = vector.getDimension();

        if (rows != vectorLen) {
            throw new IllegalArgumentException("Matrix and vector shape mismatch");
        }

        double[] result = new double[cols];

        for (int col = 0; col < cols; col++) {
            double value = 0;

            for (int row = 0; row < rows; row++) {
                value += data[row][col] * vector.getEntry(row);
            }

            result[col] = value;
        }

        return new RealVector(result);
    }

    public RealMatrix add(RealMatrix other) {
        int rows = getRows();
        int cols = getCols();

        int otherRows = other.getRows();
        int otherCols = other.getCols();

        if (rows != otherRows || cols != otherCols) {
            throw new IllegalArgumentException("Matrix shape mismatch");
        }

        double[][] newData = new double[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                newData[row][col] = data[row][col] + other.data[row][col];
            }
        }

        return new RealMatrix(newData);
    }

    public RealMatrix scalarMultiply(double scalar) {
        int rows = getRows();
        int cols = getCols();

        double[][] newData = new double[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                newData[row][col] = data[row][col] * scalar;
            }
        }

        return new RealMatrix(newData);
    }
}
