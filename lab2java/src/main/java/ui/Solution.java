package ui;

import ui.model.Clause;
import ui.model.Command;
import ui.model.CommandExecutor;
import ui.model.RefutationResolution;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

import static ui.util.Util.*;

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

		Clause nil = resolver.getNil();
		printResult(nil, resolver.getTarget());

	}

	private static void executeCommands(Path clausesPath, Path commandsPath) throws IOException {
		CommandExecutor executor = new CommandExecutor(new TreeSet<>(loadClauses(clausesPath)));
		List<Command>   commands = loadCommands(commandsPath);
		commands.forEach(executor::executeCommand);
	}
}
