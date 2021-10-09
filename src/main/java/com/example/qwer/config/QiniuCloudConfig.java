package com.example.qwer.config;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.poi.util.IOUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
//七牛云配置
public class QiniuCloudConfig {


    private String accessKey;

    private String secretKey;

    private String bucket;

    private String domain;

    private Configuration cfg=new Configuration(Region.huabei());


    public String getToken(){
        Auth auth = Auth.create(this.accessKey,this.secretKey);
        String token=auth.uploadToken(this.bucket);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return token;
    }

    //传入key覆盖原文件
    public String getToken(String key){
        Auth auth = Auth.create(this.accessKey,this.secretKey);
        String token=auth.uploadToken(this.bucket,key);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return token;
    }

    public String getFile(String fileName) throws UnsupportedEncodingException {
        String domainOfBucket = "http://qyvzomc4a.hb-bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);
        return finalUrl;
    }
    //上传方法 流
    public String uploadFile(MultipartFile file){
        UploadManager uploadManager = new UploadManager(this.cfg);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHH:mm:ss");
        String key = sdf.format(new Date())+file.getResource().getFilename();
        try {
            Response response = uploadManager.put(file.getInputStream(),key,this.getToken(),null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return "上传成功";
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    //下载方法
    /**
     * 获取下载文件路径，即：donwloadUrl
     *
     * @return
     */
    public String getDownloadUrl(String targetUrl) {
        Auth auth = Auth.create(this.accessKey,this.secretKey);
        String downloadUrl = auth.privateDownloadUrl(targetUrl);
        return downloadUrl;
    }


    /**
     * 文件下载
     *
     * @param targetUrl
     */
    public void download(String targetUrl) {
        //获取downloadUrl
        String downloadUrl = getDownloadUrl(targetUrl);
        //本地保存路径
        String filePath = "F:\\hzh\\";
        download(downloadUrl, filePath);
    }


    /**
     * 通过发送http get 请求获取文件资源
     *
     * @param url
     * @param filepath
     * @return
     */
    private static void download(String url, String filepath) {
        OkHttpClient client = new OkHttpClient();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();
        okhttp3.Response resp = null;
        try {
            resp = client.newCall(req).execute();
            System.out.println(resp.isSuccessful());
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                byte[] data = readInputStream(is);
                //判断文件夹是否存在，不存在则创建
                File file = new File(filepath);
                if (!file.exists() && !file.isDirectory()) {
                    System.out.println("===文件夹不存在===创建====");
                    file.mkdir();
                }
                File imgFile = new File(filepath + "888.jpg");
                FileOutputStream fops = new FileOutputStream(imgFile);
                fops.write(data);
                fops.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unexpected code " + resp);
        }
    }

    /**
     * 读取字节输入流内容
     *
     * @param is
     * @return
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    //批量下载打包zip
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response, List<String> fileNameList) throws UnsupportedEncodingException {
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "test.zip";
        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent.contains("MSIE")||agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"),"ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            e.printStackTrace();
        }

        //循环将文件写入压缩流
        DataOutputStream os = null;
        InputStream is = null;
        for (int i=0;i<fileNameList.size();i++) {
            //获取文件路径
            String filePath =getFile(fileNameList.get(i));
            String fileName = fileNameList.get(i);
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                assert zipos != null : "压缩包内容不能为空";
//                Assert.notNull(zipos, "压缩包流为空");
                zipos.putNextEntry(new ZipEntry(fileName));
                os = new DataOutputStream(zipos);
                if(!filePath.contains("https://") && !filePath.contains("http://")){
                    //获取本地文件，url不能带有http
                    File file = new File(filePath);
                    is = new FileInputStream(file);
                }else {
                    //获取服务器端的文件，url带有http
                    URL url = new URL(filePath);
                    URLConnection connection = url.openConnection();
                    is = connection.getInputStream();
                }

                //输入流转换为输出流
                IOUtils.copy(is, os);
                zipos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //关闭流
        try {
            is.close();
            os.flush();
            os.close();
            zipos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
