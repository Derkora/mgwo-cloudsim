package org.mgwo.cloudsim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.*;

public class MGWO {
    private final int populationSize = 50;  // Ukuran populasi
    private final int maxIterations = 100;  // Jumlah iterasi
    private final double alpha = 0.5;
    private final double beta = 0.3;
    private final double delta = 0.2;
    
    private FitnessFunction fitnessFunction;

    public MGWO(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public int[] optimize() {
        int[] bestSolution = new int[fitnessFunction.getCloudletCount()];
        double bestFitness = Double.MAX_VALUE;

        for (int iter = 0; iter < maxIterations; iter++) {
            int[] currentSolution = generateSolution();
            double currentFitness = fitnessFunction.calculateFitness(currentSolution);

            if (currentFitness < bestFitness) {
                bestFitness = currentFitness;
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }

    private int[] generateSolution() {
        int[] solution = new int[fitnessFunction.getCloudletCount()];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = new Random().nextInt(fitnessFunction.getVmCount());
        }
        return solution;
    }
}
