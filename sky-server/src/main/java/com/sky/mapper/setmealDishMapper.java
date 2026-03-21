package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface setmealDishMapper {

    void save(SetmealDish setmealDish);

   List<SetmealDish> getgetById(Long id);

    void delete(List<Long> ids);

    void deleteById(Long id);
}
