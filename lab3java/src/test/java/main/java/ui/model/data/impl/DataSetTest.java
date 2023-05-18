package main.java.ui.model.data.impl;

import java.util.*;

import main.java.ui.model.data.Data;
import main.java.ui.model.data.Sample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataSetTest {

    public static DataSet emptyDataSet = new DataSet(Collections.emptyList());
    public static List<Sample> samples = List.of(/*TODO Add samples*/);
    public static DataSet dataSet = new DataSet(samples);

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
        assertEquals(5 /*TODO Add size*/, dataSet.size());
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
        assertEquals(""/*TODO Set mfl*/, mfl);
    }

    @Test
    public void partitionDataSetByPresentLabelTest() {
        Data partitionedDataSet = dataSet.partitionByLabel(""/*TODO Add label*/);
        Data expectedDataSet = new DataSet(List.of(/*TODO Add expected samples*/));
        assertEquals(expectedDataSet, partitionedDataSet);

    }

    @Test
    public void partitionDataSetByNotPresentLabelTest() {
        Data partitionedDataSet = dataSet.partitionByLabel(""/*TODO Add label*/);
        assertTrue(partitionedDataSet.isEmpty());
    }

    @Test
    public void getFeatureValueSetTest() {
        Set<String> featureValueSet = dataSet.getFeatureValueSet(""/*TODO Add feature*/);
        Set<String> expectedSet = Set.of(/*TODO Add expected feature values*/);

        assertEquals(expectedSet, featureValueSet);
    }

    @Test
    public void getFeatureSetTest() {
        Set<String> featureSet = dataSet.getFeatureSet(/*TODO Add feature*/);
        Set<String> expectedSet = Set.of(/*TODO Add expected features*/);

        assertEquals(expectedSet, featureSet);
    }

    @Test
    public void getLabelSetTest() {
        Set<String> labelSet = dataSet.getLabelSet(/*TODO Add feature*/);
        Set<String> expectedSet = Set.of(/*TODO Add expected labels*/);

        assertEquals(expectedSet, labelSet);
    }

    @Test
    public void getDataSetEntropyTest() {
        double expectedEntropy = 0.0; //TODO Calc actual entropy
        assertEquals(expectedEntropy, dataSet.getDataSetEntropy());
    }

    @Test
    public void partitionFeatureValueTest() {
        Data expectedPartition = new DataSet(List.of(/*TODO Expected samples*/));
        assertEquals(expectedPartition, dataSet.partitionByFeatureValue("" /*TODO Feature*/, "" /*TODO Value*/));
    }

    @Test
    public void mostDiscriminativeFeatureTest() {
        assertEquals("" /*TODO Most desc feature*/, dataSet.mostDiscriminativeFeature());
    }

    @Test
    public void igTest() {
        double expectedIG = 0; //TODO Calc ig
        assertEquals(expectedIG, dataSet.IG("" /*TODO Feature*/));
    }
}
