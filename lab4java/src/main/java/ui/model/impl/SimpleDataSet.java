package ui.model.impl;

import ui.math.MatrixUtils;
import ui.math.RealVector;
import ui.model.DataSet;

import java.util.ArrayList;
import java.util.List;

public class SimpleDataSet implements DataSet {
    private List<String> header;
    private final List<RealVector> inputs = new ArrayList<>();
    private final List<Double> outputs = new ArrayList<>();

    @Override
    public void setHeader(List<String> header) {
        this.header = header;
    }

    @Override
    public void addInput(RealVector input) {
        inputs.add(input);
    }

    @Override
    public void addOutput(double output) {
        outputs.add(output);
    }

    @Override
    public int getInputLayerSize() {
        return header.size() - 1;
    }

    @Override
    public int getOutputLayerSize() {
        return 1;
    }

    @Override
    public List<RealVector> getInputs() {
        return inputs;
    }

    @Override
    public RealVector getOutputs() {
        int outputsSize = outputs.size();
        double[] data = new double[outputsSize];

        for (int i = 0; i < outputsSize; i++) {
            data[i] = outputs.get(i);
        }

        return MatrixUtils.createRealVector(data);
    }
}
