package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Category;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.service
 * @ClassName : CategoryService.java
 * @createTime : 2023/3/15 10:42
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
