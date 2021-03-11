package com.browser.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.browser.manager.SmsManager;
import com.browser.repository.UserRepository;
import com.browser.response.UserResponse;
import com.browser.sql.User;
import com.browser.utils.UserUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    /**
     * 数据库事务，数据库Type设置为InnoDB，该类似数据库支持事务
     */
    @Transactional
    public void createTwo() {
        User user1 = new User();
        user1.setNickName("jimmy1");
        user1.setPhoneNumber("18217522770");
        user1.setPassword("147147");
        repository.save(user1);

        User user2 = new User();
        user2.setNickName("jimmy1");
        user2.setPhoneNumber("18217522770");
        user2.setPassword("147147");
        repository.save(user2);
    }

    /**
     * 查询用户
     *
     * @param phoneNumber
     * @return
     */
    public User getUserByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    /**
     * 发送注册验证码
     *
     * @param number
     * @return
     */
    public SendSmsResponse sendRegisterSms(String number) {
        return SmsManager.getInstance().sendRegisterSms(number);
    }

    /**
     * 发送重置密码验证码
     *
     * @param number
     * @return
     */
    public SendSmsResponse sendResetPasswordSms(String number) {
        return SmsManager.getInstance().sendResetPasswordSms(number);
    }

    /**
     * 用户注册
     *
     * @param phoneNumber
     * @param password
     * @param verificationCode
     * @return
     */
    public UserResponse register(String phoneNumber, String password, String verificationCode) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return new UserResponse(null, -1, "手机号码不能为空");
        }
        if (TextUtils.isEmpty(password)) {
            return new UserResponse(null, -2, "密码不能为空");
        }
        if (TextUtils.isEmpty(verificationCode)) {
            return new UserResponse(null, -3, "验证码不能为空");
        }
        // 校验验证码
        if (!SmsManager.getInstance().verifyPhoneNumberAndCode(phoneNumber, verificationCode)) {
            return new UserResponse(null, -4, "验证码错误");
        }
        // 删除短信记录
        SmsManager.getInstance().removeVerificationCode(phoneNumber);

        User user = repository.findByPhoneNumber(phoneNumber);
        if (user != null && !user.getPassword().equals(password)) {
            return new UserResponse(null, -5, "账号已存在，请直接登录");
        }
        // 保存用户数据到数据库
        User userNew = new User();
        userNew.setPhoneNumber(phoneNumber);
        userNew.setPassword(password);
        userNew.setNickName(phoneNumber.substring(0, 6) + "_" + UserUtils.generateNickNameAsDefault());
        repository.save(userNew);
        return new UserResponse(userNew, 0, "注册成功");
    }

    /**
     * 用户登录
     *
     * @param phoneNumber
     * @param password
     * @return
     */
    public UserResponse login(String phoneNumber, String password) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return new UserResponse(null, -1, "手机号码不能为空");
        }
        if (TextUtils.isEmpty(password)) {
            return new UserResponse(null, -2, "密码不能为空");
        }

        User user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return new UserResponse(null, -6, "您尚未注册");
        }
        if (!user.getPassword().equals(password)) {
            return new UserResponse(null, -7, "密码错误");
        }
        return new UserResponse(user, 0, "登录成功");
    }

    /**
     * 用户退出登录
     *
     * @param phoneNumber
     * @return
     */
    public UserResponse logout(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return null;
        }

        User user = repository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            return new UserResponse(user, 0, "退出成功");
        }
        return null;
    }

    /**
     * 修改密码
     *
     * @param phoneNumber
     * @return
     */
    public UserResponse resetPassword(String phoneNumber, String password) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return new UserResponse(null, -1, "手机号码不能为空");
        }
        if (TextUtils.isEmpty(password)) {
            return new UserResponse(null, -2, "密码不能为空");
        }

        User user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return new UserResponse(null, -6, "您尚未注册");
        }
        user.setPassword(password);
        repository.save(user);
        return new UserResponse(user, 0, "修改密码成功");
    }

}
