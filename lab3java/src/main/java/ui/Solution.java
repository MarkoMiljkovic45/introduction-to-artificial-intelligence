package ui;

import ui.model.ID3;
import ui.model.data.Data;
import ui.model.data.Sample;
import ui.model.data.impl.DataSample;
import ui.model.data.impl.DataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        boolean verbose = true;

        Path trainFile = Path.of(args[0]);
        Path testFile = Path.of(args[1]);

        Data trainDataSet = loadData(trainFile, verbose);
        Data testDataSet  = loadData(testFile, verbose);

        ID3 model;

        if (args.length == 3) {
            int depthLimit = Integer.parseInt(args[2]);
            model = new ID3(depthLimit);
        } else {
            model = new ID3();
        }

        model.fit(trainDataSet);

        System.out.println("[BRANCHES]:");
        model.getBranches().forEach(System.out::println);

        System.out.print("[PREDICTIONS]:");
        List<String> predictions = model.predict(testDataSet);
        predictions.forEach(prediction -> System.out.print(" " + prediction));

        System.out.printf("\n[ACCURACY]: %.5f\n", getModelAccuracy(testDataSet, predictions));

        System.out.println("[CONFUSION_MATRIX]:");
        getConfusionMatrixRows(testDataSet, predictions).forEach(System.out::println);
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

    private static double getModelAccuracy(Data testSet, List<String> predictions) {
        List<Sample> samples = testSet.getData();

        int correctPredictionCount = 0;
        int totalSampleCount = samples.size();

        for (int i = 0; i < totalSampleCount; i++) {
            if (samples.get(i).getLabel().equals(predictions.get(i))) {
                correctPredictionCount++;
            }
        }

        return 1.0 * correctPredictionCount / totalSampleCount;
    }

    private static List<String> getConfusionMatrixRows(Data testData, List<String> predictions) {
        List<String> rows = new ArrayList<>();

        List<String> labels = testData.getLabelSet().stream().sorted().toList();
        int labelsSize = labels.size();

        long[][] matrix = new long[labelsSize][labelsSize];

        List<Sample> samples = testData.getData();
        int sampleSize = samples.size();
        for (int i = 0; i < sampleSize; i++) {
            String correctLabel = samples.get(i).getLabel();
            String predictedLabel = predictions.get(i);

            if (predictedLabel.equals(correctLabel)) {
                int idx = labels.indexOf(correctLabel);
                matrix[idx][idx]++;
            } else {
                int row = labels.indexOf(correctLabel);
                int col = labels.indexOf(predictedLabel);
                matrix[row][col]++;
            }
        }

        for (int row = 0; row < labelsSize; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < labelsSize; col++) {
                sb.append(matrix[row][col]);

                if (col != labelsSize - 1) {
                    sb.append(" ");
                }
            }
            rows.add(sb.toString());
        }

        return rows;
    }
}
