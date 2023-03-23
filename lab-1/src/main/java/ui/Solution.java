package ui;

import ui.algorithms.SearchAlgorithm;
import ui.algorithms.impl.AStarSearchAlgorithm;
import ui.algorithms.impl.BFSSearchAlgorithm;
import ui.algorithms.impl.UCSSearchAlgorithm;
import ui.util.Util;
import ui.util.model.StateSpace;

import java.nio.file.Path;

public class Solution
{
    public static void main(String ... args) {
        //TODO Accept args

        Path stateSpacePath = Path.of("lab-1/ai.txt");
        Path heuristicPath  = Path.of("lab-1/ai_pass.txt");

        StateSpace stateSpace = Util.getData(stateSpacePath, null);

        SearchAlgorithm alg = new UCSSearchAlgorithm(stateSpace);
        alg.run();
        alg.printResults();
    }
}
