package com.browser.manager;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.browser.data.VerificationCodeData;
import com.browser.utils.UserUtils;
import com.google.gson.JsonObject;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.browser.utils.UserUtils.DIGITS;

public class SmsManager {

    //产品名称:云通信短信API产品,无需替换
    static final String product = "Dysmsapi";
    //产品域名,无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // accessKey信息(在阿里云访问控制台寻找，找不到就新增一个)
    static final String accessKeyId = "*****************";
    static final String accessKeySecret = "******************";

    private static final String register_sms_template = "SMS_*********";
    private static final String reset_password_sms_template = "SMS_*********";

    private List<VerificationCodeData> mCodeArray = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(SmsManager.class);

    private SmsManager() {
    }

    public static SmsManager getInstance() {
        return SmsManagerHolder.instance;
    }

    /**
     * 通过手机号获取验证码
     *
     * @param phoneNumber
     * @return
     */
    public String getVerificationCode(String phoneNumber) {
        for (VerificationCodeData data : mCodeArray) {
            if (data.getPhoneNumber().equals(phoneNumber)) {
                return data.getCode();
            }
        }
        return null;
    }

    /**
     * 删除验证码
     *
     * @param phoneNumber
     */
    public void removeVerificationCode(String phoneNumber) {
        for (VerificationCodeData data : mCodeArray) {
            if (data.getPhoneNumber().equals(phoneNumber)) {
                mCodeArray.remove(data);
                return;
            }
        }
    }

    /**
     * 验证验证码是否正确
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public boolean verifyPhoneNumberAndCode(String phoneNumber, String code) {
        System.out.println("verifyPhoneNumberAndCode phoneNumber:" + phoneNumber + " code:" + code);
        String verificationCode = getVerificationCode(phoneNumber);
        if (!TextUtils.isEmpty(verificationCode)) {
            return verificationCode.equals(code);
        }
        return false;
    }

    /**
     * 保存验证码，且发送注册短信
     *
     * @param phoneNumber
     * @return
     */
    public SendSmsResponse sendRegisterSms(String phoneNumber) {

        // 如果手机号码不符合规则，不执行操作
        if (!UserUtils.isMobile(phoneNumber)) {
            return null;
        }
        // 清除已过期的验证码
        cleanOvertimeCode();

        // 服务端保证已经发送的验证码
        String code = UserUtils.generateVerificationCode(DIGITS, 6);
        System.out.println("code:" + code);
        addVerificationCode(phoneNumber, code);

        // 通过阿里云发送验证码短信
        String codeJson = UserUtils.generateVerificationJson(code);
        System.out.println("codeJson:" + codeJson);
        return sendSmsByAli(phoneNumber, codeJson, register_sms_template);
    }

    /**
     * 保存验证码，且发送重置密码短信
     *
     * @param phoneNumber
     * @return
     */
    public SendSmsResponse sendResetPasswordSms(String phoneNumber) {

        // 如果手机号码不符合规则，不执行操作
        if (!UserUtils.isMobile(phoneNumber)) {
            return null;
        }
        // 清除已过期的验证码
        cleanOvertimeCode();

        // 服务端保证已经发送的验证码
        String code = UserUtils.generateVerificationCode(DIGITS, 6);
        addVerificationCode(phoneNumber, code);

        // 通过阿里云发送验证码短信
        String codeJson = UserUtils.generateVerificationJson(code);
        return sendSmsByAli(phoneNumber, codeJson, reset_password_sms_template);
    }

    /**
     * 清除过期的验证码
     */
    private void cleanOvertimeCode() {
        long curTime = System.currentTimeMillis();
        for (VerificationCodeData data : mCodeArray) {
            if (curTime - data.getTime() > (60 + 10) * 1000) {
                mCodeArray.remove(data);
            }
        }
    }

    /**
     * 添加验证码到数组
     * @param phoneNumber
     * @param code
     */
    private void addVerificationCode(String phoneNumber, String code) {
        VerificationCodeData data = new VerificationCodeData(phoneNumber, code, System.currentTimeMillis());
        System.out.println("addVerificationCode phoneNumber:" + phoneNumber + " code:" + code);
        mCodeArray.add(data);
    }

    /**
     * 阿里云发送短信验证码
     * @param phoneNumber
     * @param codeJson
     * @return
     */
    private SendSmsResponse sendSmsByAli(String phoneNumber, String codeJson, String smsTemplate) {
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("好物浏览器");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(smsTemplate);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(codeJson);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = null;
            sendSmsResponse = acsClient.getAcsResponse(request);
            return sendSmsResponse;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class SmsManagerHolder {
        private static SmsManager instance = new SmsManager();
    }

}
