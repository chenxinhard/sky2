package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface userMapper {

    @Select("select *  from user where openid=#{openid}")
   User getByopenid(String openid);

    void insert(User user);
}
