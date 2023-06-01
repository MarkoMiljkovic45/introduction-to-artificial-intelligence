package ui.model;

import ui.math.RealVector;

import java.util.List;

public interface DataSet {
    void setHeader(List<String> header);
    void addInput(RealVector input);
    void addOutput(double input);
    int getInputLayerSize();
    int getOutputLayerSize();
    List<RealVector> getInputs();
    RealVector getOutputs();
}
