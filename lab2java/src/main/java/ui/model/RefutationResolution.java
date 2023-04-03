package ui.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class RefutationResolution {

    private final Set<Clause> inputClauses;
    private final Set<Clause> negatedTarget;
    private final Set<Clause> generatedClauses;
    private Clause target;

    public RefutationResolution(Set<Clause> inputClauses, Set<Clause> negatedTarget, Clause target) {
        this.inputClauses = inputClauses;
        this.negatedTarget = negatedTarget;
        this.generatedClauses = new TreeSet<>();
        this.target = target;
    }
}
