package com.browser.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.browser.repository.UserRepository;
import com.browser.response.UserResponse;
import com.browser.service.UserService;
import com.browser.sql.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> list() {
        return repository.findAll();
    }

    @PostMapping("/createUser")
    public User create(@RequestParam("nickName") String nickName,
                       @RequestParam("phoneNumber") String phoneNumber,
                       @RequestParam("password") String password) {
        User user = new User();
        user.setNickName(nickName);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return repository.save(user);

    }

    @GetMapping("/getUserById")
    public User getUserById(@RequestParam("id") Integer id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/getUserByPhoneNumber")
    public User getUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return userService.getUserByPhoneNumber(phoneNumber);
    }

    @PutMapping("/update")
    public User updateUser(@PathVariable("id") Integer id,
                           @RequestParam("phoneNumber") String phoneNumber) {
        Optional<User> optional = repository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setPhoneNumber(phoneNumber);
            return repository.save(user);
        }
        return null;
    }

    @GetMapping("/sendRegisterSms")
    public SendSmsResponse sendRegisterSms(@RequestParam("phoneNumber") String phoneNumber) {
        return userService.sendRegisterSms(phoneNumber);
    }

    @GetMapping("/sendResetPasswordSms")
    public SendSmsResponse sendResetPasswordSms(@RequestParam("phoneNumber") String phoneNumber) {
        return userService.sendResetPasswordSms(phoneNumber);
    }

    @PostMapping("/register")
    public UserResponse register(@RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("password") String password,
                                 @RequestParam("verificationCode") String verificationCode) {
        return userService.register(phoneNumber, password, verificationCode);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("password") String password) {
        return userService.login(phoneNumber, password);
    }

    @PostMapping("/logout")
    public UserResponse logout(@RequestParam("phoneNumber") String phoneNumber) {
        return userService.logout(phoneNumber);
    }

    @PostMapping("/resetPassword")
    public UserResponse resetPassword(@RequestParam("phoneNumber") String phoneNumber,
                              @RequestParam("password") String password) {
        return userService.resetPassword(phoneNumber, password);
    }
}
