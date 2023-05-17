package main.java.ui.model.data;

import java.util.Set;

public interface Data {
    boolean isEmpty();
    String mostFrequentLabel();
    Data partitionByLabel(String label);
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
    Data partitionByFeatureValue(String feature, String value);
    Set<String> getFeatureSet();
}
