package ui.algorithms.impl;

import ui.util.model.State;
import ui.util.model.StateSpace;

import java.util.PriorityQueue;

public class UCSSearchAlgorithm extends AbstractSearchAlgorithm
{
    public UCSSearchAlgorithm(StateSpace stateSpace)
    {
        super(new PriorityQueue<>(State.BY_TOTAL_COST.thenComparing(State.BY_NAME)), stateSpace, null);
    }

    @Override
    public String getAlgorithmName()
    {
        return "UCS";
    }
}
