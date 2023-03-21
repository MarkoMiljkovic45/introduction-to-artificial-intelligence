package hr.fer.uui.util.model.impl;

import hr.fer.uui.util.model.Data;

import java.util.Set;

public class StateSpace implements Data
{
    private State initialState;
    private Set<State> acceptableStates;
    private Set<State> states;
}
