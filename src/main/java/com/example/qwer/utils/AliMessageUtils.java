package com.example.qwer.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.example.qwer.pojo.Log;
import com.example.qwer.pojo.MessageSign;
import com.qiniu.util.Json;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Data
@ConfigurationProperties(prefix = "alimessage")
public class AliMessageUtils {
    private String regionId;
    private String accessKeyId;
    private String accessKeySecret;
    private String secret;

    public void sendMessage(MessageSign messageSign,String phoneNumber) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(this.accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(this.accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client=new Client(config);
        Map map=new HashMap();
        map.put("code","2131");
        SendSmsRequest sendReq=new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName("今软科技")
                .setTemplateCode(messageSign.getValue())
                .setTemplateParam(Json.encode(map));
        SendSmsResponse sendResp = client.sendSms(sendReq);
        String getCode = sendResp.body.code;
        if (!Common.equalString(getCode, "OK")) {
            System.out.println(sendResp.body.message + "");
            return ;
        }

        String bizId = sendResp.body.bizId;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        // 2. 等待 10 秒后查询结果
        Common.sleep(10000);
        // 3.查询结果
        QuerySendDetailsRequest queryReq = new QuerySendDetailsRequest()
                    .setPhoneNumber(Common.assertAsString(phoneNumber))
                    .setBizId(bizId)
                    .setSendDate(sdf.format(new Date()))
                    .setPageSize(10L)
                    .setCurrentPage(1L);
            QuerySendDetailsResponse queryResp = client.querySendDetails(queryReq);
            List<QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO> dtos = queryResp.body.smsSendDetailDTOs.smsSendDetailDTO;
            // 打印结果
            for (QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO dto : dtos) {
                if (Common.equalString("" + dto.sendStatus + "", "3")) {
                    System.out.println( dto.phoneNum + " 发送成功，接收时间: " + dto.receiveDate + "");
                } else if (Common.equalString("" + dto.sendStatus + "", "2")) {
                    System.out.println( dto.phoneNum + " 发送失败");
                } else {
                    System.out.println(dto.phoneNum + " 正在发送中...");
                }
            }
        }

}

