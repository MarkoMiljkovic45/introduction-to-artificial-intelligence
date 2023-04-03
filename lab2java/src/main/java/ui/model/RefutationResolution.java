package ui.model;

import java.util.ArrayList;
import java.util.List;

public class RefutationResolution {

    private List<Clause> clauses;
    private List<Clause> newClauses;
    private List<Clause> inputClauses;
    private List<Clause> negatedTargetClauses;
    private Clause target;

    public RefutationResolution(List<Clause> clauses, List<Clause> inputClauses, List<Clause> negatedTargetClauses, Clause target) {
        this.clauses = clauses;
        this.newClauses = new ArrayList<>();
        this.inputClauses = inputClauses;
        this.negatedTargetClauses = negatedTargetClauses;
        this.target = target;
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public Clause getTarget() {
        return target;
    }

    public boolean resolve() {
        //TODO
        return false;
    }
}
