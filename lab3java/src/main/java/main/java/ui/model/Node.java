package main.java.ui.model;

import java.util.Map;

public class Node {
    private final String value;
    private final Map<String, Node> children;

    public Node(String value, Map<String, Node> children) {
        this.value = value;
        this.children = children;
    }

    public Node(String label) {
        this(label, null);
    }

    public String getValue() {
        return value;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children == null;
    }
}
