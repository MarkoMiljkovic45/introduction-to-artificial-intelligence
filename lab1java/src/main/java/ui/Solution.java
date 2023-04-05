package ui;

import ui.algorithms.SearchAlgorithm;
import ui.algorithms.impl.AbstractSearchAlgorithm;
import ui.util.Util;
import ui.util.model.StateSpace;

import java.nio.file.Path;
import java.util.Map;

public class Solution
{
    public static void main(String ... args)
    {
        Map<String, String> argMap = Util.parseProgramArguments(args);

        Path stateSpacePath = Path.of(argMap.get("ss"));
        Path heuricsticPath;

        if (argMap.get("h") != null)  heuricsticPath = Path.of(argMap.get("h"));
        else heuricsticPath = null;

        StateSpace stateSpace = Util.getData(stateSpacePath, heuricsticPath);

        try
        {
            if (argMap.get("alg") != null)
            {
                SearchAlgorithm alg = AbstractSearchAlgorithm.getInstance(argMap.get("alg"));
                alg.setStateSpace(stateSpace);
                alg.setHeuristicPath(heuricsticPath);
                alg.run();
                alg.printResults();
            }
            else if (argMap.get("check-optimistic") != null)
            {
                if (heuricsticPath != null)
                    Util.checkIfHeuristicOptimistic(stateSpace, heuricsticPath);
            }
            else if (argMap.get("check-consistent") != null)
            {
                if (heuricsticPath != null)
                    Util.checkIfHeuristicConsistent(stateSpace, heuricsticPath);
            }
        }
        catch (Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
