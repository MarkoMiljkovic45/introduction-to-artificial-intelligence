package main.java.ui;

import main.java.ui.model.ID3;
import main.java.ui.model.data.Data;

import java.nio.file.Path;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        Path trainFile = Path.of("volleyball.csv");
        Path testFile = Path.of("volleyball_text.csv");

        Data trainDataSet = loadData(trainFile);
        Data testDataSet  = loadData(testFile);

        ID3 model = new ID3();
        model.fit(trainDataSet);
        List<String> predictions = model.predict(testDataSet);

        System.out.println(predictions);
    }

    private static Data loadData(Path trainFile) {
        //TODO
        return null;
    }

}
