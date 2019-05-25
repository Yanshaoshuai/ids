package me.rabrg.jknn.dataset.impl;

import me.rabrg.jknn.dataset.Dataset;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MnistDataset extends Dataset {

    private static final int LABELS_MAGIC_NUMBER = 2049;
    private static final int FEATURES_MAGIC_NUMBER = 2051;

    @Override
    public Dataset load(final InputStream... inputStream) throws IOException {
        // Read the labels
        final DataInputStream labelsStream = new DataInputStream(inputStream[0]);

        final int labelsMagic = labelsStream.readInt();
        if (labelsMagic != LABELS_MAGIC_NUMBER)//读label魔数
            throw new IllegalStateException("Labels magic number not valid!");

        final int items = labelsStream.readInt();//读数据元素组数
        final int[] labels = new int[items];
        for (int i = 0; i < items; i++)//读label值
            labels[i] = labelsStream.readUnsignedByte();

        // Read the features
        final DataInputStream featuresStream = new DataInputStream(inputStream[1]);

        final int featuresMagic = featuresStream.readInt();//读特征值魔数
        if (featuresMagic != FEATURES_MAGIC_NUMBER)
            throw new IllegalStateException("Features magic number not valid!");

        if (items != featuresStream.readInt())
            throw new IllegalStateException("Labels and features items count mismatch!");

        final int rows = featuresStream.readInt();
        final int cols = featuresStream.readInt();
        final double[][] features = new double[items][rows * cols];
        for (int i = 0; i < items; i++)
            for (int row = 0; row < rows; row++)
                for (int col = 0; col < cols; col++){
                    System.out.println("the application is running");//读特征值的值
                    features[i][row * rows + col] = featuresStream.readUnsignedByte() / 255d;
                }

        for (int i = 0; i < items; i++){//加入特征值
            System.out.println("the application is running");
            addEntry(features[i], String.valueOf(labels[i]));
        }
        return this;
    }
}
