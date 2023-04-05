package ui.util;

import ui.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Util {

    public static void printResult(Clause nil, Clause target) {
        List<Clause> usedClauses = reconstructUsedClauses(nil);
        Map<Clause, Integer> clauseIndexMap  = new HashMap<>();
        int usedClausesSize = usedClauses.size();
        int i               = 0;

        while (i < usedClausesSize) {
            int    index  = i + 1;
            Clause clause = usedClauses.get(i);
            clauseIndexMap.put(clause, index);

            if (clause.getFirstParent() != null) {
                System.out.println("===============");
                break;
            }

            System.out.printf("%d. %s\n", index, clause);
            i++;
        }

        while (i < usedClausesSize) {
            int index = i + 1;
            Clause clause = usedClauses.get(i);
            clauseIndexMap.put(clause, index);

            int firstParentIndex  = clauseIndexMap.get(clause.getFirstParent());
            int secondParentIndex = clauseIndexMap.get(clause.getSecondParent());

            if (firstParentIndex > secondParentIndex) {
                int tmp = firstParentIndex;
                firstParentIndex = secondParentIndex;
                secondParentIndex = tmp;
            }
            System.out.printf("%d. %s (%d, %d)\n", index, clause, firstParentIndex, secondParentIndex);
            i++;
        }
        System.out.println("===============");
        System.out.printf("[CONCLUSION]: %s is true", target);
    }

    /**
     * Reconstructs the used clauses by putting the input clauses and
     * negated target clauses first and then the newly generated clauses
     * @param nilClause Used to reconstruct the used clauses
     * @return Used clauses
     */
    private static List<Clause> reconstructUsedClauses(Clause nilClause) {
        LinkedList<Clause> usedClauses = new LinkedList<>();
        reconstructUsedClausesRecursive(usedClauses, nilClause);
        return usedClauses;
    }

    private static void reconstructUsedClausesRecursive(LinkedList<Clause> usedClauses, Clause clause) {
        if (clause == null) return;

        Clause firstParent  = clause.getFirstParent();
        Clause secondParent = clause.getSecondParent();

        if (firstParent == null && secondParent == null) {
            usedClauses.addFirst(clause);
            return;
        }

        reconstructUsedClausesRecursive(usedClauses, firstParent);
        reconstructUsedClausesRecursive(usedClauses, secondParent);
        usedClauses.addLast(clause);
    }

    public static RefutationResolution initRefutationResolution(List<Clause> clauses) {
        int          indexOfTarget = clauses.size() - 1;
        Clause       target        = clauses.get(indexOfTarget);
        Set<Clause> negatedTarget = negateTarget(target);

        clauses.remove(indexOfTarget);
        clauses.addAll(negatedTarget);

        return new RefutationResolution(new TreeSet<>(clauses), negatedTarget, target);
    }

    private static Set<Clause> negateTarget(Clause clause) {
        Set<Clause> negatedTarget = new TreeSet<>();

        for (Literal literal: clause) {
            negatedTarget.add(new Clause(new Literal(literal.getName(), !literal.isNegated())));
        }

        return negatedTarget;
    }

    public static List<Clause> loadClauses(Path clausesPath) throws IOException {
        List<Clause> clauses = new ArrayList<>();
        Scanner reader       = new Scanner(clausesPath);

        while (reader.hasNext()) {
            String line = reader.nextLine().toLowerCase();
            if (line.startsWith("#")) continue;

            Clause clause = new Clause();

            for (String literal : line.split(" v ")) {
                String name;
                boolean negated;

                if (literal.startsWith("~")) {
                    name = literal.substring(1);
                    negated = true;
                } else {
                    name = literal;
                    negated = false;
                }

                clause.addLiteral(new Literal(name, negated));
            }
            clauses.add(clause);
        }

        return clauses;
    }

    public static List<Command> loadCommands(Path commandsPath) throws IOException {
        List<Command> commands = new ArrayList<>();
        Scanner       reader   = new Scanner(commandsPath);

        while (reader.hasNext()) {
            String line = reader.nextLine().toLowerCase();
            if (line.startsWith("#")) continue;

            Clause      clause = new Clause();
            CommandType type   = getType(line);

            if (type == null) throw new IllegalArgumentException("Unsupported file format");

            String clauseString = line.replace(" " + type, "");

            for (String literal : clauseString.split(" v ")) {
                String name;
                boolean negated;

                if (literal.startsWith("~")) {
                    name = literal.substring(1);
                    negated = true;
                } else {
                    name = literal;
                    negated = false;
                }

                clause.addLiteral(new Literal(name, negated));
            }

            commands.add(new Command(type, clause));
        }

        return commands;
    }

    private static CommandType getType(String line) {
        for (CommandType type: CommandType.values()) {
            if (line.endsWith(type.toString())) {
                return type;
            }
        }
        return null;
    }
}
