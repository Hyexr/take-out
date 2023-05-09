package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.DishDto;
import com.example.entity.Dish;
import com.example.entity.DishFlavor;
import com.example.service.CategoryService;
import com.example.service.DishFlavorService;
import com.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.controller
 * @ClassName : DishController.java
 * @createTime : 2023/3/27 10:50
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("成功");
    }

    /**
     *  分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        // 分页对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> pageInfoDto = new Page<>();

        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);

        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,lambdaQueryWrapper);

        //复制分页对象
        BeanUtils.copyProperties(pageInfo,pageInfoDto,"records");

        //除了dish的信息 还需要有dish的category的名称
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            Long categoryId = item.getCategoryId();
            //id查分类对象名称
            String categoryName = categoryService.getById(categoryId).getName();
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(categoryName);

            return dishDto;
        }).toList();

        pageInfoDto.setRecords(list);

        return R.success(pageInfoDto);
    }

    /**
     * 根据id查询dish
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){


        DishDto dishDto = dishService.getIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        return R.success("成功");
    }

    /**
     * 查询菜品
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        //查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq( dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId() );
//        queryWrapper.eq(Dish::getStatus, 1);
//        //排序
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq( dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId() );
        queryWrapper.eq(Dish::getStatus, 1);
        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            Long categoryId = item.getCategoryId();
            //id查分类对象名称
            String categoryName = categoryService.getById(categoryId).getName();

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(categoryName);

            Long dishId = item.getId();
            //根据dishId查询口味
            List<DishFlavor> dishFlavorList = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishId));
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).toList();

        return R.success(dishDtoList);
    }
}
