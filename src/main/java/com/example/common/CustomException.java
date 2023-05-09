package com.example.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.common
 * @ClassName : CustomException.java
 * @createTime : 2023/3/16 16:33
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
