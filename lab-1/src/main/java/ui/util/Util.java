package ui.util;

import ui.algorithms.impl.UCSSearchAlgorithm;
import ui.util.model.Edge;
import ui.util.model.State;
import ui.util.model.StateSpace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util
{
    public static StateSpace getData(Path stateSpacePath, Path heuristicPath)
    {
        StateSpace stateSpace        = new StateSpace();
        State initialState;
        List<State> acceptableStates = new ArrayList<>();
        List<State> states           = new ArrayList<>();

        try
        {
            List<String> stateSpaceLinesWithComments = Files.readAllLines(stateSpacePath);

            List<String> stateSpaceLines = stateSpaceLinesWithComments.stream()
                    .filter(line -> !line.startsWith("#")).toList();

            initialState = new State(stateSpaceLines.get(0));
            states.add(initialState);

            String[] acceptableStatesString = stateSpaceLines.get(1).split(" ");
            for (String acceptableStateString: acceptableStatesString)
            {
                State acceptableState = new State(acceptableStateString);
                acceptableStates.add(acceptableState);
                states.add(acceptableState);
            }

            for (int i = 2; i < stateSpaceLines.size(); i++)
            {
                String line        = stateSpaceLines.get(i);
                String[] lineSplit = line.split(": ");

                if (lineSplit.length == 1) continue;

                String[] edges     = lineSplit[1].split(" ");

                State state;
                int stateIndex = states.indexOf(new State(lineSplit[0]));

                if (stateIndex != -1)
                {
                    state = states.get(stateIndex);
                }
                else
                {
                    state = new State(lineSplit[0]);
                    states.add(state);
                }

                for (String edge: edges)
                {
                    String[] edgeSplit = edge.split(",");

                    State neighbour;
                    double weight      = Double.parseDouble(edgeSplit[1]);
                    int neighbourIndex = states.indexOf(new State(edgeSplit[0]));

                    if (neighbourIndex != -1)
                    {
                        neighbour = states.get(neighbourIndex);
                    }
                    else
                    {
                        neighbour = new State(edgeSplit[0]);
                        states.add(neighbour);
                    }

                    state.getNeighbours().add(new Edge(neighbour, weight));
                }
            }

            if (heuristicPath != null)
            {
                List<String> heuristicLines = Files.readAllLines(heuristicPath);

                heuristicLines.stream()
                        .filter(line -> !line.startsWith("#"))
                        .forEach(line ->
                        {
                            String[] lineSplit = line.split(": ");

                            int stateIndex       = states.indexOf(new State(lineSplit[0]));
                            double heuristicCost = Double.parseDouble(lineSplit[1]);

                            states.get(stateIndex).setHeuristicCost(heuristicCost);
                        });
            }
        } catch (IOException ignore) { initialState = null; }

        stateSpace.setInitialState(initialState);
        stateSpace.getAcceptableStates().addAll(acceptableStates);
        stateSpace.getStates().addAll(states);

        return stateSpace;
    }

    public static void checkIfHeuristicOptimistic(Path stateSpacePath, Path heuristicPath)
    {
        System.out.printf("# HEURISTIC-OPTIMISTIC %s\n", heuristicPath.getFileName());

        StateSpace stateSpace = Util.getData(stateSpacePath, heuristicPath);
        boolean isOptimistic  = true;

        for (State state: stateSpace.getStates())
        {
            stateSpace.setInitialState(state);

            UCSSearchAlgorithm ucs = new UCSSearchAlgorithm(stateSpace);
            ucs.run();

            double hState = state.getHeuristicCost();
            double hReal  = ucs.getTotalCost();

            boolean heuristicOptimistic = hState <= hReal;

            System.out.printf("[CONDITION]: [%s] h(%s) <= h*: %.1f <= %.1f\n",
                    heuristicOptimistic ? "OK" : "ERR",
                    state.getName(),
                    hState,
                    hReal
            );

            stateSpace.reset();
            isOptimistic = isOptimistic && heuristicOptimistic;
        }

        String optimistic    = "Heuristic is optimistic.";
        String notOptimistic = "Heuristic is not optimistic.";

        System.out.printf("[CONCLUSION]: %s", isOptimistic ? optimistic : notOptimistic);
    }

    public static void checkIfHeuristicConsistent(Path stateSpacePath, Path heuristicPath)
    {
        System.out.printf("# HEURISTIC-CONSISTENT %s\n", heuristicPath.getFileName());

        StateSpace stateSpace = Util.getData(stateSpacePath, heuristicPath);
        boolean isConsistent  = true;

        for (State state: stateSpace.getStates())
        {
            for (Edge edge: state.getNeighbours())
            {
                State neighbour   = edge.getNeighbour();

                double hState     = state.getHeuristicCost();
                double hNeighbour = neighbour.getHeuristicCost();
                double cost       = edge.getWeight();

                boolean edgeConsistent = hState <= hNeighbour + cost;

                System.out.printf("[CONDITION]: [%s] h(%s) <= h(%s) + c: %.1f <= %.1f + %.1f\n",
                        edgeConsistent ? "OK" : "ERR",
                        state.getName(),
                        neighbour.getName(),
                        hState,
                        hNeighbour,
                        cost
                        );

                isConsistent = isConsistent && edgeConsistent;
            }
        }

        String consistent    = "Heuristic is consistent.";
        String notConsistent = "Heuristic is not consistent.";

        System.out.printf("[CONCLUSION]: %s", isConsistent ? consistent : notConsistent);
    }
}
