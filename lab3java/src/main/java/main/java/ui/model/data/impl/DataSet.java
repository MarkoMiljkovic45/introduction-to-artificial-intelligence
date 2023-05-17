package main.java.ui.model.data.impl;

import main.java.ui.model.data.Data;
import main.java.ui.model.data.Sample;

import java.util.Collection;
import java.util.Set;

public class DataSet implements Data { //TODO
    private final Collection<Sample> data; //TODO Decide on a data structure

    public DataSet(Collection<Sample> data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public String mostFrequentLabel() {
        return null;
    }

    @Override
    public Data partitionByLabel(String label) {
        return null;
    }

    @Override
    public String mostDiscriminativeFeature() {
        return null;
    }

    @Override
    public Set<String> getFeatureValueSet(String feature) {
        return null;
    }

    @Override
    public Data partitionByFeatureValue(String feature, String value) {
        return null;
    }

    @Override
    public Set<String> getFeatureSet() {
        return null;
    }
}
