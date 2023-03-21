package hr.fer.uui.util.model.impl;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public class State implements Comparable<State>
{
    private String name;
    private double heuristicCost;
    private Set<Edge> neighbours;

    public int compareTo(State other) {
        return name.compareTo(other.name);
    }

    public static final Comparator<State> BY_HEURISTIC_COST = (s1, s2) -> (int) (s1.heuristicCost - s2.heuristicCost);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return name.equals(state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
