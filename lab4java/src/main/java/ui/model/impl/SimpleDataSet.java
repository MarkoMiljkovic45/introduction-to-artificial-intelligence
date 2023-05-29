package ui.model.impl;

import ui.model.DataSet;
import ui.model.Sample;

import java.util.ArrayList;
import java.util.List;

public class SimpleDataSet implements DataSet {
    private List<String> header;
    private final List<Sample> samples;

    public SimpleDataSet() {
        samples = new ArrayList<>();
    }

    @Override
    public void setHeader(List<String> header) {
        this.header = header;
    }

    @Override
    public void addSample(Sample sample) {
        samples.add(sample);
    }

    public List<Sample> getSamples() {
        return samples;
    }

    @Override
    public int getFeatureCount() {
        return header.size() - 1;
    }
}
