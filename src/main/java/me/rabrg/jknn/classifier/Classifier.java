package me.rabrg.jknn.classifier;

import me.rabrg.jknn.dataset.Dataset;
import me.rabrg.jknn.distance.Distance;
import me.rabrg.jknn.distance.impl.EuclideanDistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Classifier {

    private final Distance distance;

    public Classifier() {
        this(new EuclideanDistance());
    }

    public Classifier(final Distance distance) {
        this.distance = distance;
    }

    public final void fit(final Dataset dataset) {
        for (int i = 0; i < dataset.getFeaturesList().size(); i++)
            fit(dataset.getFeaturesList().get(i), dataset.getLabelsList().get(i));
    }

    public abstract void fit(final double[] features, final String label);

    public final double accuracy(final Dataset dataset, final int k) {//获取准确度
        double correct = 0;
        for (int i = 0; i < dataset.getFeaturesList().size(); i++){//循环测试集中的特征值，逐个测试是否匹配
            if (classify(dataset.getFeaturesList().get(i), k).equals(dataset.getLabelsList().get(i)))
                correct++;
            System.out.println("已遍历:"+i+"  正确:"+correct);
        }

        return correct / dataset.getFeaturesList().size();
    }

    public abstract String classify(double[] features, int k);

    protected Distance getDistance() {
        return distance;
    }

    protected <T> T mode(final List<T> list) {//找到出现频率最多的label
        final Map<T, Integer> map = new HashMap<>();
        int maxFreq = 0;
        T maxFreqLabel = null;
        for (final T s : list) {
            final int freq = map.getOrDefault(s, 0) + 1;
            if (freq > maxFreq) {
                maxFreq = freq;
                maxFreqLabel = s;
            }
            map.put(s, freq);
        }
        return maxFreqLabel;
    }
}
