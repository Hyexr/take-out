package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.SMSUtils;
import com.example.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.controller
 * @ClassName : UserController.java
 * @createTime : 2023/4/7 10:51
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){

            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //发送短信
            log.info("code={}", code);
//          SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            //保存验证码
            //session.setAttribute(phone, code);
            //验证码缓存至redis
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            R.success("成功");
        }

        return R.error("失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

       //获取手机号
        String phone =map.get("phone").toString();

        //获取验证码
        String code =map.get("code").toString();

        //获取session中的验证码
//        Object sessionCode = session.getAttribute(phone);
        //获取redis中的验证码
        Object sessionCode = redisTemplate.opsForValue().get(phone);

        //判断验证码是否正确
        if(sessionCode!=null && sessionCode.equals(code)){

            //判断用户是否存在
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);

            //如果用户不存在
            if(user == null){
                //创建用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            //删除验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }

        return R.error("失败");
    }
}
