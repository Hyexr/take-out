package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存
     * @param dishDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "dishCache",key = "#dishDto.categoryId+'_1'")
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
    @CacheEvict(value = "dishCache",key = "#dishDto.categoryId+'_1'")
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        //清理缓存
////        Set keys = redisTemplate.keys("dish_*");
//        String keys = "dish_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(keys);

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
    @Cacheable(value = "dishCache",key = "#dish.categoryId+'_'+#dish.status")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList = null;
//        //动态构造key
//        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
//
//        //redis获取数据
//        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
//        if (dishDtoList != null){
//            return R.success(dishDtoList);
//        }

        //查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq( dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId() );
        queryWrapper.eq(Dish::getStatus, 1);

        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
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

        //redis存储数据
//        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Dish> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(ids != null, Dish::getId, ids);
        lambdaUpdateWrapper.set(Dish::getStatus, status);
        dishService.update(lambdaUpdateWrapper);
        return R.success("成功");
    }




}
