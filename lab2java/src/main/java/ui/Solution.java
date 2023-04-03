package ui;

import ui.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

	public static void main(String[] args) {
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
	}

	private static void resolve(Path classesPath) {
		//TODO
	}

	private static void executeCommands(Path classesPath, Path commandsPath) {
		//TODO
	}

	private static Set<Clause> loadClauses(Path clausesPath) throws IOException {
		Set<Clause> clauses = new TreeSet<>();
		Scanner reader      = new Scanner(clausesPath);

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
