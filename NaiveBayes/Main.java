package NaiveBayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\KAMIL\\Desktop\\JavaNAI\\outgame.csv";
        PrepareDataset prepareDataset = new PrepareDataset(path);
        List<Dataset> dataset = prepareDataset.readDataset();

        System.out.println("Wczytano " + dataset.size() + " rekordów.");

        Map<String, List<Dataset>> splitData = prepareDataset.trainTestSplit(dataset, 10);
        List<Dataset> trainData = splitData.get("train");
        List<Dataset> testData = splitData.get("test");

        System.out.println("Rozmiar zbioru treningowego: " + trainData.size());
        System.out.println("Rozmiar zbioru testowego: " + testData.size());

        NaiveBayesClassifier classifier = new NaiveBayesClassifier(true, trainData);

        List<String> trueLabels = new ArrayList<>();
        List<String> predictedLabels = new ArrayList<>();

        System.out.println("\n--- Wyniki predykcji ---");
        for (Dataset testExample : testData) {
            String predicted = classifier.predict(testExample.features());
            String actual = testExample.label();

            trueLabels.add(actual);
            predictedLabels.add(predicted);

            System.out.println("Oczekiwano: " + actual + " | Przewidziano: " + predicted);
        }

        System.out.println("\n--- Metryki ewaluacji ---");
        System.out.printf("Accuracy: %.2f%%\n", EvaluationMetrics.measureAccuracy(trueLabels, predictedLabels) * 100);
        System.out.printf("Precision: %.2f%%\n", EvaluationMetrics.measurePrecision(trueLabels, predictedLabels) * 100);
        System.out.printf("Recall: %.2f%%\n", EvaluationMetrics.measureRecall(trueLabels, predictedLabels) * 100);
        System.out.printf("F1-Score: %.2f%%\n", EvaluationMetrics.measureF1Score(trueLabels, predictedLabels) * 100);
    }
}
