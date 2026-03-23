package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.dishMapper;
import com.sky.mapper.setmealMapper;
import com.sky.mapper.shoppingCardMapper;
import com.sky.service.shoppingCardService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class shoppingCardServiceImpl implements shoppingCardService {
@Autowired
private shoppingCardMapper shoppingCardMapper;
@Autowired
private dishMapper dishMapper;
@Autowired
private setmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车的商品是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> shoppings = shoppingCardMapper.list(shoppingCart);
        // 如果存在就加1
        if(shoppings !=null && shoppings.size()>0){
           ShoppingCart shop = shoppings.get(0);
           shop.setNumber(shop.getNumber()+1);
           shoppingCardMapper.updateById(shop);
        }else {
            //不存在加入数据
          Long dishId = shoppingCartDTO.getDishId();
          if(dishId != null){
              DishVO dishVO = dishMapper.get(dishId);
              shoppingCart.setName(dishVO.getName());
              shoppingCart.setImage(dishVO.getImage());
              shoppingCart.setAmount(dishVO.getPrice());



          }else {
              Long setmealId = shoppingCartDTO.getSetmealId();
              if(setmealId != null){
                  SetmealVO setmeal = setmealMapper.getByid(setmealId);
                  shoppingCart.setName(setmeal.getName());
                  shoppingCart.setImage(setmeal.getImage());
                  shoppingCart.setAmount(setmeal.getPrice());


              }

          }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCardMapper.insert(shoppingCart);


        }


    }

    @Override
    public List<ShoppingCart> list() {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart =  ShoppingCart.builder().userId(currentId).build();
        List<ShoppingCart> List = shoppingCardMapper.list(shoppingCart);
        return List;
    }

    @Override
    public void clean() {
        Long currentId = BaseContext.getCurrentId();
        shoppingCardMapper.clean(currentId);

    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        Long currentId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        ShoppingCart shoppingCart = new ShoppingCart();
        if(dishId != null){

            shoppingCart.setUserId(currentId);
            shoppingCart.setDishId(dishId);
            Integer number = shoppingCardMapper.findByDishId(shoppingCart);
            if (number>1){
                shoppingCart.setNumber(number-1);
                shoppingCardMapper.updateByIds(shoppingCart);
            }else {
                shoppingCardMapper.cleans(shoppingCart);
            }

        }else if(setmealId != null) {

            shoppingCart.setUserId(currentId);
            shoppingCart.setDishId(setmealId);
            Integer number = shoppingCardMapper.findBySetmealId(shoppingCart);
            if (number > 1) {
                shoppingCart.setNumber(number-1);
                shoppingCardMapper.updateByIds(shoppingCart);
            }else {
                shoppingCardMapper.cleans(shoppingCart);
            }

        }
    }
}
