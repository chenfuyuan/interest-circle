package com.cfy.interest.provider;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.cfy.interest.dto.SmsResponse;
import com.cfy.interest.service.vo.SendSmsMessage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:smsCode.properties",encoding = "utf-8")
@ConfigurationProperties(prefix = "user.aliyun.smscode")
@EnableConfigurationProperties
@Data
public class AliyunSmsProvider {

    private String accessId;
    private String accessSecret;
    private String domain;
    private String version;
    private String signName;
    private String regTemplateCode;
    private String regionId;
    public void sendSms(SendSmsMessage sendSmsMessage) {
        System.out.println(signName);
        DefaultProfile profile = DefaultProfile.getProfile(regionId,accessId,accessSecret);

        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion(version);
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", sendSmsMessage.getPhone());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", regTemplateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":"+sendSmsMessage.getAuthCode()+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            //将响应封装json封装成对象
            SmsResponse smsResponse = JSON.parseObject(response.getData(), SmsResponse.class);

            //填充短信信息状态
            sendSmsMessage.setMessage(smsResponse.getMessage());

            //当状态码为OK时，设置短信发送状态
            if (smsResponse.getCode().equals("OK")) {
                sendSmsMessage.setSuccess(true);
            }

            System.out.println(sendSmsMessage.toString());

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
