package Perceptron;

import java.util.List;

public class Perceptron {
    private int dimension;
    private double[] weights;
    private double treshold;
    private double learningRate;
    private double beta;

    public Perceptron(int dimension, double learningRate, double beta) {
        this.dimension = dimension;
        this.learningRate = learningRate;
        this.beta = beta;
        this.weights = new double[dimension];
        this.treshold = 0;
    }

    public void train(List<Observation> dataset) {

    }
}
