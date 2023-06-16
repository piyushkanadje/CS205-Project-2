package Prj2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeatureSearchWrapper {
    private List<DataPoint> dataset;
    private NearestNeighborClassifier classifier;

    public FeatureSearchWrapper(List<DataPoint> dataset) {
        this.dataset = dataset;
        this.classifier = new NearestNeighborClassifier(dataset);
    }

    public void forwardSelection() {
        int numFeatures = dataset.get(0).getFeatures().length;
        int numInstances = dataset.size();
        System.out.println("This dataset has " + numFeatures + " features and " + numInstances + " instances.");
        List<Integer> selectedFeatures = new ArrayList<>();
        double accuracyAllFeatures = evaluateAccuracy(selectedFeatures);
        System.out.println("Executing nearest neighbor with all " + numFeatures + " features, using \"leaving-one-out\" evaluation, Accuracy =  " + accuracyAllFeatures + "%");
        System.out.println("Lets Begin the Search ...........................");
        List<Integer> bestFeatureSubset = new ArrayList<>();
        double bestAccuracy = 0.0;
        for (int i = 0; i < numFeatures; i++) {
            double currentBestAccuracy = 0.0;
            int currentBestFeature = -1;
            for (int j = 1; j <= numFeatures; j++) {
                if (!selectedFeatures.contains(j)) {
                    selectedFeatures.add(j);
                    double accuracy = evaluateAccuracy(selectedFeatures);
                    if (accuracy > currentBestAccuracy) {
                        currentBestAccuracy = accuracy;
                        currentBestFeature = j;
                    }
                    selectedFeatures.remove(Integer.valueOf(j));
                }
            }
            selectedFeatures.add(currentBestFeature);
            System.out.println("With feature(s) " + selectedFeatures + ", accuracy = " + currentBestAccuracy + "%");
            if (currentBestAccuracy > bestAccuracy) {
                bestAccuracy = currentBestAccuracy;
                bestFeatureSubset = new ArrayList<>(selectedFeatures);
            } 
        }
        System.out.println("Finished search!! The best feature subset is " + bestFeatureSubset + ", which has an accuracy of " + bestAccuracy + "%");
    }

    public void backwardElimination() {
        int numFeatures = dataset.get(0).getFeatures().length;
        int numInstances = dataset.size();

        System.out.println("This dataset has " + numFeatures + " features and " + numInstances + " instances.");

        List<Integer> selectedFeatures = new ArrayList<>();
        for (int i = 1; i <= numFeatures; i++) {
            selectedFeatures.add(i);
        }

        double accuracyAllFeatures = evaluateAccuracy(selectedFeatures);
        System.out.println("Running nearest neighbor with all " + numFeatures + " features, using \"leaving-one-out\" evaluation, Accuracy = " + accuracyAllFeatures + "%");

        System.out.println("Lets Begin the Search ...........................");

        List<Integer> bestFeatureSubset = new ArrayList<>(selectedFeatures);
        double bestAccuracy = 0.0;

        while (selectedFeatures.size() > 1) {
            double currentBestAccuracy = 0.0;
            int worstFeature = -1;

            for (int feature : selectedFeatures) {
                List<Integer> featuresToRemove = new ArrayList<>(selectedFeatures);
                featuresToRemove.remove(Integer.valueOf(feature));

                double accuracy = evaluateAccuracy(featuresToRemove);

                if (accuracy > currentBestAccuracy) {
                    currentBestAccuracy = accuracy;
                    worstFeature = feature;
                }
            }

            selectedFeatures.remove(Integer.valueOf(worstFeature));
            System.out.println("With feature(s) " + selectedFeatures + ", accuracy = " + currentBestAccuracy + "%");

            if (currentBestAccuracy > bestAccuracy) {
                bestAccuracy = currentBestAccuracy;
                bestFeatureSubset = new ArrayList<>(selectedFeatures);
            }
        }

        System.out.println("Finished search!! The best feature subset is " + bestFeatureSubset + ", which has an accuracy of " + bestAccuracy + "%");
    }


    private double evaluateAccuracy(List<Integer> selectedFeatures) {
        int numCorrect = 0;
        int totalInstances = dataset.size();
        int sampleSize = (int)(totalInstances * 0.1); 
        Random random = new Random();

        for (int i = 0; i < totalInstances; i++) {
            DataPoint dataPoint = dataset.get(i);
            List<DataPoint> trainingSet = new ArrayList<>(dataset.subList(0, i));
            trainingSet.addAll(dataset.subList(i + 1, totalInstances));

            List<DataPoint> sampledTrainingSet = new ArrayList<>();
            for (int j = 0; j < sampleSize; j++) {
                sampledTrainingSet.add(trainingSet.get(random.nextInt(trainingSet.size())));
            }

            NearestNeighborClassifier classifier = new NearestNeighborClassifier(sampledTrainingSet);
            int predictedClass = classifier.classify(dataPoint, selectedFeatures);

            if (predictedClass == dataPoint.getClassLabel()) {
                numCorrect++;
            }
        }

        return (double) numCorrect / totalInstances * 100.0;
    }

}

