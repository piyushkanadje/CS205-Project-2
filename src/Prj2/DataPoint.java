package Prj2;

public class DataPoint {
    private int classLabel;
    private double[] features;

    public DataPoint(int classLabel, double[] features) {
        this.classLabel = classLabel;
        this.features = features;
    }

    public int getClassLabel() {
        return classLabel;
    }

    public double[] getFeatures() {
        return features;
    }
}
