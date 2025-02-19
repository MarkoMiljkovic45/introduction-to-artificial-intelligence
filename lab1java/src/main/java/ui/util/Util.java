package ui.util;

import ui.algorithms.SearchAlgorithm;
import ui.algorithms.impl.AbstractSearchAlgorithm;
import ui.util.model.Edge;
import ui.util.model.State;
import ui.util.model.StateSpace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Util
{
    //TODO speed up getData
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

    public static void checkIfHeuristicOptimistic(StateSpace stateSpace, Path heuristicPath)
    {
        System.out.printf("# HEURISTIC-OPTIMISTIC %s\n", heuristicPath.getFileName());

        try
        {
            SearchAlgorithm searchAlgorithm = AbstractSearchAlgorithm.getInstance("ucs");
            searchAlgorithm.setStateSpace(stateSpace);
            searchAlgorithm.setHeuristicPath(heuristicPath);

            boolean isOptimistic  = true;

            List<State> states = stateSpace.getStates();
            states.sort(State.BY_NAME);

            for (State state: states)
            {
                stateSpace.setInitialState(state);

                searchAlgorithm.run();

                double hState = state.getHeuristicCost();
                double hReal  = searchAlgorithm.getTotalCost();

                boolean heuristicOptimistic = hState <= hReal;

                System.out.printf("[CONDITION]: [%s] h(%s) <= h*: %.1f <= %.1f\n",
                        heuristicOptimistic ? "OK" : "ERR",
                        state.getName(),
                        hState,
                        hReal
                );

                isOptimistic = isOptimistic && heuristicOptimistic;
            }

            String optimistic    = "Heuristic is optimistic.";
            String notOptimistic = "Heuristic is not optimistic.";

            System.out.printf("[CONCLUSION]: %s", isOptimistic ? optimistic : notOptimistic);
        } catch (NoSuchAlgorithmException e)
        {
            System.out.println("Error loading search algorithm.");
        }
    }

    public static void checkIfHeuristicConsistent(StateSpace stateSpace, Path heuristicPath)
    {
        System.out.printf("# HEURISTIC-CONSISTENT %s\n", heuristicPath.getFileName());

        boolean isConsistent  = true;

        List<State> states = stateSpace.getStates();
        states.sort(State.BY_NAME);

        for (State state: states)
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

    public static Map<String, String> parseProgramArguments(List<String> args)
    {
        Map<String, String> map = new HashMap<>();

        for (String arg : args)
        {
            if (arg.startsWith("--"))
            {
                if (args.indexOf(arg) + 1 != args.size() && !args.get(args.indexOf(arg) + 1).startsWith("--"))
                {
                    map.put(arg.replace("--", ""), args.get(args.indexOf(arg) + 1));
                }
                else
                {
                    map.put(arg.replace("--", ""), "FLAG_UP");
                }
            }
        }

        return map;
    }

    public static Map<String, String> parseProgramArguments(String[] args)
    {
        return parseProgramArguments(List.of(args));
    }
}
