package com.browser.response;

import com.browser.sql.User;

public class UserResponse {
    private int code;
    private String msg;
    private User user;

    public UserResponse(User user, int code, String msg) {
        this.user = user;
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
