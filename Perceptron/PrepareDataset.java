package Perceptron;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PrepareDataset {
    private String path;

    public PrepareDataset(String path) {
        this.path = path;
    }

    public List<Observation> readDataset() {
        List<Observation> data = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(this.path))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String label = parts[parts.length - 1];
                if (label.toLowerCase().equals("virginica")) {
                    continue;
                }
                double[] features = new double[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    features[i] = Double.parseDouble(parts[i]);
                }
                double labelValue = label.toLowerCase().equals("setosa") ? 1 : 0;
                data.add(new Observation(features, labelValue));
            }
        } catch (IOException e) {
            System.err.println("blad odczytu pliku: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("blad formatu danych" + e.getMessage());
        }

        return data;

    }

    public Map<String, List<Observation>> trainTestSplit(List<Observation> dataset) {
        Map<String, List<Observation>> groupedData = new HashMap<>();
        for (Observation obs : dataset) {
            groupedData.computeIfAbsent(String.valueOf(obs.getLabel()), k -> new ArrayList<>()).add(obs);
        }

        List<Observation> trainingData = new ArrayList<>();
        List<Observation> testData = new ArrayList<>();

        Random random = new Random();
        for (List<Observation> group : groupedData.values()) {
            Collections.shuffle(group, random);

            int trainSize = (int) Math.round(group.size() * 0.3);

            for (int i = 0; i < group.size(); i++) {
                if (i < trainSize) {
                    trainingData.add(group.get(i));
                } else {
                    testData.add(group.get(i));
                }
            }
        }

        Collections.shuffle(trainingData, random);
        Collections.shuffle(testData, random);

        Map<String, List<Observation>> result = new HashMap<>();
        result.put("train", trainingData);
        result.put("test", testData);

        return result;
    }

}
