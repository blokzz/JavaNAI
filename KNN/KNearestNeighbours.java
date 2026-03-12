import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

class KNearestNeighbours {
    private int k;
    private List<Observation> trainingData;

    public KNearestNeighbours(int k, List<Observation> trainingData) {
        this.k = k;
        this.trainingData = trainingData;
    }

    public double calculateEuclideanDistance(double[] features1, double[] features2) {
        double sum = 0;
        for (int i = 0; i < features1.length; i++) {
            sum += Math.pow(features1[i] - features2[i], 2);
        }
        return Math.sqrt(sum);
    }

    public List<NeighborDistance> sortDistances(List<NeighborDistance> distances) {
        for (int i = 1; i < distances.size(); i++) {
            NeighborDistance key = distances.get(i);
            int j = i - 1;
            while (j >= 0 && distances.get(j).distance > key.distance) {
                distances.set(j + 1, distances.get(j));
                j = j - 1;
            }
            distances.set(j + 1, key);
        }
        return distances;
    }

    public String findPredictedClass(double[] testFeatures) {
        List<NeighborDistance> distances = new ArrayList<>();
        for (Observation observation : trainingData) {
            double distance = calculateEuclideanDistance(testFeatures, observation.getFeatures());
            distances.add(new NeighborDistance(distance, observation.getLabel()));
        }
        sortDistances(distances);
        int limit = Math.min(k, distances.size());
        List<NeighborDistance> kNearestNeighbors = distances.subList(0, limit);

        Map<String, Integer> labelCounts = new HashMap<>();
        for (NeighborDistance neighbor : kNearestNeighbors) {
            labelCounts.put(neighbor.label, labelCounts.getOrDefault(neighbor.label, 0) + 1);
        }

        String predictedClass = labelCounts.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<String> winners = entry.getValue();
                    return winners.get(new Random().nextInt(winners.size()));
                })
                .orElse(null);

        return predictedClass;
    }

    public Observation classify(double[] testFeatures) {
        String predictedClass = findPredictedClass(testFeatures);
        return new Observation(testFeatures, predictedClass);
    }

    private class NeighborDistance {
        double distance;
        String label;

        NeighborDistance(double d, String l) {
            this.distance = d;
            this.label = l;
        }
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}