package com.sky.controller.category;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "菜品")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
   @GetMapping("/page")
   @ApiOperation("查询")
    public  Result<PageResult> list(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult<Category> pagecategory = categoryService.list(categoryPageQueryDTO);

        return Result.success(pagecategory);
    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
       categoryService.update(categoryDTO);
       return Result.success();
    }
    @PostMapping("/status/{status}")
   public Result status(@PathVariable String status, Integer id) {
       categoryService.statusC(status ,id);
       return Result.success();

    }
   @PostMapping
    public Result save(@RequestBody Category category) {
       categoryService.save(category);
       return Result.success();
   }
    @DeleteMapping
    public Result delete(Long id){
       categoryService.delete(id);
       return Result.success();
   }
   @GetMapping("/list")
       public Result<List<Category>> list(Integer type){
       List<Category> lists =   categoryService.lists(type);
         return Result.success(lists);
       }

}
