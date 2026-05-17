package com.campus.canteen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Hello, World!");
        result.put("data", "测试接口正常");
        return result;
    }

    @GetMapping("/env")
    public Map<String, Object> env() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("java.version", System.getProperty("java.version"));
        result.put("user.dir", System.getProperty("user.dir"));
        return result;
    }
}