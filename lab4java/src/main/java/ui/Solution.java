package ui;

import ui.math.MatrixUtils;
import ui.model.DataSet;
import ui.model.impl.SimpleDataSet;
import ui.nn.NeuralNet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public static void main(String[] args) {
        try {
            Map<String, String> argMap = parseArgs(args);

            DataSet trainSet = loadDataSet(Path.of(argMap.get("--train")));
            DataSet testSet = loadDataSet(Path.of(argMap.get("--test")));

            NeuralNet nn = new NeuralNet(
                    argMap.get("--nn"),
                    Integer.parseInt(argMap.get("--popsize")),
                    Integer.parseInt(argMap.get("--elitism")),
                    Double.parseDouble(argMap.get("--p")),
                    Double.parseDouble(argMap.get("--K")),
                    Long.parseLong(argMap.get("--iter"))
            );

            nn.fit(trainSet);
            nn.test(testSet);
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        int argLen = args.length;
        int i = 0;

        while (i < argLen) {
            String arg = args[i++];
            String value = args[i++];

            if (!arg.startsWith("--")) {
                throw new IllegalArgumentException("Unexpected program argument: " + arg);
            }

            argMap.put(arg, value);
        }

        return argMap;
    }

    private static DataSet loadDataSet(Path dataSetPath) throws IOException {
        DataSet dataSet = new SimpleDataSet();

        List<String> lines = Files.readAllLines(dataSetPath);

        List<String> header = List.of(lines.get(0).split(","));
        dataSet.setHeader(header);

        int linesSize = lines.size();
        int headerSize = header.size();
        int inputSize = headerSize - 1;

        for (int i = 1; i < linesSize; i++) {
            String[] sampleData = lines.get(i).split(",");
            double[] data = new double[inputSize];

            for (int j = 0; j < inputSize; j++) {
                data[j] = Double.parseDouble(sampleData[j]);
            }

            dataSet.addInput(MatrixUtils.createRealVector(data));
            dataSet.addOutput(Double.parseDouble(sampleData[inputSize]));
        }

        return dataSet;
    }
}
