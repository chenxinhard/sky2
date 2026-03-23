package com.sky.controller.shapping;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.setmealService;
import com.sky.service.shoppingCardService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车")
public class shoppingcardController {


    @Autowired
    private shoppingCardService shoppingCardService;


    @PostMapping("/add")
    public Result  add(@RequestBody  ShoppingCartDTO shoppingCartDTO){
        log.info("传递参数",shoppingCartDTO);
        shoppingCardService.add(shoppingCartDTO);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<ShoppingCart>>  list(){
       List<ShoppingCart > lists = shoppingCardService.list();
        return Result.success(lists);
    }
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCardService.clean();
        return Result.success();
    }

    @PostMapping("/sub")
    public Result sub(@RequestBody  ShoppingCartDTO shoppingCartDTO){
        log.info("删除的套餐或者菜品",shoppingCartDTO);
        shoppingCardService.delete(shoppingCartDTO);
        return Result.success();

    }

}
