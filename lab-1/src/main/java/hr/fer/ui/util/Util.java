package hr.fer.ui.util;

import hr.fer.ui.util.model.Edge;
import hr.fer.ui.util.model.State;
import hr.fer.ui.util.model.StateSpace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util
{
    public static StateSpace getData(Path stateSpacePath, Path heuristicPath) throws IOException
    {
        StateSpace stateSpace        = new StateSpace();
        State initialState;
        List<State> acceptableStates = new ArrayList<>();
        List<State> states           = new ArrayList<>();

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
            String[] edges     = lineSplit[1].split(" ");

            State state;
            int stateIndex = states.indexOf(new State(lineSplit[0]));

            if (stateIndex != -1)
                state = states.get(stateIndex);
            else
                state = new State(lineSplit[0]);

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

        try
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
        } catch (NoSuchFileException ignore) {}

        stateSpace.setInitialState(initialState);
        stateSpace.getAcceptableStates().addAll(acceptableStates);
        stateSpace.getStates().addAll(states);

        return stateSpace;
    }

    public static StateSpace getData(Path stateSpacePath) throws IOException
    {
        return Util.getData(stateSpacePath, null);
    }
}
