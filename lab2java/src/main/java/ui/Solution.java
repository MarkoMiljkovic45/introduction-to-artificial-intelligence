package ui;

import ui.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

	public static void main(String[] args) {
		try {
			if (args.length == 2) {
				String resolution  = args[0];
				Path   clausesPath = Path.of(args[1]);

				if (resolution.equals("resolution")) {
					resolve(clausesPath);
					return;
				}
			}

			if (args.length == 3) {
				String cooking      = args[0];
				Path   clausesPath  = Path.of(args[1]);
				Path   commandsPath = Path.of(args[2]);

				if (cooking.equals("cooking")) {
					executeCommands(clausesPath, commandsPath);
					return;
				}
			}

			System.out.println("Invalid arguments.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void resolve(Path clausesPath) throws IOException {
		RefutationResolution resolver   = initRefutationResolution(loadClauses(clausesPath));
		boolean              conclusion = resolver.resolve();

		if (!conclusion) {
			System.out.printf("[CONCLUSION]: %s is unknown", resolver.getTarget());
			return;
		}

		Clause nilClause = resolver.getNil();
		List<Clause> usedClauses = reconstructUsedClauses(nilClause);
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
		System.out.printf("[CONCLUSION]: %s is true", resolver.getTarget());
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

	private static RefutationResolution initRefutationResolution(List<Clause> clauses) {
		int          indexOfTarget = clauses.size() - 1;
		Clause       target        = clauses.get(indexOfTarget);
		List<Clause> negatedTarget = negateTarget(target);

		clauses.remove(indexOfTarget);
		clauses.addAll(negatedTarget);

		return new RefutationResolution(new TreeSet<>(clauses), new TreeSet<>(negatedTarget), target);
	}

	private static List<Clause> negateTarget(Clause clause) {
		List<Clause> negatedTarget = new ArrayList<>();

		for (Literal literal: clause) {
			negatedTarget.add(new Clause(new Literal(literal.getName(), !literal.isNegated())));
		}

		return negatedTarget;
	}

	private static void executeCommands(Path classesPath, Path commandsPath) {
		//TODO
	}

	private static List<Clause> loadClauses(Path clausesPath) throws IOException {
		List<Clause> clauses = new ArrayList<>();
		Scanner reader       = new Scanner(clausesPath);

		while (reader.hasNext()) {
			String line = reader.nextLine();
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

	private static List<Command> loadCommands(Path commandsPath) throws IOException {
		List<Command> commands = new ArrayList<>();
		Scanner       reader   = new Scanner(commandsPath);

		while (reader.hasNext()) {
			String line = reader.nextLine();
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
