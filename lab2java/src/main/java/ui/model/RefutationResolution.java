package ui.model;

import java.util.*;

public class RefutationResolution {

    private final Set<Clause> clauses;
    private final Set<Clause> setOfSupport;
    private final Clause target;
    private Clause nil;

    public RefutationResolution(Set<Clause> clauses, Set<Clause> setOfSupport, Clause target) {
        this.clauses      = clauses;
        this.setOfSupport = setOfSupport;
        this.target       = target;
        this.nil          = null;
    }

    public Clause getTarget() {
        return target;
    }

    public Clause getNil() {
        return nil;
    }

    public boolean resolve() {
        while(true) {
            simplify();
            Set<Clause> resolvents = getResolvents();

            if (resolvents == null) {
                return true;
            }

            if (clauses.containsAll(resolvents)) {
                return false;
            }
            clauses.addAll(resolvents);
            setOfSupport.addAll(resolvents);
        }
    }

    /**
     * Removes redundant clauses from <code>clauses</code> and <code>setOfSupport</code>
     * <p>
     * Redundant clauses:
     * <ul>
     *     <li>If the set contains a pair of clauses C1 and C2 such that C1 ⊆ C2,
     *     then clause C2 may be removed from the set</li>
     *     <li>Every valid clause (tautology)</li>
     * </ul>
     */
    private void simplify() {
        Iterator<Clause> iterator  = clauses.iterator();
        List<Clause>     superSets = new ArrayList<>();

        while (iterator.hasNext()) {
            Clause clause = iterator.next();

            if (clause.isTautology()) {
                iterator.remove();
            }

            Clause superSet = isSubsetOf(clause, clauses);

            if (superSet != null) {
                superSets.add(superSet);
            }
        }

        superSets.forEach(clauses::remove);
        superSets.forEach(setOfSupport::remove);
    }

    private Clause isSubsetOf(Clause clause, Collection<Clause> clauses) {
        for (Clause other : clauses) {
            boolean otherSmaller = other.getLiterals().size() < clause.getLiterals().size();
            boolean isEqual      = clause == other;

            if (otherSmaller || isEqual) {
                continue;
            }

            if (other.containsClause(clause)) {
                return other;
            }
        }

        return null;
    }

    /**
     * Combines clauses from <code>clauses</code> set with clauses from <code>setOfSupport</code> set
     * and returns the newly created clauses
     *
     * @return Resolvents from combining existing clauses
     */
    public Set<Clause> getResolvents() {
        Set<Clause> resolvents = new TreeSet<>();

        for (Clause first: clauses) {
            for (Clause second: setOfSupport) {
                if (first == second) {
                    continue;
                }

                Clause resolvent = resolveClauses(first, second);

                if (resolvent == null) {
                    continue;
                }

                if (resolvent.getLiterals().size() == 0) {
                    nil = resolvent;
                    return null;
                }

                resolvents.add(resolvent);
            }
        }

        return resolvents;
    }

    private Clause resolveClauses(Clause first, Clause second) {
        Clause resolvent = new Clause(first, second);

        Iterator<Literal> firstIterator  = first.getLiterals().iterator();
        Iterator<Literal> secondIterator = second.getLiterals().iterator();

        try {
            Literal firstLiteral  = firstIterator.next();
            Literal secondLiteral = secondIterator.next();

            String firstName  = firstLiteral.getName();
            String secondName = secondLiteral.getName();

            int     cmp           = firstName.compareTo(secondName);
            boolean sameNegations = firstLiteral.isNegated() == secondLiteral.isNegated();

            while(cmp != 0 || sameNegations) {
                if (cmp < 0) {
                    firstLiteral = firstIterator.next();
                } else {
                    secondLiteral = secondIterator.next();
                }

                firstName  = firstLiteral.getName();
                secondName = secondLiteral.getName();

                cmp = firstName.compareTo(secondName);
                sameNegations = firstLiteral.isNegated() == secondLiteral.isNegated();
            }

            resolvent.getLiterals().addAll(first.getLiterals());
            resolvent.getLiterals().addAll(second.getLiterals());

            resolvent.getLiterals().remove(firstLiteral);
            resolvent.getLiterals().remove(secondLiteral);

            return resolvent;
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }
}
