package me.rabrg.jknn.classifier.impl;

import me.rabrg.jknn.classifier.Classifier;
import me.rabrg.jknn.distance.Distance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BruteForceClassifier extends Classifier {

    private final List<double[]> trainingFeatures = new ArrayList<>();
    private final List<String> trainingLabels = new ArrayList<>();

    public BruteForceClassifier(Distance distance){
        super(distance);
    }
    @Override
    public void fit(final double[] features, final String label) {
        trainingFeatures.add(features);
        trainingLabels.add(label);
    }

    @Override
    public String classify(final double[] features, final int k) {//返回预测标签
        final Map<Double, List<String>> distanceLabelMap = distanceLabelMap(features);//获取测试点特征值所有距离标签的map
        final List<String> kNearestNeighbors = kNearestNeighbors(distanceLabelMap, k);//找到最近的k个点的标签集合
        return mode(kNearestNeighbors);
    }
    /**
     *     获取距离标签集合
     */
    private Map<Double, List<String>> distanceLabelMap(final double[] features) {
        final Map<Double, List<String>> map = new TreeMap<>();
        for (int i = 0; i < trainingLabels.size(); i++) {
            final double distance = getDistance().getDistance(features, trainingFeatures.get(i));
            final List<String> labels = map.getOrDefault(distance, new ArrayList<>());
            labels.add(trainingLabels.get(i));
            map.put(distance, labels);
        }
        return map;
    }
    //找到距离最近的k个点的标签值
    private static List<String> kNearestNeighbors(final Map<Double, List<String>> map, final int k) {
        final List<String> list = new ArrayList<>();
        for (final List<String> labels : map.values()){
            if (list.addAll(labels) && list.size() > k)
                break;
        }

        return list;
    }
}
