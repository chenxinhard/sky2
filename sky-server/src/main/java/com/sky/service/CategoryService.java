package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface  CategoryService {
    PageResult<Category> list(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(CategoryDTO categoryDTO);

    void statusC(String status, Integer id);

    void save(Category category);

    void delete(Long id);

    List<Category> lists(Integer type);
}
