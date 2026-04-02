package SingleLayer;

import java.util.ArrayList;

import java.util.List;

public class SingleLayerNeuralNetwork {
    private List<Perceptron> neurons;

    public SingleLayerNeuralNetwork(List<Perceptron> neurons) {
        this.neurons = neurons;
    }

    public void train(List<Observation> dataset, int epochs) {
        for (Perceptron neuron : neurons) {
            neuron.train(dataset, epochs);
        }
    }

    public List<String> predict(List<Observation> dataset) {
        List<String> predictions = new ArrayList<>();
        // int count = 0;
        for (Observation observation : dataset) {
            double maxActivation = Double.NEGATIVE_INFINITY;
            String predictedLabel = null;
            // if (count < 2) {
            // System.out.println("Predicting for true label: " + observation.getLabel());
            // }

            for (Perceptron neuron : neurons) {
                double activation = neuron.activationFunction(observation.getFeatures());
                // if (count < 2) {
                // System.out.println(" Neuron " + neuron.getTargetClass() + " activation: " +
                // activation);
                // }
                if (activation > maxActivation || Double.isNaN(activation)) {
                    maxActivation = activation;
                    predictedLabel = neuron.getTargetClass();
                }
            }
            predictions.add(predictedLabel);
            // count++;
        }
        return predictions;
    }

    public List<Perceptron> getNeurons() {
        return neurons;
    }
}
