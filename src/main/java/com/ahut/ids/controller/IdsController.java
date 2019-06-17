package com.ahut.ids.controller;

import com.ahut.ids.model.KDDData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ahut.ids.service.KnnService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Controller
public class IdsController {
    @Autowired
    ServletContext application;
    @Autowired
    KnnService knnService;
    private static final Logger logger = LoggerFactory.getLogger(IdsController.class);
    @GetMapping("/index")
    public String view(Model model) throws IOException {
        List<KDDData> kddDataList=new LinkedList<>();
        int i=0;
        BufferedReader bf=new BufferedReader(new FileReader("C:\\Users\\yan\\Desktop\\aa\\ids\\ids\\src\\main\\resources\\File\\corrected"));
        String line="";
        while ((i++<=13)&&((line=bf.readLine())!=null)){
                String []kddDataArray=line.split(",");
                KDDData kddData=new KDDData();
                kddData.setDuration(kddDataArray[0]);
                kddData.setProtocol_type(kddDataArray[1]);
                kddData.setService(kddDataArray[2]);
                kddData.setFlag(kddDataArray[3]);
                kddData.setSrc_bytes(kddDataArray[4]);
                kddData.setDst_bytes(kddDataArray[5]);
                kddData.setLand(kddDataArray[6]);
                kddData.setWrong_fragment(kddDataArray[7]);
                kddData.setUrgent(kddDataArray[8]);
                kddData.setHot(kddDataArray[9]);
                kddData.setNum_failed_logins(kddDataArray[10]);
                kddData.setLogged_in(kddDataArray[11]);
                kddData.setNum_compromised(kddDataArray[12]);
                kddData.setRoot_shell(kddDataArray[13]);
                kddData.setSu_attempted(kddDataArray[14]);
                kddData.setNum_root(kddDataArray[15]);
                kddData.setNum_file_creations(kddDataArray[16]);
                kddData.setNum_shells(kddDataArray[17]);
                kddData.setNum_access_files(kddDataArray[18]);
                kddData.setNum_outbound_cmds(kddDataArray[19]);
                kddData.setIs_hot_login(kddDataArray[20]);
                kddData.setIs_guest_login(kddDataArray[21]);
                kddData.setCount(kddDataArray[22]);
                kddData.setSrv_count(kddDataArray[23]);
                kddData.setSerror_rate(kddDataArray[24]);
                kddData.setSrv_serror_rate(kddDataArray[25]);
                kddData.setRerror_rate(kddDataArray[26]);
                kddData.setSrv_rerror_rate(kddDataArray[27]);
                kddData.setSame_srv_rate(kddDataArray[28]);
                kddData.setDiff_srv_rate(kddDataArray[29]);
                kddData.setSrv_diff_host_rate(kddDataArray[30]);
                kddData.setDst_host_count(kddDataArray[31]);
                kddData.setDst_host_srv_count(kddDataArray[32]);
                kddData.setDst_host_same_srv_rate(kddDataArray[33]);
                kddData.setDst_host_diff_srv_rate(kddDataArray[34]);
                kddData.setDst_host_same_src_port_rate(kddDataArray[35]);
                kddData.setDst_host_srv_diff_host_rate(kddDataArray[36]);
                kddData.setDst_host_serror_rate(kddDataArray[37]);
                kddData.setDst_host_srv_serror_rate(kddDataArray[38]);
                kddData.setDst_host_rerror_rate(kddDataArray[39]);
                kddData.setDst_host_srv_rerror_rate(kddDataArray[40]);
                kddData.setLabel(kddDataArray[41]);
                kddDataList.add(kddData);
        }
        application.setAttribute("kddDataList",kddDataList);
        return "index";
    }
    @GetMapping("/ids")
    public String ids(Model model) throws IOException {
//        knnService.init();

//        InputStream inputStream=this.getClass().getResourceAsStream("/File/record");
//        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
//        String str=null;
//        Map<String,String> k_accruncy_map=new TreeMap<>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return Integer.compare(Integer.valueOf(o1),Integer.valueOf(o2));
//            }
//        });
//
//        while ((str=bufferedReader.readLine())!=null){
//            String []strs=str.split(",");
//            k_accruncy_map.put(strs[0],strs[1]);
//        }
//        model.addAttribute("k_accruncy_map",k_accruncy_map);
//        Optional<String> max = k_accruncy_map.values().stream().max(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return Double.compare(Double.valueOf(o1),Double.valueOf(o2));
//            }
//        });
//        String niceK=null;
//        for (Map.Entry<String,String> entry:
//             k_accruncy_map.entrySet()) {
//            if(entry.getValue().equals(max.get())){
//                niceK=entry.getValue();
//                break;
//            }
//        }
//        model.addAttribute("nice",max.get());
//        model.addAttribute("niceK",niceK);

        return "ids/ids";
    }
    @PutMapping("/predict")
    @ResponseBody public String predict(@RequestParam String input) throws IOException, ExecutionException, InterruptedException {
        System.err.println(input);
//        input="0,icmp,ecr_i,SF,1032,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,511,511,0.00,0.00,0.00,0.00,1.00,0.00,0.00,255,255,1.00,0.00,1.00,0.00,0.00,0.00,0.00,0.00";
        return knnService.judge(input,9);
    }
    @PostMapping("/reset")
    @ResponseBody public  boolean reset() throws IOException {
        return knnService.init();
    }

    //文件上传相关代码
    @RequestMapping("/mark")
    @ResponseBody
    public String upload(@RequestParam("test") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        if(fileName.lastIndexOf(".")!=-1){
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传的后缀名为：" + suffixName);
        }else {
            logger.info("上传的后缀名为空"  );
        }
        // 文件上传后的路径
        String filePath = "D://test//";
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
          //"上传成功"
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File markedFile=knnService.mark(dest);

        return markedFile.getName();
    }
    @RequestMapping("/download")
    public void downloadMark(@RequestParam("fileName") String fileName,HttpServletRequest request,HttpServletResponse response){
        File markedFile=new File(fileName);
        if (markedFile!= null&&markedFile.getName()!=null) {

            if (markedFile.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" +  markedFile.getName());// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(markedFile);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    @RequestMapping("KddCup99")
    public String Kddcup99(Model model){
        return "KddCup99/KddCup99";
    }
    @RequestMapping("system")
    public String system(Model model){
        return "system/system";
    }
}
