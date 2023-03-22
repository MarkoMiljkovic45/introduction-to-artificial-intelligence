package hr.fer.uui.util.model;

import java.util.Objects;

public class Edge implements Comparable<Edge>
{
    private final State neighbour;
    private final double weight;

    public Edge(State neighbour, double weight)
    {
        this.neighbour = neighbour;
        this.weight = weight;
    }

    /**
     * Default neighbour weight is 1
     */
    public Edge(State neighbour)
    {
        this(neighbour, 1);
    }

    public State getNeighbour()
    {
        return neighbour;
    }

    public double getWeight()
    {
        return weight;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 && Objects.equals(neighbour, edge.neighbour);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(neighbour, weight);
    }

    @Override
    public int compareTo(Edge other) {
        return (int) (weight - other.weight);
    }
}
