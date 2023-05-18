package main.java.ui.model;

import main.java.ui.model.data.Data;

import java.util.Map;

public class Node {
    private final String value;
    private final Map<String, Node> children;
    private final Data dataPartition;

    public Node(String value, Map<String, Node> children, Data dataPartition) {
        this.value = value;
        this.children = children;
        this.dataPartition = dataPartition;
    }

    public Node(String label) {
        this(label, null, null);
    }

    public String getValue() {
        return value;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public Data getDataPartition() {
        return dataPartition;
    }

    public boolean isLeaf() {
        return children == null;
    }
}
