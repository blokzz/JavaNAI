import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path = "iris.csv";
        PrepareDataset prepareDataset = new PrepareDataset(path);
        List<Observation> dataset = prepareDataset.readDataset();

        if (dataset.isEmpty()) {
            System.out.println("Blad: Nie udalo sie wczytac danych z iris.csv.");
            return;
        }

        Map<String, List<Observation>> splitData = prepareDataset.trainTestSplit(dataset);
        List<Observation> trainingData = splitData.get("train");
        List<Observation> testData = splitData.get("test");

        KNearestNeighbours knn = new KNearestNeighbours(3, trainingData);

        boolean running = true;
        while (running) {
            System.out.println("\n========= MENU  =========");
            System.out.println("1. Klasyfikuj nowa obserwacja (recznie)");
            System.out.println("2. Wczytaj plik testowy i oblicz Accuracy");
            System.out.println("3. Uruchom test porownawczy dla roznych k (1, 3, 5, 7, 9, 15)");
            System.out.println("4. Wyjscie");
            System.out.print("Wybierz opcje: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleManualEntry(scanner, knn);
                    break;
                case "2":
                    handleFileTest(scanner, knn);
                    break;
                case "3":
                    runComparisonTest(trainingData, testData);
                    break;
                case "4":
                    running = false;
                    System.out.println("Zamykanie programu...");
                    break;
                default:
                    System.out.println("Nieprawidlowa opcja.");
            }
        }
        scanner.close();
    }

    private static void runComparisonTest(List<Observation> trainingData, List<Observation> testData) {
        int[] kValues = { 1, 3, 5, 7, 9, 15, 20, 25 };
        System.out.println("\n--- TEST POROWNAWCZY DLA ROZNYCH WARTOSCI K ---");
        System.out.printf("%-10s | %-15s%n", "Wartosc K", "Accuracy");
        System.out.println("---------------------------");

        for (int k : kValues) {
            KNearestNeighbours tempKnn = new KNearestNeighbours(k, trainingData);
            List<String> trueLabels = new ArrayList<>();
            List<String> predictedLabels = new ArrayList<>();

            for (Observation obs : testData) {
                Observation result = tempKnn.classify(obs.getFeatures());
                trueLabels.add(obs.getLabel());
                predictedLabels.add(result.getLabel());
            }

            double accuracy = EvaluationMetrics.measureAccuracy(trueLabels, predictedLabels);
            System.out.printf("k = %-6d | %.2f%%%n", k, accuracy * 100);
        }
        System.out.println("---------------------------");
    }

    private static void handleManualEntry(Scanner scanner, KNearestNeighbours knn) {
        System.out.println("\nPodaj cechy oddzielone spacja (np. 5.1 3.5 1.4 0.2):");
        try {
            String input = scanner.nextLine();
            String[] parts = input.split("\\s+");
            double[] features = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                features[i] = Double.parseDouble(parts[i]);
            }

            Observation result = knn.classify(features);
            System.out.println(">>> Wynik: " + result.getLabel());
        } catch (Exception e) {
            System.out.println("Blad formatu danych");
        }
    }

    private static void handleFileTest(Scanner scanner, KNearestNeighbours knn) {
        System.out.print("\nPodaj sciezke do pliku testowego: ");
        String testPath = scanner.nextLine();
        PrepareDataset testLoader = new PrepareDataset(testPath);
        List<Observation> testDataset = testLoader.readDataset();

        if (testDataset.isEmpty())
            return;

        List<String> trueLabels = new ArrayList<>();
        List<String> predictedLabels = new ArrayList<>();

        for (Observation obs : testDataset) {
            Observation result = knn.classify(obs.getFeatures());
            trueLabels.add(obs.getLabel());
            predictedLabels.add(result.getLabel());
        }

        double accuracy = EvaluationMetrics.measureAccuracy(trueLabels, predictedLabels);
        System.out.printf("Accuracy dla %s: %.2f%%%n", testPath, accuracy * 100);
    }
}