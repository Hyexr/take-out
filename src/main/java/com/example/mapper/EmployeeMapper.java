package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.mapper
 * @ClassName : EmployeeMapper.java
 * @createTime : 2023/2/23 10:47
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
