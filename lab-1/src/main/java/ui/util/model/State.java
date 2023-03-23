package ui.util.model;

import java.util.*;

public class State
{
    private final String name;
    private double heuristicCost;
    private double totalCost;
    private State parent;

    private final Set<Edge> neighbours;

    public State(String name, double heuristicCost)
    {
        this.name          = name;
        this.heuristicCost = heuristicCost;
        this.neighbours    = new TreeSet<>(Comparator.comparing(e -> e.getNeighbour().getName()));
        this.parent        = null;
        this.totalCost     = 0;
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

    public Set<Edge> getNeighbours()
    {
        return neighbours;
    }

    public void setHeuristicCost(double heuristicCost)
    {
        this.heuristicCost = heuristicCost;
    }

    public double getTotalCost()
    {
        return totalCost;
    }

    public State getParent()
    {
        return parent;
    }

    public void setTotalCost(double totalCost)
    {
        this.totalCost = totalCost;
    }

    public void setParent(State parent)
    {
        this.parent = parent;
    }

    public static final Comparator<State> BY_NAME = Comparator.comparing(State::getName);
    public static final Comparator<State> BY_TOTAL_COST = (s1, s2) -> (int) (s1.getTotalCost() - s2.getTotalCost());
    public static final Comparator<State> A_STAR = (s1, s2) -> (int) (s1.getTotalCost() + s1.getHeuristicCost() - s2.getTotalCost() - s2.getHeuristicCost());


    @Override
    public String toString()
    {
        return getName();
    }

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
