import java.util.List;

public class EvaluationMetrics {

    public static double measureAccuracy(List<String> trueLabels, List<String> predictedLabels) {
        if (trueLabels.size() != predictedLabels.size()) {
            throw new IllegalArgumentException("Listy etykiet muszą mieć tę samą długość.");
        }

        int correctPredictions = 0;
        for (int i = 0; i < trueLabels.size(); i++) {
            if (trueLabels.get(i).equals(predictedLabels.get(i))) {
                correctPredictions++;
            }
        }

        return (double) correctPredictions / trueLabels.size();
    }

}
