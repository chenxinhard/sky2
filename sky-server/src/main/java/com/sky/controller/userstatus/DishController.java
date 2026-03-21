package com.sky.controller.userstatus;


import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.dishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController("DishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {

    @Autowired
    private dishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        //构造redis中的key,dish_分类ID
        String key = "dish_" +categoryId;
        //查询是否在redis缓存·中
        List<DishVO> DISHS= (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(DISHS !=null && DISHS.size()>0){
            //存在返回
            return Result.success(DISHS);
        }


        //不存在查询数据库·后再放入redis
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listwithFlavor(dish);
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);

    }
}
