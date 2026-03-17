package Perceptron;

import java.util.List;

public class EvaluationMetrics {

    public static double measureAccuracy(double[] trueLabels, double[] predictedLabels) {
        if (trueLabels.length != predictedLabels.length) {
            throw new IllegalArgumentException("Listy etykiet muszą mieć tę samą długość.");
        }

        int correctPredictions = 0;
        for (int i = 0; i < trueLabels.length; i++) {
            if (trueLabels[i] == predictedLabels[i]) {
                correctPredictions++;
            }
        }

        return (double) correctPredictions / trueLabels.length;
    }

}
