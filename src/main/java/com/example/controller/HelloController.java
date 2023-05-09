package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.controller
 * @ClassName : HelloController.java
 * @createTime : 2023/2/15 10:40
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handler01(){
        return "Hello, Spring Boot 2!";
    }
}
