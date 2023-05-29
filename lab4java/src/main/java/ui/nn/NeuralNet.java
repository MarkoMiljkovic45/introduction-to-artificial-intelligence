package ui.nn;

import ui.model.DataSet;

public class NeuralNet {
    private static final int INFO_ON_ITER = 2000;

    private DataSet trainSet;
    private DataSet testSet;
    private NeuralNetArchitecture architecture;
    private int populationSize;
    private int elitism;
    private double pMutation;
    private double gaussStdDev;
    private long iter;

    public NeuralNet(DataSet trainSet, DataSet testSet, NeuralNetArchitecture architecture, int populationSize, int elitism, double pMutation, double gaussStdDev, long iter) {
        this.trainSet = trainSet;
        this.testSet = testSet;
        this.architecture = architecture;
        this.populationSize = populationSize;
        this.elitism = elitism;
        this.pMutation = pMutation;
        this.gaussStdDev = gaussStdDev;
        this.iter = iter;
    }

    public void run() {
        trainModel();
        testModel();
    }

    private void trainModel() {
        //TODO
    }

    private void testModel() {
        //TODO
    }
}
