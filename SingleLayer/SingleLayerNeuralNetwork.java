package SingleLayer;

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

    public List<Perceptron> getNeurons() {
        return neurons;
    }
}
