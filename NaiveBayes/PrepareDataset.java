package NaiveBayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareDataset {
    private String path;

    public PrepareDataset(String path) {
        this.path = path;
    }

    public List<Dataset> readDataset() {
        List<Dataset> data = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(this.path))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                String[] features = new String[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    features[i] = parts[i];
                }

                String label = parts[parts.length - 1];

                data.add(new Dataset(features, label));
            }
        } catch (IOException e) {
            System.err.println("blad odczytu pliku: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("blad formatu danych" + e.getMessage());
        }

        return data;
    }

    public Map<String, List<Dataset>> trainTestSplit(List<Dataset> dataset, int trainsize) {
        List<Dataset> trainingData = new ArrayList<>();
        List<Dataset> testData = new ArrayList<>();
        for (Dataset row : dataset) {
            if (trainingData.size() < trainsize) {
                trainingData.add(row);
            } else {
                testData.add(row);
            }
        }

        Map<String, List<Dataset>> result = new HashMap<>();
        result.put("train", trainingData);
        result.put("test", testData);

        return result;
    }
}
