package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface CategoryMapper {


      List<Category> list(CategoryPageQueryDTO categoryPageQueryDTO) ;

      @Autofill(OperationType.UPDATE)
      void update(Category category);

     @Update("update category set status=#{status} where id=#{id}")
      void status(String status, Integer id);
     @Autofill(OperationType.INSERT)
      void save(Category category);

      @Delete("delete  from category where id=#{id}")
     void delete(Long id);

    List<Category> lists(Integer type);
}
