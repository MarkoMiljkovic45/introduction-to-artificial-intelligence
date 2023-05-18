package ui.model;

import ui.model.data.Data;
import ui.model.data.Sample;

import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private Node decisionTreeRoot;
    private final int depthLimit;

    public ID3(int depthLimit) {
        this.depthLimit = depthLimit;
    }

    public ID3() {
        this(-1);
    }

    /**
     * Trains the model on labeled data
     * @param data labeled data
     */
    public void fit(Data data) {
        Set<String> features = data.getFeatureSet();
        decisionTreeRoot = id3(data, data, features, 0, depthLimit);
    }

    public List<String> getBranches() {
        List<String> branches = new ArrayList<>();
        recursiveBranchReconstruction(decisionTreeRoot, branches, "", 1);
        return branches;
    }

    private void recursiveBranchReconstruction(Node node, List<String> branches, String branch, int depth) {
        if (node.isLeaf()) {
            branches.add(branch + node.getValue());
            return;
        }

        Map<String, Node> children = node.getChildren();

        for (String transition: children.keySet()) {
            recursiveBranchReconstruction(
                    children.get(transition),
                    branches,
                    branch + depth + ":" + node.getValue() + "=" + transition + " ",
                    depth + 1
            );
        }
    }

    /**
     * Uses the model to predict the label for samples in the test set
     * @param testSet samples to be labeled
     * @return Labeled data
     */
    public List<String> predict(Data testSet) {
        List<String> predictions = new ArrayList<>();

        for (Sample sample: testSet.getData()) {
            Map<String, String> featureMap = sample.getFeatureMap();

            Node node = decisionTreeRoot;
            while(!node.isLeaf()) {
                String featureValue = featureMap.get(node.getValue());
                Node nextNode = node.getChildren().get(featureValue);

                if (nextNode == null) {
                    break;
                } else {
                    node = nextNode;
                }
            }

            if (node.isLeaf()) {
                predictions.add(node.getValue());
            } else {
                predictions.add(node.getDataPartition().mostFrequentLabel());
            }
        }

        return predictions;
    }

    private Node id3(Data data, Data parentData, Set<String> features, int depth, int depthLimit) {
        if (data.isEmpty()) {
            String label = parentData.mostFrequentLabel();
            return new Node(label);
        }

        if (depth == depthLimit) {
            return new Node(data.mostFrequentLabel());
        }

        String label = data.mostFrequentLabel();
        if (features.isEmpty() || data.equals(data.partitionByLabel(label))) {
            return new Node(label);
        }

        String feature = data.mostDiscriminativeFeature();
        Map<String, Node> subtrees = new HashMap<>();

        for(String value: data.getFeatureValueSet(feature)) {
            Node subtree = id3(data.partitionByFeatureValue(feature, value), data, subset(features, feature), depth + 1, depthLimit);
            subtrees.put(value, subtree);
        }

        return new Node(feature, subtrees, data);
    }

    private static Set<String> subset(Set<String> set, String exclude) {
        return set.stream().filter(e -> !e.equals(exclude)).collect(Collectors.toSet());
    }
}
