package Perceptron;

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
        int[] epochs = { 1, 3, 10, 50, 100 };
        double[] weights = { Math.random(), Math.random() };
        Perceptron bestModel = null;
        for (int epoch : epochs) {
            Perceptron p = new Perceptron(2, 0.02, 0.7, weights);
            p.train(trainData, epoch);
            double[] predicted = p.predict(testData);
            double[] trueLabels = new double[testData.size()];
            for (int i = 0; i < testData.size(); i++) {
                trueLabels[i] = testData.get(i).getLabel();
            }
            System.out.println(
                    "Accuracy for " + epoch + " epochs: " + EvaluationMetrics.measureAccuracy(trueLabels, predicted));
            bestModel = p;
        }

        if (bestModel != null) {
            Visualizer.showPlot(testData, bestModel.getWeights(), bestModel.getTreshold());
        }

    }

}
