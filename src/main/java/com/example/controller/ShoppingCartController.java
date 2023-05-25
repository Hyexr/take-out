package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.entity.ShoppingCart;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.controller
 * @ClassName : ShoppingCarController.java
 * @createTime : 2023/4/11 11:27
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        log.info("购物车:{}",shoppingCart);

        //设置userId
        Long curId = BaseContext.getCurrentId();
        shoppingCart.setUserId(curId);
        //查询当前菜品或者套餐是否已经存在于购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,curId);
        if(dishId == null){
            //菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else{
            //套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if(cart != null){
            //存在则更新数量
            cart.setNumber(cart.getNumber() + shoppingCart.getNumber());
            shoppingCartService.updateById(cart);
        } else {
            //如果不存在则添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }

        return R.success(cart);
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        Long curId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,curId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     *
     */
    @DeleteMapping
    public R<String> clean(){

        Long curId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,curId);

        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if(cart == null){
            return R.error("购物车中不存在该商品");
        }

        cart.setNumber(cart.getNumber() - 1);
        if(cart.getNumber() == 0){
            shoppingCartService.removeById(cart.getId());
        }else{
            shoppingCartService.updateById(cart);
        }
        return R.success(cart);
    }
}
