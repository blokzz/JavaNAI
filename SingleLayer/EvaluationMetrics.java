package SingleLayer;

import java.util.List;

public class EvaluationMetrics {

    public static double measureAccuracy(List<String> trueLabels, List<String> predictedLabels) {
        if (trueLabels.size() != predictedLabels.size()) {
            throw new IllegalArgumentException("Listy etykiet muszą mieć tę samą długość.");
        }

        int correctPredictions = 0;
        for (int i = 0; i < trueLabels.size(); i++) {
            if (trueLabels.get(i).equalsIgnoreCase(predictedLabels.get(i))) {
                correctPredictions++;
            }
        }

        return (double) correctPredictions / trueLabels.size();
    }

    public static double measurePrecision(List<String> trueLabels, List<String> predictedLabels) {
        if (trueLabels.size() != predictedLabels.size()) {
            throw new IllegalArgumentException("Listy etykiet muszą mieć tę samą długość.");
        }

        java.util.Set<String> classes = new java.util.HashSet<>(trueLabels);
        double totalPrecision = 0.0;

        for (String c : classes) {
            int tp = 0;
            int fp = 0;
            for (int i = 0; i < trueLabels.size(); i++) {
                if (predictedLabels.get(i).equalsIgnoreCase(c)) {
                    if (trueLabels.get(i).equalsIgnoreCase(c)) {
                        tp++;
                    } else {
                        fp++;
                    }
                }
            }
            if (tp + fp > 0) {
                totalPrecision += (double) tp / (tp + fp);
            }
        }

        return totalPrecision / classes.size();
    }

    public static double measureRecall(List<String> trueLabels, List<String> predictedLabels) {
        if (trueLabels.size() != predictedLabels.size()) {
            throw new IllegalArgumentException("Listy etykiet muszą mieć tę samą długość.");
        }

        java.util.Set<String> classes = new java.util.HashSet<>(trueLabels);
        double totalRecall = 0.0;

        for (String c : classes) {
            int tp = 0;
            int fn = 0;
            for (int i = 0; i < trueLabels.size(); i++) {
                if (trueLabels.get(i).equalsIgnoreCase(c)) {
                    if (predictedLabels.get(i).equalsIgnoreCase(c)) {
                        tp++;
                    } else {
                        fn++;
                    }
                }
            }
            if (tp + fn > 0) {
                totalRecall += (double) tp / (tp + fn);
            }
        }

        return totalRecall / classes.size();
    }

    public static double measureF1Score(List<String> trueLabels, List<String> predictedLabels) {
        double precision = measurePrecision(trueLabels, predictedLabels);
        double recall = measureRecall(trueLabels, predictedLabels);

        if (precision + recall == 0.0)
            return 0.0;
        return 2 * (precision * recall) / (precision + recall);
    }

}
