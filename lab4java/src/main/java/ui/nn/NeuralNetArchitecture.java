package ui.nn;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public class NeuralNetArchitecture {
    private final static double GAUSS_STDDEV_FOR_INITIAL_WEIGHTS = 0.01;

    private int inputLayerSize;
    private int outputLayerSize;
    private List<RealMatrix> hiddenLayers;


    public static NeuralNetArchitecture getInstance(int inputLayerSize, String archetype, int outputLayerSize) {
        //TODO
        return new NeuralNetArchitecture();
    }
}
