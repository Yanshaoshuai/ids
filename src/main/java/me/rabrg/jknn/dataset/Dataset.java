package me.rabrg.jknn.dataset;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Dataset {

    private final List<double[]> featuresList = new ArrayList<>();
    private final List<String> labelsList = new ArrayList<>();

    public abstract Dataset load(final InputStream... inputStream) throws IOException;

    protected final void addEntry(final double[] features, final String label) {
        featuresList.add(features);
        labelsList.add(label);
    }

    public List<double[]> getFeaturesList() {
        return featuresList;
    }

    public List<String> getLabelsList() {
        return labelsList;
    }
}
