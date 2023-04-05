package ui.algorithms.impl;

import ui.util.model.State;
import ui.util.model.StateSpace;

import java.nio.file.Path;
import java.util.PriorityQueue;

public class AStarSearchAlgorithm extends AbstractSearchAlgorithm
{
    public AStarSearchAlgorithm(StateSpace stateSpace, Path heuristicPath)
    {
        super(new PriorityQueue<>(State.A_STAR.thenComparing(State.BY_NAME)),
                stateSpace,
                heuristicPath
        );
    }

    public AStarSearchAlgorithm()
    {
        this(null, null);
    }

    @Override
    public String getAlgorithmName()
    {
        return "A-STAR";
    }
}
