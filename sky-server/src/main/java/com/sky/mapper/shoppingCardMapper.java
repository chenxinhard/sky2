package com.sky.mapper;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface shoppingCardMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateById(ShoppingCart shop);
     @Select("select number\n" +
             "from shopping_cart where user_id=#{userId} and dish_id=#{dishId}")
    Integer findByDishId(ShoppingCart  shoppingCart);

    @Select("select number\n" +
           "from shopping_cart where user_id=#{userId} and setmeal_id=#{setmealId}")
    Integer findBySetmealId(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);
   @Delete("delete from shopping_cart where user_id=#{currentId}")
    void clean(Long currentId);

    void updateByIds(ShoppingCart shoppingCart);

    void cleans(ShoppingCart shoppingCart);
}
