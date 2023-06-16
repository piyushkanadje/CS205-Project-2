package Prj2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type in the path of the file to test: ");
        String filename = scanner.nextLine();
        System.out.println("Choose the Algorithm.\n1) Forward Selection\n2) Backward Elimination");
        int algorithmChoice = scanner.nextInt();

        List<DataPoint> dataset = loadDataset(filename);

        FeatureSearchWrapper featureSearchWrapper = new FeatureSearchWrapper(dataset);

        if (algorithmChoice == 1) {
        	long startTime = System.currentTimeMillis();
            featureSearchWrapper.forwardSelection();
            long endTime = System.currentTimeMillis();
            long RunTime = endTime - startTime;
            System.out.println("Total Run Time = " + RunTime);
        } else if (algorithmChoice == 2) {
        	long startTime = System.currentTimeMillis();
            featureSearchWrapper.backwardElimination();
            long endTime = System.currentTimeMillis();
            long RunTime = endTime - startTime;
            System.out.println("Total Run Time = " + RunTime);
        } else {
            System.out.println("Invalid algorithm choice. Please choose 1 or 2.");
        }
    }

    private static List<DataPoint> loadDataset(String filename) {
        List<DataPoint> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                double classLabel = Double.parseDouble(values[0]);
                double[] features = new double[values.length - 1];
                for (int i = 1; i < values.length; i++) {
                    features[i - 1] = Double.parseDouble(values[i].replace("e+", "e"));
                }
                dataset.add(new DataPoint((int) classLabel, features));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }
}

