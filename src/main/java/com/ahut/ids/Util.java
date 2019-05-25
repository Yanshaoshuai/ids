package com.ahut.ids;

import com.ahut.ids.model.Result;
import com.ahut.ids.model.Result_inner;
import com.ahut.ids.model.Source_Judge;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Util {
    /**
     * 分析并统计攻击类型 测试集，训练集
     */
    public static Map<String,Integer> anylisysHackType(InputStream inputStream) throws IOException {
        BufferedReader bfTrain=new BufferedReader(new InputStreamReader(inputStream));
        Map<String,Integer> map=new HashMap<>();
        String line="";
        while ((line=bfTrain.readLine())!=null){
            String label=line.substring(line.lastIndexOf(",")+1);
            if(label!=null&&label!=""){
                if(!map.containsKey(label)){
                    map.put(label,1);
                }else {
                    map.put(label,map.get(label)+1);
                }
            }
        }
        return map;
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        boolean isNumber=true;
        for(int i=0;i<str.length();i++){//判断字段是否包含字母
            char charJ=str.charAt(i);
            if((charJ<='z'&&charJ>='a')||(charJ<='Z'&&charJ>='A')){
                isNumber=false;
                break;
            }
        }
        return isNumber;
    }

    /**
     * 去除文件中的非数值型数据
     * @param filePath
     * @throws IOException
     */
    public static void prepareFile(String filePath) throws IOException {//去除非数值类型数据(不包括标签属性)
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String[] name=filePath.split("\\\\");
        File file=new File(name[name.length-1]+"_NOString");
        PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String line=null;
        System.out.println(bufferedReader.readLine());
        int count=0;
        while ((line=bufferedReader.readLine())!=null){
            String[] lines=line.split(",");
            line="";
            boolean isStr=false;
            for(int i=0;i<lines.length;i++){
                if(i!=lines.length-1)
                    isStr=!Util.isNumber(lines[i]);
                if(isStr==true)//如果包含字母进行下一个字段的操作
                {
                    isStr=false;
                    continue;
                }
                line=line+lines[i]+",";
            }
            if(line!=null){
                line=line.substring(0,line.length()-1);
                printWriter.println(line);
                System.out.println(count++);
            }
        }
        printWriter.close();
        bufferedReader.close();
    }

    /**
     * 预测数据预处理，去除非数值型数据
     * @param str
     * @return
     */
    public static String prepareStr(String str){
        String[]strs=str.split(",");
        String result="";
        for(String string:strs){
            if(isNumber(string)){
                result=result+string+",";
            }
        }
        return result.substring(0,result.length()-1);
    }

    /**
     * 把字符串归一化
     * @param str
     * @return
     * @throws IOException
     */
    public static String normalizationStr(String str, MinMax[] minMax) throws IOException {//标准化字符串
        str=prepareStr(str);
        String[] strs=str.split(",");
        str="";
        for(int i=0;i<strs.length;i++){
            if(isNumber(strs[i])){
                double thisNumber=Double.parseDouble(strs[i]);
                if(thisNumber==minMax[i].getMax()){
                    str=str+"1"+",";
                    continue;
                }else if (thisNumber==minMax[i].getMin()){
                    str=str+"0"+",";
                    continue;
                }
                double normalNumber=(thisNumber-minMax[i].getMin())/(minMax[i].getMax()-minMax[i].getMin());
                str=str+String.valueOf(normalNumber)+",";
            }
        }
        return str.substring(0,str.length()-1);
    }

    /**
     *找到所有列的最大最小值
     * @param filePath
     * @return
     * @throws IOException
     */
    public static MinMax[] getMinMax(String filePath) throws IOException {

        String str=null;
        List<String> column;
        MinMax[] minMaxes=new MinMax[38];
        for(int i=0;i<minMaxes.length;i++){
            minMaxes[i]=new MinMax();
        }
        for(int i=0;i<38;i++){//38列
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            column=new LinkedList<>();
            double max=0;
            double min=0;
            while ((str=bufferedReader.readLine())!=null){//读所有行，找到第i列，把该行i列放进链表
                String []strs=str.split(",");
                column.add(strs[i]);
            }
            for (String s:
                 column) {
                if(isNumber(s)){
                    double thisNumber=Double.parseDouble(s);
                    if(thisNumber<min){
                        min=thisNumber;
                    }else if(thisNumber>max){
                        max=thisNumber;
                    }
                }
            }
            minMaxes[i].setMax(max);
            minMaxes[i].setMin(min);

        }

        return minMaxes;
    }

    /**
     *把文件中数字归一化处理
     * @param filePath
     * @throws IOException
     */
    public static void normalizationFile(String filePath) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath+"xx")));
        MinMax[] minMaxes=getMinMax(filePath);
        String str=null;
        while ((str=bufferedReader.readLine())!=null){
            String[]strs=str.split(",");
            str="";
            for(int i=0;i<strs.length;i++){
                double normalNumber=0;
                if(isNumber(strs[i])){
                    double thisNumber=Double.parseDouble(strs[i]);
                    if(thisNumber==minMaxes[i].getMin()){
                        normalNumber=0;
                    }else if(thisNumber==minMaxes[i].getMax()){
                        normalNumber=1;
                    }else {
                        normalNumber=(thisNumber-minMaxes[i].getMin())/(minMaxes[i].getMax()-minMaxes[i].getMin());
                    }
                    str=str+String.valueOf(normalNumber)+",";
                }else
                    str=str+strs[i]+",";
            }
            printWriter.println(str.substring(0,str.length()-1));
        }
        printWriter.close();
        bufferedReader.close();
    }

    /**
     * 封装result工具类
     * @param list
     * @param result
     */
    public static void computeResult(List<Source_Judge> list, Result result) {
        Map<String,Result_inner> map=result.getResult_innerMap();
        for(Source_Judge source_judge:list){
            if(source_judge.getSourceLabel().equals(source_judge.getJudgeLabel())){
                //如果判断正确，该分类Tp+1
                if(!map.containsKey(source_judge.getSourceLabel())){
                    Result_inner result_inner=new Result_inner();
                    map.put(source_judge.getSourceLabel(),result_inner);
                }
                Result_inner result_inner=map.get(source_judge.getSourceLabel());
                result_inner.setTp(result_inner.getTp()+1);
            }else {
                //如果判断错误
                if(!map.containsKey(source_judge.getSourceLabel())){
                    Result_inner result_inner=new Result_inner();
                    map.put(source_judge.getSourceLabel(),result_inner);
                }
                Result_inner result_inner=map.get(source_judge.getSourceLabel());
                result_inner.setFn(result_inner.getFn()+1);//本分类的Fn+1(正类判为负类)

                if(!map.containsKey(source_judge.getJudgeLabel())){
                    Result_inner result_inner1=new Result_inner();
                    map.put(source_judge.getJudgeLabel(),result_inner1);
                }
                Result_inner result_inner1=map.get(source_judge.getJudgeLabel());
                result_inner1.setFp(result_inner.getFp()+1);//判断错误的分类Fp+1(负类判断为正类)

            }
        }

        int tp_all=0;

        for(Map.Entry<String,Result_inner> entry:map.entrySet()){
            String classify=entry.getKey();
            Result_inner result_inner=entry.getValue();
            tp_all+=result_inner.getTp();
            if((result_inner.getTp()+result_inner.getFp())!=0){
                result_inner.setPrecision(result_inner.getTp()/Double.valueOf(result_inner.getTp()+result_inner.getFp()));
            }
            if((result_inner.getTp()+result_inner.getFn())!=0){
                result_inner.setRecall(result_inner.getTp()/Double.valueOf(result_inner.getTp()+result_inner.getFn()));
            }
        }
        result.setAccuracy(tp_all/Double.valueOf(list.size()));
        result.setRecall(map.values().stream().filter(new Predicate<Result_inner>() {
            @Override
            public boolean test(Result_inner result_inner) {
                return !(result_inner.getRecall()==0);
            }
        }).mapToDouble(Result_inner::getRecall).average().getAsDouble());
        result.setPrecision(map.values().stream().filter(new Predicate<Result_inner>() {
            @Override
            public boolean test(Result_inner result_inner) {
                return !(result_inner.getPrecision()==0);
            }
        }).mapToDouble(Result_inner::getPrecision).average().getAsDouble());
    }



}
