package ui.nn;

import ui.model.DataSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class NeuralNet {
    private static final int INFO_ON_ITER = 2000;

    private final String architecture;
    private final int populationSize;
    private final int elitism;
    private final double pMutation;
    private final double gaussStdDev;
    private final long iter;
    private DataSet trainSet;
    private List<NeuralNetInstance> population;
    private NeuralNetInstance bestInstance;

    public NeuralNet(String architecture, int populationSize, int elitism, double pMutation, double gaussStdDev, long iter) {
        this.architecture = architecture;
        this.populationSize = populationSize;
        this.elitism = elitism;
        this.pMutation = pMutation;
        this.gaussStdDev = gaussStdDev;
        this.iter = iter;
        this.population = new ArrayList<>(populationSize);
    }

    public void setTrainSet(DataSet trainSet) {
        this.trainSet = trainSet;
    }

    public void fit(DataSet trainSet) {
        setTrainSet(trainSet);
        initPopulation();
        for (int i = 1; i <= iter; i++) {
            iterationStep();

            if (i % INFO_ON_ITER == 0) {
                System.out.printf("[Train error @%d]: %.6f\n", i, bestInstance.getLatestEvaluation());
            }
        }
    }

    private void iterationStep() {
        population.forEach(instance -> instance.evaluate(trainSet));
        population.sort(Comparator.comparing(NeuralNetInstance::getLatestEvaluation));
        bestInstance = population.get(0);

        List<NeuralNetInstance> nextGeneration = new ArrayList<>(population.subList(0, elitism));

        double maxError = population.get(populationSize - 1).getLatestEvaluation();
        double totalFitness = population.stream()
                .mapToDouble(instance -> maxError - instance.getLatestEvaluation())
                .sum();

        while (nextGeneration.size() < populationSize) {
            NeuralNetInstance firstParent = pickParent(totalFitness, maxError);
            NeuralNetInstance secondParent = pickParent(totalFitness, maxError);

            nextGeneration.add(NeuralNetInstance.pair(firstParent, secondParent, pMutation, gaussStdDev));
        }

        population = nextGeneration;
    }

    /**
     * Picks a parent using fitness proportional selection;
     *
     * @param totalFitness total population fitness
     * @return an instance from the population
     */
    private NeuralNetInstance pickParent(double totalFitness, double maxError) {
        double pick = new Random().nextDouble();
        double totalLen = 0;

        for (NeuralNetInstance instance: population) {
            totalLen += (maxError - instance.getLatestEvaluation()) / totalFitness;

            if (pick < totalLen) {
                return instance;
            }
        }

        throw new RuntimeException("Unexpected total fitness");
    }

    private void initPopulation() {
        population.clear();

        for (int i = 0; i < populationSize; i++) {
            NeuralNetInstance instance = new NeuralNetInstance(
                    trainSet.getInputLayerSize(),
                    trainSet.getOutputLayerSize(),
                    architecture
            );

            instance.initHiddenLayers();

            population.add(instance);
        }
    }

    public void test(DataSet testSet) {
        bestInstance.evaluate(testSet);
        System.out.printf("[Test error]: %.6f\n", bestInstance.getLatestEvaluation());
    }
}
