package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;
import java.util.Set;

@Mapper
public interface setmealMapper {
    @Autofill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    List<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getByid(Long id);

    void delete(List<Long> ids);

    void deleteById(Long id);

    void statusC(Integer status,Long id);

    List<Setmeal> list(Setmeal setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
