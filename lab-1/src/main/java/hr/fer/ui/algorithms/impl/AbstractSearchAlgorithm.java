package hr.fer.ui.algorithms.impl;

import hr.fer.ui.algorithms.SearchAlgorithm;
import hr.fer.ui.util.Util;
import hr.fer.ui.util.model.Edge;
import hr.fer.ui.util.model.State;
import hr.fer.ui.util.model.StateSpace;

import java.nio.file.Path;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public abstract class AbstractSearchAlgorithm implements SearchAlgorithm
{
    private final Queue<State> open;
    private final Set<State> visited;
    private final StateSpace stateSpace;
    private final Path heuristicPath;
    private boolean solutionFound;
    private int statesVisited;
    private int pathLength;
    private State finalState;
    private String path;

    public AbstractSearchAlgorithm(Queue<State> open, Set<State> visited, Path stateSpacePath, Path heuristicPath)
    {
        this.open = open;
        this.visited = visited;
        this.stateSpace = Util.getData(stateSpacePath, heuristicPath);
        this.heuristicPath = heuristicPath;
        this.solutionFound = false;
        this.statesVisited = 0;
        this.pathLength = 0;
        this.totalCost = 0;
        this.finalState = null;
        this.path = "";
    }

    public abstract String getAlgorithmName();

    @Override
    public void run()
    {
        open.add(stateSpace.getInitialState());

        while (!open.isEmpty())
        {
            State n = open.poll();
            statesVisited++;
            if (stateSpace.getAcceptableStates().contains(n))
            {
                finalState = n;
                solutionFound = true;
                return;
            }
            visited.add(n);
            for (Edge edge: n.getNeighbours())
            {
                State neighbour = edge.getNeighbour();
                if (!visited.contains(neighbour))
                {
                    resolveParent(n, edge);
                    open.add(neighbour);
                }
            }
        }

        if (solutionFound) shortestPathReconstruction();
    }

    private void resolveParent(State parent, Edge edge)
    {
        State  child        = edge.getNeighbour();
        double newTotalCost = parent.getTotalCost() + edge.getWeight();

        if (child.getParent() == null || child.getTotalCost() > newTotalCost)
        {
            child.setParent(parent);
            child.setTotalCost(newTotalCost);
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

    @Override
    public void printResults()
    {
        System.out.printf("# %s %s\n", getAlgorithmName(), heuristicPath);
        System.out.printf("[FOUND_SOLUTION]: %s\n", solutionFound ? "yes" : "no");
        if (solutionFound)
        {
            System.out.printf("[STATES_VISITED]: %d\n", statesVisited);
            System.out.printf("[PATH_LENGTH]: %d\n", pathLength);
            System.out.printf("[TOTAL_COST]: %.1f\n", finalState.getTotalCost());
            System.out.printf("[PATH]: %s", path);
        }
    }


}
