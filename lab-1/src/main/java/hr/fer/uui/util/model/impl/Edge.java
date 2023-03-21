package hr.fer.uui.util.model.impl;

public class Edge implements Comparable<Edge>
{
    private State neighbour;
    private double weight;

    @Override
    public int compareTo(Edge other) {
        return (int) (weight - other.weight);
    }
}
