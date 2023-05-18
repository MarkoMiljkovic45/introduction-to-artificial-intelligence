package main.java.ui.model.data;

import java.util.List;
import java.util.Set;

public interface Data {
    boolean isEmpty();
    long size();
    double getDataSetEntropy();
    String mostFrequentLabel();
    Data partitionByLabel(String label);
    Data partitionByFeatureValue(String feature, String value);

    /**
     * Uses IG(D,x) to find the most discriminative feature x in Data d
     * @return most discriminative feature x
     */
    String mostDiscriminativeFeature();
    /**
     * @param feature of the data set
     * @return set of all possible values for a given feature
     */
    Set<String> getFeatureValueSet(String feature);
    Set<String> getFeatureSet();
    Set<String> getLabelSet();
    List<Sample> getData();
    boolean equals(Object o);
}
