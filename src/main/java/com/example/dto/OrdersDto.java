package com.example.dto;

import com.example.entity.OrderDetail;
import com.example.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.dto
 * @ClassName : OrdersDto.java
 * @createTime : 2023/5/23 10:11
 */
@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
}
