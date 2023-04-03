package ui.model;

import java.util.*;

/**
 * Represents a formula in the Conjunctive Normal Form
 */
public class Clause implements Comparable<Clause>, Iterable<Literal> {

    private final Clause firstParent;
    private final Clause secondParent;
    private final Set<Literal> literals;

    private Clause(Clause firstParent, Clause secondParent, Set<Literal> literals) {
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.literals = literals;
    }

    public Clause(Literal...literals) {
        this(null, null, new TreeSet<>(Set.of(literals)));
    }

    public Clause() {
        this(null, null, new TreeSet<>());
    }

    public Clause getFirstParent() {
        return firstParent;
    }

    public Clause getSecondParent() {
        return secondParent;
    }



    public void addLiteral(Literal l) {
        literals.add(l);
    }

    public boolean containsClause(Clause other) {
        return literals.containsAll(other.literals);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Iterator<Literal> iterator = literals.iterator();

        while(iterator.hasNext()) {
            sb.append(iterator.next());

            if (iterator.hasNext()) sb.append(" v ");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(literals, clause.literals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }

    @Override
    public int compareTo(Clause other) {
        int sizeDiff = literals.size() - other.literals.size();

        if (sizeDiff != 0) return sizeDiff;

        Iterator<Literal> iterator      = literals.iterator();
        Iterator<Literal> otherIterator = other.literals.iterator();

        while (iterator.hasNext()) {
            Literal literal      = iterator.next();
            Literal otherLiteral = otherIterator.next();

            int diff = literal.compareTo(otherLiteral);

            if (diff != 0) return diff;
        }

        return 0;
    }

    @Override
    public Iterator<Literal> iterator() {
        return literals.iterator();
    }
}
