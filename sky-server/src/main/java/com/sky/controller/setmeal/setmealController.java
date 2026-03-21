package com.sky.controller.setmeal;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.setmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api("套餐")
public class setmealController {

    @Autowired
    private setmealService setmealService;

   @PostMapping
   public Result save(@RequestBody SetmealDTO setmealDTO){
       log.info("setmealDTO={}",setmealDTO);
           setmealService.save(setmealDTO);
           return Result.success();

    }
   @GetMapping("/page")
   public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult<SetmealVO> pages =  setmealService.page(setmealPageQueryDTO);
        return Result.success(pages);
   }

   @GetMapping("/{id}")
    public Result<SetmealVO> get(@PathVariable Long id){
           SetmealVO setmealVO = setmealService.getById(id);
           return Result.success(setmealVO);
   }




   @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
       setmealService.delete(ids);
       return Result.success();
   }

   @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
       setmealService.update(setmealDTO);
       return Result.success();

   }

   @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status,Long id){
       setmealService.statusC(status,id);
               return Result.success();
   }



}
