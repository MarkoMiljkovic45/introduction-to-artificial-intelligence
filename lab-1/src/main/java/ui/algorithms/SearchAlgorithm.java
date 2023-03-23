package ui.algorithms;

import ui.util.model.StateSpace;

import java.nio.file.Path;

public interface SearchAlgorithm
{
    void run();
    void printResults();

    void setStateSpace(StateSpace stateSpace);

    void setHeuristicPath(Path heuristicPath);

    double getTotalCost();
}
