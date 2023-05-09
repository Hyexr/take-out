package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Orders;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.service
 * @ClassName : OrderService.java
 * @createTime : 2023/4/12 11:17
 */
public interface OrderService extends IService<Orders>{

    void submit(Orders orders);
}
