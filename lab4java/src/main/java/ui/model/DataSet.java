package ui.model;

import java.util.List;

public interface DataSet {
    void setHeader(List<String> header);
    void addSample(Sample sample);
    int getFeatureCount();
    List<Sample> getSamples();
}
