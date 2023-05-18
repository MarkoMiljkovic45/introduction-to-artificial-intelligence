package ui.model.data.impl;

import ui.model.data.Sample;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataSample implements Sample {
    private String label;
    private final Map<String, String> featureMap;

    public DataSample(String label, Map<String, String> featureMap) {
        this.label = label;
        this.featureMap = featureMap;
    }

    public DataSample() {
        this(null, new HashMap<>());
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Map<String, String> getFeatureMap() {
        return featureMap;
    }

    @Override
    public void addFeature(String feature, String value) {
        featureMap.put(feature, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSample that = (DataSample) o;
        return Objects.equals(label, that.label) && Objects.equals(featureMap, that.featureMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, featureMap);
    }
}
