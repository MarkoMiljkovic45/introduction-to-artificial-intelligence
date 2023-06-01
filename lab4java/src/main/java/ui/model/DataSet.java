package ui.model;

import org.apache.commons.math3.linear.RealVector;

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
