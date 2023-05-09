package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : DishFlavorMapper.java
 * @createTime : 2023/3/27 10:45
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
