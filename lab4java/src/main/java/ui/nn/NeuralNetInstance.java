package ui.nn;

import ui.math.MatrixUtils;
import ui.math.RealMatrix;
import ui.math.RealVector;
import ui.math.Sigmoid;
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
    private final Sigmoid transferFunction = new Sigmoid();
    private final static double GAUSS_STDDEV_FOR_INITIAL_WEIGHTS = 0.01;

    public NeuralNetInstance(int inputLayerSize, int outputLayerSize, String architecture) {
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.architecture = architecture;
    }

    public double getLatestEvaluation() {
        return latestEvaluation;
    }

    public void initHiddenLayers() {
        String[] layers = architecture.split("s");
        int previousLayerSize = inputLayerSize + 1; // +1 for bias input

        for (String layer : layers) {
            int layerSize = Integer.parseInt(layer);

            double[][] randomWeights = getRandomWeightsMatrix(previousLayerSize, layerSize);
            RealMatrix weightMatrix = MatrixUtils.createRealMatrix(randomWeights);
            hiddenLayers.add(weightMatrix);

            previousLayerSize = layerSize + 1; // +1 for bias input
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

        input = input.append(1);                    //Add bias

        RealVector output = input;
        int numOfLayers = hiddenLayers.size();

        for (int i = 0; i < numOfLayers; i++) {
            RealMatrix hiddenLayer = hiddenLayers.get(i);
            output = hiddenLayer.preMultiply(output);

            if (i + 1 < numOfLayers) {
                output.mapToSelf(transferFunction);
                output = output.append(1);              //Add bias
            }
        }

        return output;
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
        List<RealVector> inputs = dataSet.getInputs();
        RealVector expectedOutputs = dataSet.getOutputs();
        RealVector actualOutputs = MatrixUtils.createRealVector(new double[0]);

        for (RealVector input: inputs) {
            actualOutputs = actualOutputs.append(getOutput(input));
        }

        RealVector error = expectedOutputs.subtract(actualOutputs);
        RealVector squaredError = error.ebeMultiply(error);

        int errorCount = error.getDimension();
        double totalSquaredError = squaredError.getSum();

        latestEvaluation = totalSquaredError / errorCount;
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
        NeuralNetInstance child = new NeuralNetInstance(first.inputLayerSize, first.outputLayerSize, first.architecture);

        int hiddenLayerCount = first.hiddenLayers.size();

        for (int i = 0; i < hiddenLayerCount; i++) {
            RealMatrix firstLayer = first.hiddenLayers.get(i);
            RealMatrix secondLayer = second.hiddenLayers.get(i);

            RealMatrix layerAverage = firstLayer.add(secondLayer).scalarMultiply(0.5);

            int rows = layerAverage.getRows();
            int cols = layerAverage.getCols();
            RealMatrix mutationMatrix = getMutationLayer(rows, cols, pMutation, gaussStdDev);
            RealMatrix mutatedLayer = layerAverage.add(mutationMatrix);

            child.hiddenLayers.add(mutatedLayer);
        }

        return child;
    }

    private static RealMatrix getMutationLayer(int rows, int cols, double pMutation, double gaussStdDev) {
        Random random = new Random();
        double[][] data = new double[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (random.nextDouble() < pMutation) {
                    data[row][col] = random.nextGaussian() * gaussStdDev;
                } else {
                    data[row][col] = 0;
                }
            }
        }

        return MatrixUtils.createRealMatrix(data);
    }
}
