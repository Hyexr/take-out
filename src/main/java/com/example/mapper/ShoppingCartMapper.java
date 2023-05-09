package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : ShoppingCartMapper.java
 * @createTime : 2023/4/11 11:24
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart>{
}
