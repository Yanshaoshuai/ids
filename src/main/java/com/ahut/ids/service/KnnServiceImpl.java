package com.ahut.ids.service;

import com.ahut.ids.MinMax;
import com.ahut.ids.Util;
import com.ahut.ids.model.Result;
import com.ahut.ids.model.Source_Judge;
import me.rabrg.jknn.classifier.Classifier;
import me.rabrg.jknn.classifier.impl.BruteForceClassifier;
import me.rabrg.jknn.dataset.Dataset;
import me.rabrg.jknn.dataset.impl.KddDataset;
import me.rabrg.jknn.distance.impl.EuclideanDistance;
import com.ahut.ids.model.KNN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class KnnServiceImpl implements KnnService{
    private KNN knn;
    private Dataset trainDataset ;
    private Dataset testDataset ;
    private Classifier classifier;
    @Autowired
    ServletContext application;
    @Override
    public String judge(String predictStr,Integer k) throws IOException, ExecutionException, InterruptedException {
        predictStr=Util.prepareStr(predictStr);
        predictStr=Util.normalizationStr(predictStr,(MinMax[])(((Future<MinMax[]>) application.getAttribute("minMaxesFuture")).get()));
        String[]predictStrs=predictStr.split(",");
        double []features=new double[predictStrs.length];
        for (int i=0;i<predictStrs.length;i++) {
            features[i]=Double.parseDouble(predictStrs[i]);
        }

        String predictedLabel="";
        predictedLabel= classifier.classify(features, k);//预测属于哪个分类
        System.out.println(predictedLabel);


        return predictedLabel;
    }


    @Override
    public File mark(File file) throws IOException, ExecutionException, InterruptedException {
        final BufferedReader kddDataReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str = null;
        int count = 0;
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file.getName()+"_marked")),"UTF-8"));
        while ((str = kddDataReader.readLine()) != null) {
          str=str+","+judge(str,9);
          out.println(str);
        }
        out.close();
        kddDataReader.close();
        return new File(file.getName()+"_marked");
    }

    @Override
    public boolean repalceTrainFile(String trainFileName) throws IOException {
        //设置默认系统参数
        knn.setK(9);
        knn.setTrainFileName("kddcup.data_10_percent_corrected");
        knn.setTestFileName("corrected");
        knn.setAccuracy(0.99);
        knn.setPerfectK(9);

        InputStream trainStream=new FileInputStream(trainFileName);
        trainDataset = new KddDataset().load(trainStream);
        classifier.fit(trainDataset);

        return true;
    }

    @Override
    public boolean init() throws IOException {
        knn=new KNN();
        //设置默认系统参数
        knn.setK(9);
        knn.setTrainFileName("kddcup.data_10_percent_correctedxx");
        knn.setTestFileName("correctedxx");
        knn.setAccuracy(0.99);
        knn.setPerfectK(9);
        InputStream trainStream=this.getClass().getResourceAsStream("/File/kddcup.data_10_percent_corrected_NOStringxx");

        InputStream testStream=this.getClass().getResourceAsStream("/File/correctedxx");
        // Load the train and test datasets
        trainDataset = new KddDataset().load(trainStream);
        testDataset = new KddDataset().load(testStream);
        classifier = new BruteForceClassifier(new EuclideanDistance());
        classifier.fit(trainDataset);
        application.setAttribute("trainDataset",trainDataset);
        application.setAttribute("testDataset",testDataset);
        return true;
    }

    public void kddCup99Analysis() throws IOException {
        InputStream trainStream=this.getClass().getResourceAsStream("/File/kddcup.data_10_percent_corrected_NOStringxx");
        InputStream testStream=this.getClass().getResourceAsStream("/File/correctedxx");
        Map<String, Integer> trainLabelCountMap = Util.anylisysHackType(trainStream);
        Map<String, Integer> testLabelCountMap = Util.anylisysHackType(testStream);
        Collection<String> trainSet=trainLabelCountMap.keySet();
        Collection<String> testSet=testLabelCountMap.keySet();
        for(String labelTrain:trainSet){
            if(!testSet.contains(labelTrain)){
                testLabelCountMap.put(labelTrain,0);
            }
        }
        for(String labelTest:testSet){
            if(!trainSet.contains(labelTest)){
                trainLabelCountMap.put(labelTest,0);
            }
        }
        String xAxisData="";
        String str=trainLabelCountMap.keySet().toString();
        xAxisData=str.substring(1,str.length()-1);
        List<Integer>trainDataCounts= new ArrayList<>(trainLabelCountMap.values());
        List<Integer>testDataCouts= new ArrayList<>(testLabelCountMap.values());
        int sum1=0;
        for(Integer i:trainDataCounts){
            sum1+=i;
        }
        int sum2=0;
        for(Integer i:testDataCouts){
            sum2+=i;
        }
        application.setAttribute("trainSum",sum1);
        application.setAttribute("testSum",sum2);
        application.setAttribute("xAxisData",xAxisData);
        application.setAttribute("trainDataCounts",trainDataCounts);
        application.setAttribute("testDataCouts",testDataCouts);

    }

    /**
     * 分析系统参数
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public void systemAnalysis() throws IOException, ExecutionException, InterruptedException {
        InputStream in=this.getClass().getResourceAsStream("/File/output");
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
        Map<Integer,Result> resultMap=new TreeMap<>();
        List<Source_Judge> list=null;
        String line="";
        Result result=null;
        int k;
        while((line=bufferedReader.readLine())!=null){
            if(line.contains("k=")){
                    if(line.contains("开始")){
                        result=new Result();
                        result.setResult_innerMap(new HashMap<>());
                        list= new LinkedList();
                         k=Integer.valueOf(line.substring(line.indexOf("=")+1,line.indexOf('开')));
                        resultMap.put(k,result);
                }else {
                    Util.computeResult(list,result);
                    continue;
                }
            }
            else  {
                String str1=line.substring(line.indexOf("为")+1);
                String sourceLabel=str1.substring(str1.indexOf('<')+1,str1.indexOf('>'));
                String str2=str1.substring(str1.indexOf("为")+1);
                String judgeLabel=str2.substring(str2.indexOf('<')+1,str2.indexOf('>'));
                Source_Judge source_judge=new Source_Judge();
                source_judge.setSourceLabel(sourceLabel);
                source_judge.setJudgeLabel(judgeLabel);
                list.add(source_judge);
            }
        }
        String xAxisData="";
        String yAxisData="召回率,准确率,精确率";
        List<Double> recallList=new ArrayList<>();
        List<Double> precisionList=new ArrayList<>();
        List<Double> accuracyList=new ArrayList<>();
        String strX="";
        for(Map.Entry<Integer,Result> entry:resultMap.entrySet()){
            strX=strX+","+entry.getKey();
            recallList.add(entry.getValue().getRecall());
            precisionList.add(entry.getValue().getPrecision());
            accuracyList.add(entry.getValue().getAccuracy());
        }
        xAxisData=strX.substring(1);
        application.setAttribute("xAxisData_sys",xAxisData);
        application.setAttribute("yAxisData_sys",yAxisData);
        application.setAttribute("recallList_sys",recallList);
        application.setAttribute("precisionList_sys",accuracyList);
        application.setAttribute("accuracyList_sys",precisionList);
    }

    public static void main(String[] args){
        Dataset trainDataset ;
        Classifier classifier;
        File file=new File("output");
        try{
        MinMax[] minMax = Util.getMinMax(KnnServiceImpl.class.getResource("/File/kddcup.data_10_percent_corrected_NOString").getFile());
        PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for(int k=9;k<=20;k++){
            printWriter.println("k="+k+"开始");
            InputStream trainStream=new FileInputStream("/File/kddcup.data_10_percent_corrected_NOStringxx");
            InputStream testStream=new FileInputStream("/File/corrected");
            trainDataset = new KddDataset().load(trainStream);
            classifier = new BruteForceClassifier(new EuclideanDistance());
            classifier.fit(trainDataset);
            BufferedReader testReader=new BufferedReader(new InputStreamReader(testStream));
            String line="";
            int i=0,l=0;
            while ((line=testReader.readLine())!=null){
                String sourceLabel=line.substring(line.lastIndexOf(",")+1);
                String judgeLabel="";
                judgeLabel=Util.prepareStr(line);
                judgeLabel=Util.normalizationStr(judgeLabel,minMax);
                String[]predictStrs=judgeLabel.split(",");
                double []features=new double[predictStrs.length];
                for (int j=0;j<predictStrs.length;j++) {
                    features[j]=Double.parseDouble(predictStrs[j]);
                }
                judgeLabel= classifier.classify(features, k);//预测属于哪个分类
                if(sourceLabel.equals(judgeLabel)){
                    printWriter.println("已检索<"+ ++i +">正确<"+ ++l+">原标签为<"+sourceLabel+">判断为<"+judgeLabel+">");
                }else {
                    printWriter.println("已检索<"+ ++i +">正确<"+ l+">原标签为<"+sourceLabel+">判断为<"+judgeLabel+">");
                }
            }
            printWriter.println("k="+k+"结束");
        }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
