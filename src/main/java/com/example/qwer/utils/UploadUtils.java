package com.example.qwer.utils;


import com.example.qwer.config.QiniuCloudConfig;
import com.example.qwer.pojo.Log;
import com.example.qwer.pojo.MessageSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UploadUtils {

    @Autowired
    private QiniuCloudConfig qiniuCloudConfig;

    @Autowired
    private AliMessageUtils aliMessageUtils;
    @GetMapping("upload")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> list= new ArrayList<>();
        list.add("2021090416:14:15微信图片_20210904151010.png");
        list.add("2021090416:15:102021.8.1（2021.8.1-2021.8.31）月例会工作内容考核汇总表及下月计划-闫一瑞.xls");
        aliMessageUtils.sendMessage(MessageSign.LOGIN_SIGN,"18635486031");
        qiniuCloudConfig.downloadFiles(request,response,list);
    }

    @Log(module = "订单监控",description = "订单列表")
    @PostMapping("excelImport")
    public void excelImport(@RequestParam("file") MultipartFile file) throws IOException {
       List list=ReadExcelTools.readExcel(file);
       for(int i=0;i<list.size();i++){
           System.out.println(list.get(i).toString());
       }
    }
    @PostMapping("excelExport")
    public void excelExport() throws IOException {
        ExportExcelTools.export();
    }
}
