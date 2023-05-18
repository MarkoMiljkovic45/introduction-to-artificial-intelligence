package ui.model.data.impl.impl;

import ui.model.data.Data;
import ui.model.data.Sample;
import org.junit.jupiter.api.Test;
import ui.model.data.impl.DataSample;
import ui.model.data.impl.DataSet;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataSetTest {

    public static DataSet emptyDataSet = new DataSet(Collections.emptyList());
    public static DataSet dataSet = new DataSet(loadSamples());

    @Test
    public void isEmptyTrueTest() {
        assertTrue(emptyDataSet.isEmpty());
    }

    @Test
    public void isEmptyFalseTest() {
        assertFalse(dataSet.isEmpty());
    }

    @Test
    public void sizeTest() {
        assertEquals(14, dataSet.size());
    }

    @Test
    public void sizeZeroTest() {
        assertEquals(0, emptyDataSet.size());
    }

    @Test
    public void emptyDataSetFindMostFrequentLabelTest() {
        String mfl = emptyDataSet.mostFrequentLabel();
        assertNull(mfl);
    }

    @Test
    public void notEmptyDataSetFindMostFrequentLabelTest() {
        String mfl = dataSet.mostFrequentLabel();
        assertEquals("yes", mfl);
    }

    @Test
    public void partitionDataSetByPresentLabelTest() {
        Data partitionedDataSet = dataSet.partitionByLabel("no");
        Data expectedDataSet = new DataSet(Objects.requireNonNull(loadSamples()).stream()
                .filter(sample -> sample.getLabel().equals("no"))
                .toList());

        assertEquals(expectedDataSet, partitionedDataSet);
    }

    @Test
    public void partitionDataSetByNotPresentLabelTest() {
        Data partitionedDataSet = dataSet.partitionByLabel("maybe");
        assertTrue(partitionedDataSet.isEmpty());
    }

    @Test
    public void getFeatureValueSetTest() {
        Set<String> featureValueSet = dataSet.getFeatureValueSet("weather");
        Set<String> expectedSet = Set.of("sunny", "cloudy", "rainy");

        assertEquals(expectedSet, featureValueSet);
    }

    @Test
    public void getFeatureSetTest() {
        Set<String> featureSet = dataSet.getFeatureSet();
        Set<String> expectedSet = Set.of("weather", "temperature", "humidity" ,"wind");

        assertEquals(expectedSet, featureSet);
    }

    @Test
    public void getLabelSetTest() {
        Set<String> labelSet = dataSet.getLabelSet();
        Set<String> expectedSet = Set.of("yes", "no");

        assertEquals(expectedSet, labelSet);
    }

    @Test
    public void getDataSetEntropyTest() {
        double expectedEntropy = 0.94;
        assertEquals(expectedEntropy, dataSet.getDataSetEntropy(), 0.01);
    }

    @Test
    public void partitionFeatureValueTest() {
        List<Sample> data = loadSamples();

        List<Sample> partitionedData = new ArrayList<>();
        assert data != null;
        partitionedData.add(data.get(1));
        partitionedData.add(data.get(5));
        partitionedData.add(data.get(6));
        partitionedData.add(data.get(10));
        partitionedData.add(data.get(11));
        partitionedData.add(data.get(13));

        Data expectedPartition = new DataSet(partitionedData);
        assertEquals(expectedPartition, dataSet.partitionByFeatureValue("wind", "strong"));
    }

    @Test
    public void mostDiscriminativeFeatureTest() {
        assertEquals("weather", dataSet.mostDiscriminativeFeature());
    }

    @Test
    public void igTest() {
        double expectedIG = 0.1518;
        assertEquals(expectedIG, dataSet.IG("humidity"), 0.0001);

    }

    private static List<Sample> loadSamples() {
        try (InputStream is = DataSetTest.class.getResourceAsStream("/volleyball.csv")) {
            assert is != null;
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            String[] lines = text.strip().split("\\n");

            List<Sample> samples = new ArrayList<>();
            String[] header = lines[0].split(",");
            int sampleCount = lines.length;

            for (int i = 1; i < sampleCount; i++) {
                samples.add(parseSample(lines[i], header));
            }

            return samples;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static Sample parseSample(String line, String[] header) {
        Sample sample = new DataSample();


        int featureCount = header.length - 1;
        String[] featureValues = line.split(",");

        sample.setLabel(featureValues[featureCount]);

        for (int i = 0; i < featureCount; i++) {
            sample.addFeature(header[i], featureValues[i]);
        }

        return sample;
    }
}
