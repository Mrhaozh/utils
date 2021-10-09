package com.example.qwer.utils;

import com.example.qwer.QwerApplication;
import com.example.qwer.config.QiniuCloudConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QwerApplication.class)
public class UploadUtilsTest {

    @Autowired
    private QiniuCloudConfig qiniuCloudConfig;
    @Test
    public void tes() {
        System.out.println(qiniuCloudConfig.getAccessKey());
    }
}