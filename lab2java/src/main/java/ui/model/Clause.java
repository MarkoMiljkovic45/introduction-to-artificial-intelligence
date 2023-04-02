package ui.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a formula in the Conjunctive Normal Form
 */
public class Clause {

    private final Set<Literal> literals;

    public Clause() {
        this.literals = new HashSet<>();
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }

    public void isSubset(Clause other) {
        //TODO Is this clause a subset of other
    }

}
