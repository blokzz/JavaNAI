package NaiveBayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NaiveBayesClassifier {

    private boolean applySmoothingAll;
    private List<Dataset> dataset;
    private Map<String, Map<String, Double>> propabilitesMap;
    private Map<String, Double> apriori;
    private double posteriori;

    public NaiveBayesClassifier(boolean applySmoothingAll, List<Dataset> dataset) {
        this.applySmoothingAll = applySmoothingAll;
        this.dataset = dataset;
        this.propabilitesMap = new HashMap<>();
        this.apriori = new HashMap<>();

        Set<String> uniqueLabels = new HashSet<>();
        for (Dataset data : dataset) {
            uniqueLabels.add(data.label());
        }
        for (String label : uniqueLabels) {
            apriori.put(label, getApriori(label));
        }

        for (Dataset data : dataset) {
            String[] features = data.features();
            for (int i = 0; i < features.length; i++) {
                String featureValue = features[i];
                propabilitesMap.putIfAbsent(featureValue, new HashMap<>());

                for (String label : uniqueLabels) {
                    if (!propabilitesMap.get(featureValue).containsKey(label)) {
                        double conditionalProb = getConditionalProbability(featureValue, i, label);
                        propabilitesMap.get(featureValue).put(label, conditionalProb);
                    }
                }
            }
        }
    }

    public String predict(String[] testFeatures) {
        String bestLabel = null;
        double maxPosteriori = -1.0;

        for (String label : apriori.keySet()) {
            double currentPosteriori = getPosterioriForPrediction(testFeatures, label);

            if (currentPosteriori > maxPosteriori) {
                maxPosteriori = currentPosteriori;
                bestLabel = label;
            }
        }

        this.posteriori = maxPosteriori;
        return bestLabel;
    }

    private double getPosterioriForPrediction(String[] testFeatures, String targetLabel) {
        double posterioriResult = apriori.get(targetLabel);

        for (int i = 0; i < testFeatures.length; i++) {
            String featureValue = testFeatures[i];

            if (propabilitesMap.containsKey(featureValue)
                    && propabilitesMap.get(featureValue).containsKey(targetLabel)) {
                posterioriResult *= propabilitesMap.get(featureValue).get(targetLabel);
            } else {

                posterioriResult *= getConditionalProbability(featureValue, i, targetLabel);
            }
        }

        return posterioriResult;
    }

    private double getApriori(String targetLabel) {
        long count = 0;
        for (Dataset data : dataset) {
            if (data.label().equals(targetLabel)) {
                count++;
            }
        }
        return (double) count / dataset.size();
    }

    private double getConditionalProbability(String featureValue, int featureIndex, String targetLabel) {
        long countLabel = 0;
        long countFeatureAndLabel = 0;

        for (Dataset data : dataset) {
            if (data.label().equals(targetLabel)) {
                countLabel++;
                if (data.features()[featureIndex].equals(featureValue)) {
                    countFeatureAndLabel++;
                }
            }
        }

        if (!applySmoothingAll) {
            if (countLabel == 0)
                return 0.0;
            return (double) countFeatureAndLabel / countLabel;
        } else {
            long uniqueValuesCount = countUniqueFeaturesAt(featureIndex);
            return (double) (countFeatureAndLabel + 1) / (countLabel + uniqueValuesCount);
        }
    }

    private long countUniqueFeaturesAt(int featureIndex) {
        return dataset.stream()
                .map(data -> data.features()[featureIndex])
                .distinct()
                .count();
    }

}
