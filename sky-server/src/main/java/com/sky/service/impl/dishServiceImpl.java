package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.dishMapper;
import com.sky.mapper.dishfloverMapper;
import com.sky.result.PageResult;
import com.sky.service.dishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class dishServiceImpl implements dishService {
    @Autowired
   private dishMapper dishMapper;

    @Autowired
    private dishfloverMapper dishfloverMapper;

    @Override
    public void save(DishDTO dishDTO) {
       Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.save(dish);

        List<DishFlavor> flavor = dishDTO.getFlavors();

         Long dishId = dish.getId();

       if(flavor !=null && flavor.size()>0){
           flavor.forEach(flavor1->{
                flavor1.setDishId(dishId);
                   });
           dishfloverMapper.save(flavor);

       }


    }

    @Override
    public PageResult<DishVO> list(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        List<DishVO>dishList = dishMapper.list(dishPageQueryDTO);
        Page<DishVO> dishLists =   (Page<DishVO>) dishList;
        return new PageResult<DishVO>(dishLists.getTotal(),dishLists.getResult());
    }

    @Override
    public DishVO get(Long id) {

       return dishMapper.get(id);

    }

    @Override
    public List<Dish> lists() {
     List<Dish> result =  dishMapper.lists();
     return   result;
    }

    @Override
    public void delete(List<Long> ids) {
        List<Long> list = new ArrayList<>();
        list.addAll(ids);

        dishMapper.delete(list);
        dishfloverMapper.delete(list);
    }

    @Override
    public void update(DishDTO dishDTO) {

        List<Long> id =new ArrayList<>();
        id.add(dishDTO.getId());
        dishMapper.delete(id);
        dishfloverMapper.delete(id);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.save(dish);

        List<DishFlavor> flavor = dishDTO.getFlavors();
        Long dishId = dish.getId();
        if (flavor != null && flavor.size() > 0) {
            flavor.forEach(flavor1 -> {
                flavor1.setDishId(dishId);
            });
            dishfloverMapper.save(flavor);
        }
    }

    @Override
    public void status(Integer status,Long id) {
        dishMapper.status(status,id);
    }

    @Override
    public List<DishVO> listwithFlavor(Dish dish) {
        List<Dish> dishlist = dishMapper.dishlist(dish);

       List<DishVO> dishLists =  new ArrayList<>();

        for(Dish dish1:dishlist){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish1,dishVO);

            Long dishId = dish1.getId();
          List<DishFlavor> flavors=   dishfloverMapper.getById(dishId);
            dishVO.setFlavors(flavors);
            dishLists.add(dishVO);

        }
        return dishLists;
    }

}

