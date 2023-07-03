package ui.math;

import java.util.function.Function;

public class Sigmoid implements Function<Double, Double> {
    @Override
    public Double apply(Double value) {
        double denominator = 1 + Math.exp(-1 * value);
        return 1 / denominator;
    }
}
