package com.sky.Aspect;


import com.sky.annotation.Autofill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

//切面类
@Aspect
@Component//交给IOC容器管理
@Slf4j
public class Autofillaspect {
    //切入点

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.Autofill)")//所有类。所有方法参数

    public void pointcut() {
    }

    //前置通知
    @Before("pointcut()")
    public void autofill(JoinPoint joinPoint) throws NoSuchMethodException {
        log.info("开始填充:{}");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Autofill autofill = signature.getMethod().getAnnotation(Autofill.class);//获取注解字节码文件
        OperationType operationType = autofill.value();//获取数据库操作
        Object[] args = joinPoint.getArgs();
        //防止空指针
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];
        LocalDateTime Now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        if (operationType == OperationType.INSERT) {

            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity, Now);
                setUpdateTime.invoke(entity, Now);

                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);


            }
        } else if (operationType == OperationType.UPDATE) {
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity, Now);
                setUpdateUser.invoke(entity, currentId);

            } catch (Exception e) {
            }
        }
    }
}
