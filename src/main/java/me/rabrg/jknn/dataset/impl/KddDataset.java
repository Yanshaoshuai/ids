package me.rabrg.jknn.dataset.impl;

import me.rabrg.jknn.dataset.Dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KddDataset extends Dataset {
    @Override
    public Dataset load(InputStream... inputStream) throws IOException {
        //读标签属性
        final BufferedReader kddDataReader=new BufferedReader(new InputStreamReader(inputStream[0]));
        String str=null;
        int count=0;
        while ((str=kddDataReader.readLine())!=null){
            count++;
            String [] item=str.split(",");
            double[] features=new double[38];
            //读特征值
            for(int i=0;i<38;i++){
                    features[i]=Double.parseDouble(item[i]);
            }

            String label=item[38];
            addEntry(features,label);
        }

        return this;
    }
}
