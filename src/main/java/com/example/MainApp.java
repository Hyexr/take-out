package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example
 * @ClassName : MainApp.java
 * @createTime : 2023/2/16 10:17
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
        log.info("启动");
    }
}
