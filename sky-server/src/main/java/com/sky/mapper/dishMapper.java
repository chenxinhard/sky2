package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface dishMapper {

    @Autofill(value = OperationType.INSERT)
    void save(Dish dish);

    List<DishVO> list(DishPageQueryDTO dishPageQueryDTO);

    DishVO get(Long id);

    List<Dish> lists();

    void delete(List<Long> ids);


    void status(Integer status,Long id);

    List<Dish> dishlist(Dish dish);
}
