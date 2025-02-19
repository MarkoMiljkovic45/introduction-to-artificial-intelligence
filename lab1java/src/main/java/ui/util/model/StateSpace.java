package ui.util.model;

import java.util.*;

public class StateSpace
{
    private State initialState;
    private final List<State> acceptableStates;
    private final List<State> states;

    public StateSpace()
    {
        acceptableStates = new ArrayList<>();
        states = new ArrayList<>();
    }

    public State getInitialState()
    {
        return initialState;
    }

    public List<State> getAcceptableStates()
    {
        return acceptableStates;
    }

    public List<State> getStates()
    {
        return states;
    }

    public void setInitialState(State initialState)
    {
        this.initialState = initialState;
    }

    public void reset()
    {
        for (State state: states)
        {
            state.setParent(null);
            state.setTotalCost(0);
            state.setDepth(0);
        }
    }
}
