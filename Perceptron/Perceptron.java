package Perceptron;

import java.util.List;

public class Perceptron {
    private int dimension;
    private double[] weights;
    private double treshold;
    private double learningRate;
    private double beta;

    public Perceptron(int dimension, double learningRate, double beta, double[] weights) {
        this.dimension = dimension;
        this.learningRate = learningRate;
        this.beta = beta;
        this.weights = weights;
        this.treshold = 0;
    }

    public void train(List<Observation> dataset, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (Observation obs : dataset) {
                double[] features = obs.getFeatures();
                double label = obs.getLabel();
                double output = activationFunction(features);
                double error = label - output;
                updateWeights(features, error);
            }
        }
    }

    private double activationFunction(double[] features) {
        double sum = 0;
        for (int i = 0; i < dimension; i++) {
            sum += features[i] * weights[i];
        }
        return sum > treshold ? 1 : 0;
    }

    private void updateWeights(double[] features, double error) {
        for (int i = 0; i < dimension; i++) {
            weights[i] += learningRate * error * features[i];
        }
        treshold -= learningRate * error;
    }

    public double[] predict(List<Observation> dataset) {
        double[] predictions = new double[dataset.size()];
        for (int i = 0; i < dataset.size(); i++) {
            predictions[i] = activationFunction(dataset.get(i).getFeatures());
        }
        return predictions;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getTreshold() {
        return treshold;
    }
}
