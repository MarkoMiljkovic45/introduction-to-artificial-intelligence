package main.java.ui;

import main.java.ui.model.Sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        Path testFile = Path.of("volleyball.csv");

        List<Sample> data = loadData(testFile);
    }

    private static List<Sample> loadData(Path filePath) {
        List<Sample> data = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(filePath);
            lines.forEach(line -> data.add(parseLine(line)));
        }
        catch (IOException io) {
            System.out.println("Failed to load training data!");
            System.exit(1);
        }

        return data;
    }

    private static Sample parseLine(String line) {
        Sample sample = new Sample();
        for (String attr: line.split(",")) {
            sample.addValue(attr);
        }
        return sample;
    }
}
