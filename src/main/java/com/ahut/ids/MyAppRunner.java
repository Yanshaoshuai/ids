package com.ahut.ids;

import com.ahut.ids.service.KnnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@Component
public class MyAppRunner implements ApplicationRunner {//服务器启动时执行
    @Autowired
    ServletContext application;
    @Autowired
    private KnnService knnService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService exec= Executors.newFixedThreadPool(1);
        Future<MinMax[]> future=exec.submit(()->
            Util.getMinMax(MyAppRunner.class.getResource("/File/kddcup.data_10_percent_corrected_NOString").getFile())
        );
        application.setAttribute("minMaxesFuture",future);
        knnService.init();
        knnService.kddCup99Analysis();
        knnService.systemAnalysis();
        System.err.println("准备工作完成");

    }
}
