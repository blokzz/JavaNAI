package Perceptron;

import java.util.Scanner;
import java.lang.Math;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        PrepareDataset dataset = new PrepareDataset("iris.csv");
        List<Observation> data = dataset.readDataset();
        Map<String, List<Observation>> trainTestSplit = dataset.trainTestSplit(data);
        List<Observation> trainData = trainTestSplit.get("train");
        List<Observation> testData = trainTestSplit.get("test");
        double[] weights = { Math.random(), Math.random(), Math.random(), Math.random() };
        Perceptron p = new Perceptron(4, 0.02, 0.7, weights);
        p.train(trainData, 100);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n========= MENU  =========");
            System.out.println("1. Klasyfikuj nowa obserwacja (recznie)");
            System.out.println("2. Test dla iris.csv");
            System.out.println("3. Wizualizacja dla dwoch cech");
            System.out.println("4. Wyjscie");
            System.out.print("Wybierz opcje: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleManualEntry(scanner, p);
                    break;
                case "2":
                    runComparisonTest(data, testData);
                    break;
                case "3":
                    handleVisualisationTest(trainData, testData);
                    break;
                case "4":
                    running = false;
                    System.out.println("Zamykanie programu...");
                    break;
                default:
                    System.out.println("Nieprawidlowa opcja.");
            }
        }
        scanner.close();
    }

    private static void runComparisonTest(List<Observation> trainingData, List<Observation> testData) {

        int[] epochs = { 1, 3, 10, 50, 100 };
        double[] weights = { Math.random(), Math.random(), Math.random(), Math.random() };
        for (int epoch : epochs) {
            Perceptron p = new Perceptron(4, 0.02, 0.7, weights);
            p.train(trainingData, epoch);
            double[] predicted = p.predict(testData);
            double[] trueLabels = new double[testData.size()];
            for (int i = 0; i < testData.size(); i++) {
                trueLabels[i] = testData.get(i).getLabel();
            }
            System.out.println(
                    "Accuracy for " + epoch + " epochs: " + EvaluationMetrics.measureAccuracy(trueLabels, predicted));
        }

    }

    private static void handleManualEntry(Scanner scanner, Perceptron p) {
        System.out.println("Podaj cechy: ");
        double[] features = new double[4];
        features[0] = scanner.nextDouble();
        features[1] = scanner.nextDouble();
        features[2] = scanner.nextDouble();
        features[3] = scanner.nextDouble();
        List<Observation> dataset = new ArrayList<>();
        dataset.add(new Observation(features, 0));
        scanner.nextLine();
        double[] prediction = p.predict(dataset);
        String klasa = prediction[0] == 1 ? "Setosa" : "Versicolor";
        System.out.println("Klasa: " + klasa);
    }

    private static void handleVisualisationTest(List<Observation> trainingData, List<Observation> testData) {
        int[] epochs = { 1, 3, 10, 50, 100 };
        double[] weights = { Math.random(), Math.random() };
        Perceptron bestModel = null;
        for (int epoch : epochs) {
            Perceptron p = new Perceptron(2, 0.02, 0.7, weights);
            p.train(trainingData, epoch);
            double[] predicted = p.predict(testData);
            double[] trueLabels = new double[testData.size()];
            for (int i = 0; i < testData.size(); i++) {
                trueLabels[i] = testData.get(i).getLabel();
            }
            System.out.println(
                    "Accuracy for " + epoch + " epochs: " + EvaluationMetrics.measureAccuracy(trueLabels, predicted));
            bestModel = p;
        }
        if (bestModel != null)

        {
            Visualizer.showPlot(testData, bestModel.getWeights(), bestModel.getTreshold());
        }

    }

}
