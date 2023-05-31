package ui.nn;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import ui.model.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetInstance {
    private final int inputLayerSize;
    private final int outputLayerSize;
    private final String architecture;
    private final List<RealMatrix> hiddenLayers = new ArrayList<>();
    private double latestEvaluation;
    private final static double GAUSS_STDDEV_FOR_INITIAL_WEIGHTS = 0.01;

    public NeuralNetInstance(int inputLayerSize, int outputLayerSize, String architecture) {
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.architecture = architecture;

        initHiddenLayers();
    }

    public double getLatestEvaluation() {
        return latestEvaluation;
    }

    private void initHiddenLayers() {
        String[] layers = architecture.split("s");
        int numOfLayers = layers.length - 1;

        int previousLayerSize = inputLayerSize;

        for (int i = 0; i < numOfLayers; i++) {
            int layerSize = Integer.parseInt(layers[i]);

            double[][] randomWeights = getRandomWeightsMatrix(previousLayerSize, layerSize);
            RealMatrix weightMatrix = MatrixUtils.createRealMatrix(randomWeights);
            hiddenLayers.add(weightMatrix);

            previousLayerSize = layerSize;
        }

        double[][] randomWeights = getRandomWeightsMatrix(previousLayerSize, outputLayerSize);
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(randomWeights);
        hiddenLayers.add(weightMatrix);
    }

    private static double[][] getRandomWeightsMatrix(int rows, int cols) {
        Random random = new Random();
        double[][] weightMatrix = new double[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                weightMatrix[row][col] = random.nextGaussian() * GAUSS_STDDEV_FOR_INITIAL_WEIGHTS;
            }
        }

        return weightMatrix;
    }

    /**
     * Pushes the input data through the Neural Network
     *
     * @param input data
     * @return output from this neural network
     */
    public RealVector getOutput(RealVector input) {
        if (input.getDimension() != inputLayerSize) {
            throw new IllegalArgumentException("Unexpected input size.");
        }

        //TODO
        return null;
    }

    /**
     * Evaluates the neural network based on the given data set
     * and sets the latest evaluation for this instance
     * <p>
     * The evaluation is based on the mean squared error
     *
     * @param dataSet used for evaluation
     */
    public void evaluate(DataSet dataSet) {
        //TODO Sets latestEvaluation
    }

    /**
     * Combines two neural network instances to create a child by averaging their weights
     *
     * @param first parent instance
     * @param second parent instance
     * @param pMutation probability of a mutation occurring at an index
     * @param gaussStdDev the deviation of the normal distribution used to cause a mutation
     * @return child instance
     */
    public static NeuralNetInstance pair(NeuralNetInstance first, NeuralNetInstance second, double pMutation, double gaussStdDev) {
        //TODO
        return null;
    }
}
