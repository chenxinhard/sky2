package com.sky.controller.userstatus;


import com.sky.result.Result;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserstatusController")
@RequestMapping("/user/shop")
@Slf4j
public class userstatusController {

    @Autowired
    private RedisTemplate redisTemplate;

   @GetMapping("/status")
    public Result<Integer> getstatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        log.info("店铺状态:{}", status ==1 ? "营业中":"打烊中");
        return Result.success(status);
    }
}
