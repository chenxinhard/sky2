package com.sky.controller.dish;



import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.dishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api
public class dishController {
     @Autowired
   private dishService dishService;

     @Autowired
     private RedisTemplate redisTemplate;

    @PostMapping
    public Result save(@RequestBody  DishDTO dishDTO){
        log.info("开始:{}",dishDTO);
       dishService.save(dishDTO);
       Long categoryId = dishDTO.getCategoryId();
       String key ="dish"+categoryId;
       cleanCache(key);
       return Result.success();
    }
    @GetMapping("/page")
    public Result<PageResult> list(DishPageQueryDTO dishPageQueryDTO){
        log.info("开始查询");
      PageResult<DishVO> dishList=  dishService.list(dishPageQueryDTO);
      return Result.success(dishList);
    }
//    类似于/admin/100 放在路径
    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable Long id){

             DishVO list =  dishService.get(id);
             return Result.success(list);
    }

    @PutMapping
    public Result update(@RequestBody  DishDTO dishDTO){
        dishService.update(dishDTO);
        cleanCache("dish_**");
        return Result.success();
    }


    @GetMapping("/list")
    public Result<List<Dish>> lists(){
      List<Dish> lists =   dishService.lists();
      return Result.success(lists);
    }

///admin?id=100 放在参数
    @DeleteMapping
    public Result delete(@RequestParam  List<Long> ids){
        dishService.delete(ids);
        cleanCache("dish_**");
        return Result.success();
    }


    @PostMapping("/status/{status}")
   public Result status(@PathVariable Integer status,Long id){
        dishService.status(status,id);
        cleanCache("dish_**");
        return Result.success();
    }

    public void cleanCache(String key){
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);


    }
}
