package ui.model.impl;

import ui.model.Sample;

import java.util.HashMap;
import java.util.Map;

public class SimpleSample implements Sample {
    private final Map<String, Double> features;

    public SimpleSample() {
        features = new HashMap<>();
    }

    @Override
    public void addFeature(String name, double value) {
        features.put(name, value);
    }

    @Override
    public double getFeature(String name) {
        return features.get(name);
    }
}
