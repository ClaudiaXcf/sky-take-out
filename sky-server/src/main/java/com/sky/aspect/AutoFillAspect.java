package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
public class AutoFillAspect {

    @Before("@annotation(com.sky.annotation.AutoFill)")
    public void autofill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        //0.获取参数
        Object arg = joinPoint.getArgs()[0];
        //1.获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //2.根据方法签名获取Method对象
        Method method = methodSignature.getMethod();
        //3.根据Method对象获取该方法上的@AutoFill注解对象
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        //4.根据注解的对象 获取里面的value值
        OperationType value = annotation.value();
        //5.根据值判断是做add(4个值)还是update(2个值)处理
        if (value == OperationType.INSERT) {
            System.out.println("自动填充属性了。。。" + method);
            //5.1通过反射 获取参数身上的属性 即Field对象
            Class<?> clazz = arg.getClass();
            Field f1 = clazz.getDeclaredField("createTime");
            Field f2 = clazz.getDeclaredField("updateTime");
            Field f3 = clazz.getDeclaredField("createUser");
            Field f4 = clazz.getDeclaredField("updateUser");

            //属性是private 暴力破解
            f1.setAccessible(true);
            f2.setAccessible(true);
            f3.setAccessible(true);
            f4.setAccessible(true);

            //5.2通过Field对象调用set方法赋值
            f1.set(arg, LocalDateTime.now());
            f2.set(arg, LocalDateTime.now());
            f3.set(arg, BaseContext.getCurrentId());
            f4.set(arg, BaseContext.getCurrentId());

        } else {
            System.out.println("自动填充属性了。。。" + method);
            Class<?> clazz = arg.getClass();
            Field f1 = clazz.getDeclaredField("updateTime");
            Field f2 = clazz.getDeclaredField("updateUser");

            f1.setAccessible(true);
            f2.setAccessible(true);

            f1.set(arg, LocalDateTime.now());
            f2.set(arg, BaseContext.getCurrentId());
        }
    }
}
