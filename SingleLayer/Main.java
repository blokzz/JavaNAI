package SingleLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<String> languages = new ArrayList<>(
                List.of("english", "spanish", "french", "german", "turkish", "danish"));
        PrepareDataset dataset = new PrepareDataset("language_detection.csv");
        List<Observation> data = dataset.readDataset(languages.toArray(new String[0]));
        List<Observation> trainData = dataset.trainTestSplit(data).get("train");
        List<Observation> testData = dataset.trainTestSplit(data).get("test");

        System.out.println("Training model for custom text option (50 epochs)...");
        List<Perceptron> customNeurons = new ArrayList<>();
        for (String language : languages) {
            double[] initialWeights = new double[26];
            for (int i = 0; i < 26; i++) {
                initialWeights[i] = Math.random() * 2 - 1;
            }
            customNeurons.add(new Perceptron(26, 0.3, 0.7, initialWeights, language));
        }
        SingleLayerNeuralNetwork customNetwork = new SingleLayerNeuralNetwork(customNeurons);
        customNetwork.train(trainData, 50);
        System.out.println("Model ready!");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Run standard test (different epochs and accuracy)");
            System.out.println("2. Enter your own text to check the language");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                int[] epochs = { 1, 2, 3, 4, 5, 10, 50, 100, 150, 200 };
                for (int epoch : epochs) {
                    List<Perceptron> freshNeurons = new ArrayList<>();
                    for (String language : languages) {
                        double[] initialWeights = new double[26];
                        for (int i = 0; i < 26; i++) {
                            initialWeights[i] = Math.random() * 2 - 1;
                        }
                        freshNeurons.add(new Perceptron(26, 0.3, 0.7, initialWeights, language));
                    }
                    SingleLayerNeuralNetwork currentNetwork = new SingleLayerNeuralNetwork(freshNeurons);

                    currentNetwork.train(trainData, epoch);
                    List<String> predictions = currentNetwork.predict(testData);
                    List<String> trueLabels = new ArrayList<>();
                    for (Observation obs : testData) {
                        trueLabels.add(obs.getLabel());
                    }
                    System.out.println("========================================================================");
                    System.out.println("Stats for " + epoch + " epochs: " + "\n" +
                            "acc : " + EvaluationMetrics.measureAccuracy(trueLabels, predictions) + "\n" +
                            "prec : " + EvaluationMetrics.measurePrecision(trueLabels, predictions) + "\n" +
                            "recall : " + EvaluationMetrics.measureRecall(trueLabels, predictions) + "\n" +
                            "f1 : " + EvaluationMetrics.measureF1Score(trueLabels, predictions));
                    System.out.println("========================================================================");
                }
            } else if (choice.equals("2")) {
                System.out.println("Enter your text (in one of the languages: " + String.join(", ", languages) + "):");
                String text = scanner.nextLine();

                Map<String, Integer> letters = new HashMap<>();
                for (int i = 0; i < 26; i++) {
                    letters.put(String.valueOf((char) ('a' + i)), 0);
                }

                String redactedLine = text.toLowerCase().trim().replace(" ", "");
                for (char c : redactedLine.toCharArray()) {
                    if (c >= 'a' && c <= 'z') {
                        letters.put(String.valueOf(c), letters.get(String.valueOf(c)) + 1);
                    }
                }
                double[] features = new double[26];
                for (int i = 0; i < 26; i++) {
                    features[i] = (double) letters.get(String.valueOf((char) ('a' + i)))
                            / (redactedLine.length() > 0 ? redactedLine.length() : 1);
                }

                Observation obs = new Observation(features, "unknown");
                List<String> prediction = customNetwork.predict(List.of(obs));
                System.out.println("Predicted language: " + prediction.get(0));
            } else if (choice.equals("3")) {
                System.out.println("Closing program...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}
