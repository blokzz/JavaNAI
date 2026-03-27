package SingleLayer;

public class Observation {
    private double[] features;
    private double label;

    public Observation(double[] features, double label) {
        this.features = features;
        this.label = label;
    }

    public double[] getFeatures() {
        return features;
    }

    public double getLabel() {
        return label;
    }
}
