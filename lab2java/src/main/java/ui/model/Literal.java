package ui.model;

import java.util.Objects;

/**
 * Represents an atomic formula
 */
public class Literal {

    private final String name;
    private boolean negated;

    public Literal(String name, boolean negated) {
        this.name = name.toLowerCase();
        this.negated = negated;
    }

    public Literal(String name) {
        this(name, false);
    }

    public String getName() {
        return name;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    public void negate() {
        negated = !negated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return name.equals(literal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
