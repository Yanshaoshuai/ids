package com.ahut.ids.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface KnnService {
   String judge(String testStr,Integer k) throws IOException, ExecutionException, InterruptedException;
   File mark(File file) throws IOException, ExecutionException, InterruptedException;//返回一个打标后的文件
   boolean repalceTrainFile(String trainFileName) throws IOException;
   boolean init() throws IOException;
   void kddCup99Analysis() throws IOException;
   public void systemAnalysis() throws IOException, ExecutionException, InterruptedException;
}
