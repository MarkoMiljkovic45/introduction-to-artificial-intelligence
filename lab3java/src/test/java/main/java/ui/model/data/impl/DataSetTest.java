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
}
