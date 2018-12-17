package com.baizhi.springboot.controller;

import com.baizhi.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    public void login(){}
}
