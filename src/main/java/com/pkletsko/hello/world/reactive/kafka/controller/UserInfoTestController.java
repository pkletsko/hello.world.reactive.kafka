package com.pkletsko.hello.world.reactive.kafka.controller;

import com.pkletsko.hello.world.reactive.kafka.service.UserInfoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userinfo")
public class UserInfoTestController {
    @Autowired
    UserInfoProducer userInfoProducer;
    @PostMapping
    public void sendUserInfoToKafkaTopic() {
        userInfoProducer.sendUserInfo();
    }
}
