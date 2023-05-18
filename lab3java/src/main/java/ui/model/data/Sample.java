package ui.model.data;

import java.util.Map;

public interface Sample {
    String getLabel();
    Map<String, String> getFeatureMap();
    void setLabel(String label);
    void addFeature(String feature, String value);
}
