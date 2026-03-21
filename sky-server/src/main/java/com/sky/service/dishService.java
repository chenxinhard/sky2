package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface dishService {
    void save(DishDTO dishDTO);

    PageResult<DishVO> list(DishPageQueryDTO dishPageQueryDTO);

    DishVO get(Long id);

    List<Dish> lists();

    void delete(List<Long> ids);

    void update(DishDTO dishDTO);

    void status(Integer status, Long id);

    List<DishVO> listwithFlavor(Dish dish);
}
