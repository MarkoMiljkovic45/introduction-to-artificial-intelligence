package ui.algorithms.impl;

import ui.algorithms.SearchAlgorithm;
import ui.util.model.Edge;
import ui.util.model.State;
import ui.util.model.StateSpace;

import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public abstract class AbstractSearchAlgorithm implements SearchAlgorithm
{
    private final Queue<State> open;
    private final Set<State> visited;
    private StateSpace stateSpace;
    private Path heuristicPath;
    private boolean solutionFound;
    private int statesVisited;
    private int pathLength;
    private State finalState;
    private String path;

    public AbstractSearchAlgorithm(Queue<State> open, StateSpace stateSpace, Path heuristicPath)
    {
        this.open = open;
        this.visited = new HashSet<>();
        this.stateSpace = stateSpace;
        this.heuristicPath = heuristicPath;
        this.solutionFound = false;
        this.statesVisited = 0;
        this.pathLength = 0;
        this.finalState = null;
        this.path = "";
    }

    @Override
    public void setStateSpace(StateSpace stateSpace)
    {
        this.stateSpace = stateSpace;
    }

    @Override
    public void setHeuristicPath(Path heuristicPath)
    {
        this.heuristicPath = heuristicPath;
    }

    public abstract String getAlgorithmName();

    @Override
    public void run()
    {
        solutionFound = false;
        open.add(stateSpace.getInitialState());

        while (!open.isEmpty())
        {
            State n = open.poll();
            statesVisited++;
            if (stateSpace.getAcceptableStates().contains(n))
            {
                finalState = n;
                solutionFound = true;
                shortestPathReconstruction();
                return;
            }
            visited.add(n);
            for (Edge edge: n.getNeighbours())
            {
                State neighbour = edge.getNeighbour();
                if (!visited.contains(neighbour))
                {
                    resolveParent(n, edge);

                    if (!open.contains(neighbour))
                        open.add(neighbour);
                }
            }
        }
    }

    private void resolveParent(State parent, Edge edge)
    {
        State  child        = edge.getNeighbour();
        double newTotalCost = parent.getTotalCost() + edge.getWeight();

        if (child.getParent() == null || child.getTotalCost() > newTotalCost)
        {
            child.setParent(parent);
            child.setTotalCost(newTotalCost);

            if (open.contains(child))
            {
                open.remove(child);
                open.add(child);
            }
        }
    }

    private void shortestPathReconstruction()
    {
        Stack<State> stack = new Stack<>();

        stack.push(finalState);
        State p = finalState.getParent();

        while (p != null)
        {
            stack.push(p);
            p = p.getParent();
        }

        pathLength = stack.size();

        StringBuilder sb = new StringBuilder();

        while (!stack.empty())
        {
            sb.append(stack.pop().getName());
            if (!stack.empty()) sb.append(" => ");
        }

        path = sb.toString();
    }

    public double getTotalCost()
    {
        if (finalState == null) return -1;
        return finalState.getTotalCost();
    }

    @Override
    public void printResults()
    {
        System.out.printf("# %s", getAlgorithmName());

        if (heuristicPath != null) System.out.printf(" %s\n", heuristicPath.getFileName());
        else System.out.print("\n");

        System.out.printf("[FOUND_SOLUTION]: %s\n", solutionFound ? "yes" : "no");
        if (solutionFound)
        {
            System.out.printf("[STATES_VISITED]: %d\n", statesVisited);
            System.out.printf("[PATH_LENGTH]: %d\n", pathLength);
            System.out.printf("[TOTAL_COST]: %.1f\n", finalState.getTotalCost());
            System.out.printf("[PATH]: %s", path);
        }
    }

    public static SearchAlgorithm getInstance(String algorithmName) throws NoSuchAlgorithmException
    {
        switch (algorithmName)
        {
            case "bfs"   -> { return new BFSSearchAlgorithm(); }
            case "ucs"   -> { return new UCSSearchAlgorithm(); }
            case "astar" -> { return new AStarSearchAlgorithm(); }
            default -> throw new NoSuchAlgorithmException("No such algorithm: " + algorithmName);
        }
    }


}
