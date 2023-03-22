package hr.fer.uui.util.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class State implements Comparable<State>
{
    private final String name;
    private double heuristicCost;

    private final List<Edge> neighbours;

    public State(String name, double heuristicCost)
    {
        this.name          = name;
        this.heuristicCost = heuristicCost;
        this.neighbours    = new ArrayList<>();
    }

    /**
     * Default heuristic cost is 0
     * @param name state name
     */
    public State(String name)
    {
        this(name, 0);
    }

    public String getName()
    {
        return name;
    }

    public double getHeuristicCost()
    {
        return heuristicCost;
    }

    public List<Edge> getNeighbours()
    {
        return neighbours;
    }

    public void setHeuristicCost(double heuristicCost)
    {
        this.heuristicCost = heuristicCost;
    }

    public int compareTo(State other)
    {
        return name.compareTo(other.name);
    }

    public static final Comparator<State> BY_HEURISTIC_COST = (s1, s2) -> (int) (s1.heuristicCost - s2.heuristicCost);


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return name.equals(state.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}
