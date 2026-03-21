package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class categoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public PageResult<Category> list(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        List<Category> categories = categoryMapper.list(categoryPageQueryDTO);
        Page<Category> pageResult =  (Page<Category>) categories;
        return   new  PageResult<Category>(pageResult.getTotal(),pageResult.getResult());
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();


        category.setId(categoryDTO.getId());
        String name = categoryDTO.getName();
        category.setType(name.contains("套餐") ? 2:1);

        category.setName(categoryDTO.getName());

        category.setSort(categoryDTO.getSort());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    @Override
    public void statusC(String status, Integer id) {
        categoryMapper.status(status,id);
    }

    @Override
    public void save(Category category) {

         category.setStatus(1);
//         category.setCreateTime(LocalDateTime.now());
//         category.setUpdateTime(LocalDateTime.now());
//         category.setCreateUser(BaseContext.getCurrentId());
//         category.setUpdateUser(BaseContext.getCurrentId());
         categoryMapper.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.delete(id);
    }

    @Override
    public List<Category> lists(Integer type) {
        return categoryMapper.lists(type);
    }
}
