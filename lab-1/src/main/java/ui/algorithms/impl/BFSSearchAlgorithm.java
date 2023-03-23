package ui.algorithms.impl;

import ui.util.model.StateSpace;

import java.util.LinkedList;

public class BFSSearchAlgorithm extends AbstractSearchAlgorithm
{

    public BFSSearchAlgorithm(StateSpace stateSpace)
    {
        super(new LinkedList<>(), stateSpace, null);
    }

    public String getAlgorithmName()
    {
        return "BFS";
    }
}
