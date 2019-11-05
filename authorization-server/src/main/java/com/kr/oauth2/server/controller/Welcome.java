package com.kr.oauth2.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {
    @GetMapping("/")
    public String welcome(){
        return "<h1>Welcome to Blakol Oauth Server</h1>";
    }
}
