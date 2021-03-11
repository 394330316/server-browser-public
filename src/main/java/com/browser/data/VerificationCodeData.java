package com.browser.data;

public class VerificationCodeData {

    private String phoneNumber;

    private String code;

    private long time;

    public VerificationCodeData(String phoneNumber, String code, long time) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.time = time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
