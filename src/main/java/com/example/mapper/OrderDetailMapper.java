package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : OrderDetailMapper.java
 * @createTime : 2023/4/12 11:16
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
