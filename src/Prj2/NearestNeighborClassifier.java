package Prj2;

import java.util.Collections;
import java.util.List;

public class NearestNeighborClassifier {
    private List<DataPoint> trainingSet;

    public NearestNeighborClassifier(List<DataPoint> trainingSet) {
        this.trainingSet = trainingSet;
    }

    public int classify(DataPoint queryPoint, List<Integer> selectedFeatures) {
        double minDistance = Double.MAX_VALUE;
        int nearestClass = -1;
        for (DataPoint dataPoint : trainingSet) {
            double distance = calculateDistance(dataPoint, queryPoint, selectedFeatures, minDistance);
            if (distance < minDistance) {
                minDistance = distance;
                nearestClass = dataPoint.getClassLabel();
            }
        }
        return nearestClass;
    }


    	private double calculateDistance(DataPoint point1, DataPoint point2, List<Integer> selectedFeatures, double minDistance) {
    	    double[] features1 = point1.getFeatures();
    	    double[] features2 = point2.getFeatures();
    	    if (features1.length != features2.length) {
    	        throw new IllegalArgumentException("Mismatch in feature lengths");
    	    }
    	    double sum = 0.0;
    	    for (int featureIndex : selectedFeatures) {
    	        double diff = features1[featureIndex - 1] - features2[featureIndex - 1];
    	        sum += diff * diff;
    	        if (sum > minDistance) {
    	            return Double.MAX_VALUE;  
    	        }
    	    }
    	    return Math.sqrt(sum);
    	}

}