package com.sky.annotation;


import com.sky.enumeration.OperationType;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解加在方法上
@Retention(RetentionPolicy.RUNTIME)

public @interface Autofill {
    //数据库操作类型
    OperationType value();
}
