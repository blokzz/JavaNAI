package SingleLayer;

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

    public List<Observation> readDataset(String... languages) {
        List<Observation> data = new ArrayList<>();
        List<String> languagesList = new ArrayList<>(List.of(languages));
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(this.path))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                int lastCommaIndex = line.lastIndexOf(',');
                if (lastCommaIndex <= 0)
                    continue;

                String label = line.substring(lastCommaIndex + 1).trim();
                String text = line.substring(0, lastCommaIndex);

                if (languagesList.contains(label.toLowerCase())) {
                    Map<String, Integer> letters = new HashMap<>();
                    for (int i = 0; i < 26; i++) {
                        letters.put(String.valueOf((char) ('a' + i)), 0);
                    }

                    String redactedLine = text.toLowerCase().trim().replace(" ", "");
                    for (char c : redactedLine.toCharArray()) {
                        if (c >= 'a' && c <= 'z') {
                            letters.put(String.valueOf(c), letters.get(String.valueOf(c)) + 1);
                        }
                    }
                    double[] features = new double[26];
                    for (int i = 0; i < 26; i++) {
                        features[i] = (double) letters.get(String.valueOf((char) ('a' + i))) / redactedLine.length();
                    }
                    data.add(new Observation(features, label));
                }

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

            int trainSize = (int) Math.round(group.size() * 0.7);

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
