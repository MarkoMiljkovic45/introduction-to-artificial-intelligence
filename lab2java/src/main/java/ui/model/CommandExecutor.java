package ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ui.util.Util.*;

public class CommandExecutor {

    private final Set<Clause> clauses;

    public CommandExecutor(Set<Clause> clauses) {
        this.clauses = clauses;
    }

    public void executeCommand(Command command) {
        switch (command.type()) {
            case ADD    -> addClause(command.clause());
            case REMOVE -> removeClause(command.clause());
            case QUERY ->  resolve(command);
        }
    }

    private void addClause(Clause clause) {
        clauses.add(clause);
    }

    private void removeClause(Clause clause) {
        clauses.remove(clause);
    }

    private void resolve(Command command) {
        List<Clause> clauses = new ArrayList<>(this.clauses);
        clauses.add(command.clause());

        RefutationResolution resolver   = initRefutationResolution(clauses);
        boolean              conclusion = resolver.resolve();

        System.out.printf("User’s command: %s\n", command);

        if (!conclusion) {
            System.out.printf("[CONCLUSION]: %s is unknown", resolver.getTarget());
        } else {
            Clause nil = resolver.getNil();
            printResult(nil, resolver.getTarget());
        }

        System.out.print("\n\n");
    }
}
