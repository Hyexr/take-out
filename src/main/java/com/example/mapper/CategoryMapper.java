package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : CategoryMapper.java
 * @createTime : 2023/3/15 10:40
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
