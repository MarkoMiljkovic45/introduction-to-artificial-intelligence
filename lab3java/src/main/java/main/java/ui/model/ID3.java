package main.java.ui.model;

import main.java.ui.model.data.Data;

import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private Node decisionTreeRoot;

    /**
     * Trains the model on labeled data
     * @param data labeled data
     */
    public void fit(Data data) {
        Set<String> features = data.getFeatureSet();
        decisionTreeRoot = id3(data, data, features);
    }

    /**
     * Uses the model to predict the label for samples in the test set
     * @param testSet samples to be labeled
     * @return Labeled data
     */
    public List<String> predict(Data testSet) {
        //TODO

        //1. Print BRANCHES to all leafs, Nodes sorted by depth
        //
        return null;
    }

    private Node id3(Data data, Data parentData, Set<String> features) {
        if (data.isEmpty()) {
            String label = parentData.mostFrequentLabel();
            return new Node(label);
        }

        String label = data.mostFrequentLabel();
        if (features.isEmpty() || data.equals(data.partitionByLabel(label))) {
            return new Node(label);
        }

        String feature = data.mostDiscriminativeFeature();
        Map<String, Node> subtrees = new HashMap<>();

        for(String value: data.getFeatureValueSet(feature)) {
            Node subtree = id3(data.partitionByFeatureValue(feature, value), data, subset(features, feature));
            subtrees.put(value, subtree);
        }

        return new Node(feature, subtrees);
    }

    private static Set<String> subset(Set<String> set, String exclude) {
        return set.stream().filter(e -> !e.equals(exclude)).collect(Collectors.toSet());
    }
}
