package main.java.ui.model.data.impl;

import main.java.ui.model.data.Data;
import main.java.ui.model.data.Sample;

import java.util.*;
import java.util.stream.Collectors;

public class DataSet implements Data {
    private final List<Sample> data;
    private Set<String> featureSet;
    private Set<String> featureValueSet;
    private Set<String> labelSet;
    private final boolean verbose;

    public DataSet(List<Sample> data, boolean verbose) {
        this.data = Objects.requireNonNullElse(data, Collections.emptyList());
        this.verbose = verbose;
    }

    public DataSet(List<Sample> data) {
        this(data, false);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public long size() {
        return data.size();
    }

    @Override
    public String mostFrequentLabel() {
        Map<String, Long> counts = data.stream()
                .map(Sample::getLabel)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        Map.Entry<String, Long> mfl = counts.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue)).orElse(null);

        if (mfl != null) {
            return mfl.getKey();
        } else {
            return null;
        }
    }

    @Override
    public Data partitionByLabel(String label) {
        return new DataSet(data.stream()
                .filter(sample -> sample.getLabel().equals(label))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Finds the feature with the highest information gained IG(D, x)
     * @return most discriminative feature by information gained
     */
    @Override
    public String mostDiscriminativeFeature() {
        String mdf = null;
        double maxIG = 0;

        for (String feature: getFeatureSet()) {
            double featureIG = IG(feature);

            if (maxIG < featureIG) {
                mdf = feature;
                maxIG = featureIG;
            }
        }

        if (verbose) {
            System.out.print("\n");
        }

        return mdf;
    }

    /**
     * Calculates information gained for a specific feature
     * @param feature a data set feature
     * @return information gained given feature
     */
    public double IG(String feature) {
        double ig = getDataSetEntropy();

        for (String featureValue: getFeatureValueSet(feature)) {
            Data featureValuePartition = partitionByFeatureValue(feature, featureValue);
            double weight = 1.0 * featureValuePartition.size() / size();
            ig = ig - weight * featureValuePartition.getDataSetEntropy();
        }

        if (verbose) {
            System.out.print("IG(" + feature + ")=" + ig);
        }

        return ig;
    }

    /**
     * Calculates the information entropy E(D) for this data set
     * @return data set entropy
     */
    @Override
    public double getDataSetEntropy() {
        double entropy = 0;

        for (String label: getLabelSet()) {
            long labelCount = data.stream().filter(sample -> sample.getLabel().equals(label)).count();
            double probability = 1.0 * labelCount / size();

            if (probability != 0) {
                entropy = entropy - (probability * Math.log(probability) / Math.log(2));
            }
        }

        return entropy;
    }

    @Override
    public Set<String> getLabelSet() {
        if (labelSet == null) {
            labelSet = data.stream()
                    .map(Sample::getLabel)
                    .collect(Collectors.toSet());
        }

        return labelSet;
    }

    @Override
    public Set<String> getFeatureValueSet(String feature) {
        if (featureValueSet == null) {
            featureValueSet = data.stream()
                    .map(sample -> sample.getFeatureMap().get(feature))
                    .collect(Collectors.toSet());
        }

        return featureValueSet;
    }

    @Override
    public Set<String> getFeatureSet() {
        if (featureSet == null) {
            featureSet = data.stream()
                    .map(sample -> sample.getFeatureMap().keySet())
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        return featureSet;
    }

    @Override
    public Data partitionByFeatureValue(String feature, String value) {
        return new DataSet(data.stream()
                .filter(sample -> Objects.equals(sample.getFeatureMap().get(feature), value))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet dataSet = (DataSet) o;
        return Objects.equals(data, dataSet.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
