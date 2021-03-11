package com.browser.utils;

import com.google.gson.JsonObject;
import org.apache.http.util.TextUtils;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.regex.Pattern;

public class UserUtils {

    public static String LETTERS_AND_DIGITS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String DIGITS = "0123456789";

    // 手机号码正则表达式
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    public static String generateNickNameAsDefault() {
        return generateVerificationCode(LETTERS_AND_DIGITS, 6);
    }

    /**
     * 生成验证码json， {“code”：“147147”}
     *
     * @param code
     * @return
     */
    public static String generateVerificationJson(String code) {
        if (TextUtils.isEmpty(code)) {
            code = generateVerificationCode(DIGITS, 6);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        return jsonObject.toString();
    }

    /**
     * 生成验证码
     *
     * @param strs
     * @param length
     * @return
     */
    public static String generateVerificationCode(String strs, int length) {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(strs.length());
            char c = strs.charAt(index);
            result += c;
        }
        return result;
    }

    /**
     * 判断是否是手机号
     *
     * @param tel 手机号
     * @return boolean true:是  false:否
     */
    public static boolean isMobile(String tel) {
        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, tel);
    }
}
