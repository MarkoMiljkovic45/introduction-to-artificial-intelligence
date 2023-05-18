package main.java.ui;

import main.java.ui.model.ID3;
import main.java.ui.model.data.Data;
import main.java.ui.model.data.Sample;
import main.java.ui.model.data.impl.DataSample;
import main.java.ui.model.data.impl.DataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        boolean verbose = true;

        Path trainFile = Path.of("volleyball.csv");
        Path testFile = Path.of("volleyball_test.csv");

        Data trainDataSet = loadData(trainFile, verbose);
        Data testDataSet  = loadData(testFile, verbose);

        ID3 model = new ID3();
        model.fit(trainDataSet);

        System.out.println("[BRANCHES]:");
        model.getBranches().forEach(System.out::println);

        System.out.print("[PREDICTIONS]:");
        List<String> predictions = model.predict(testDataSet);
        predictions.forEach(prediction -> System.out.print(" " + prediction));
    }

    private static Data loadData(Path data, boolean verbose) {
        try {
            List<String> lines = Files.readAllLines(data);

            List<Sample> samples = new ArrayList<>();
            String[] header = lines.get(0).split(",");
            int sampleCount = lines.size();

            for (int i = 1; i < sampleCount; i++) {
                samples.add(parseSample(lines.get(i), header));
            }

            return new DataSet(samples, verbose);
        }
        catch (IOException io) {
            System.out.println("Failed to load data!");
            System.exit(1);
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
