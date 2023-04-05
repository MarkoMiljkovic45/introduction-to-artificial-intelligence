package ui.algorithms.impl;

import ui.util.model.State;
import ui.util.model.StateSpace;

import java.util.PriorityQueue;

public class BFSSearchAlgorithm extends AbstractSearchAlgorithm
{

    public BFSSearchAlgorithm(StateSpace stateSpace)
    {
        super(new PriorityQueue<>(State.BY_DEPTH.thenComparing(State.BY_NAME)), stateSpace, null);
    }

    public BFSSearchAlgorithm()
    {
        this(null);
    }

    public String getAlgorithmName()
    {
        return "BFS";
    }
}
