package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.setmealDishMapper;
import com.sky.mapper.setmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.setmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class setmealServiceImpl implements setmealService {


    @Autowired
    private setmealMapper setmealMapper;

    @Autowired
    private setmealDishMapper setmealDishMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SetmealDTO setmealDTO) {
        setmealDTO.setStatus(StatusConstant.ENABLE);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);
        List<SetmealDish> setmealDishes = new ArrayList<>();

        setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes.size() > 0 && setmealDishes != null) {
            for (SetmealDish setmealDish : setmealDishes) {
                Long setmealId = setmeal.getId();
                setmealDish.setSetmealId(setmealId);
                setmealDishMapper.save(setmealDish);

            }
        }
    }

    @Override
    public PageResult<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        List<SetmealVO> pages = setmealMapper.page(setmealPageQueryDTO);

        Page<SetmealVO> pageResult = (Page<SetmealVO>) pages;
        return new PageResult<SetmealVO>(pageResult.getTotal(), pageResult.getResult());

    }

    @Override
    public SetmealVO getById(Long id) {

        SetmealVO byid = setmealMapper.getByid(id);
        List  <SetmealDish> setmealDish = setmealDishMapper.getgetById(id);
        for(SetmealDish setmealDish1:setmealDish){
            byid.getSetmealDishes().add(setmealDish1);
        }

        return byid;
    }


    @Override
    public void delete(List<Long> ids) {
        setmealMapper.delete(ids);
        setmealDishMapper.delete(ids);
    }

    @Override
    public void statusC(Integer status,Long id) {
        setmealMapper.statusC(status,id);
    }


    @Override
    public void update(SetmealDTO setmealDTO) {
      Long  id = setmealDTO.getId();
        setmealMapper.deleteById(id);
        setmealDishMapper.deleteById(id);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        List<SetmealDish>  setmealDishes = new ArrayList<>();
        setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes.size() > 0 && setmealDishes != null) {
            for (SetmealDish setmealDish1 : setmealDishes) {
                Long setmealId = setmeal.getId();
                setmealDish1.setSetmealId(setmealId);
                setmealDishMapper.save(setmealDish1);
            }
        }


    }
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }
     @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}

