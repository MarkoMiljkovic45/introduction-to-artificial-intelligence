package ui.model;

import java.util.Objects;

/**
 * Represents an atomic formula
 */
public class Literal implements Comparable<Literal> {

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

    @Override
    public String toString() {
        if (negated) {
            return "~" + name;
        } else {
            return name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return negated == literal.negated && Objects.equals(name, literal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, negated);
    }

    @Override
    public int compareTo(Literal literal) {
        int nameDiff = name.compareTo(literal.getName());

        if (nameDiff != 0) return nameDiff;

        if (negated != literal.negated) return negated ? 1 : -1;

        return 0;
    }
}
