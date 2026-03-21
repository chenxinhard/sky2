package com.sky.controller.adminstatus;


import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminstatusController")//设置bean的名字
@RequestMapping("/admin/shop")
@Slf4j
@Api("店铺状态设置")
public class adminstatusController {

    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    public Result<String> getStatus(@PathVariable Integer status) {
        log.info("店铺状态：{}", status ==1 ? "营业中":"打烊中");
        redisTemplate.opsForValue().set("status", status);
        return Result.success();


    }

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        log.info("店铺状态:{}", status ==1 ? "营业中":"打烊中");
        return Result.success(status);

    }
}
