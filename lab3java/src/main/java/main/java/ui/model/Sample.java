package main.java.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Sample {
    private final List<String> values;

    public Sample() {
        values = new ArrayList<>();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public String getLabel() {
        if (values.size() > 0)
            return values.get(values.size() - 1);
        else
            return null;
    }

    public List<String> getData() {
        if (values.size() > 0) {
            return values.subList(0, values.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
