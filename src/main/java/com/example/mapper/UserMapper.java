package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : UserMapper.java
 * @createTime : 2023/4/7 10:48
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
