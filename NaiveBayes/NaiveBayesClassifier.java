package NaiveBayes;

import java.util.Map;

public class NaiveBayesClassifier {

    private boolean applySmoothingAll;
    private TrainDataset dataset;
    private double apriori;
    private double posteriori;

    public NaiveBayesClassifier(boolean applySmoothingAll, TrainDataset dataset) {
        this.applySmoothingAll = applySmoothingAll;
        this.dataset = dataset;
        this.apriori = dataset
        this.posteriori = dataset.calculatePosteriori();
    }

}
