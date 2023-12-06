package com.lcl.aspect;

import com.lcl.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author LovelyPeracid
 */
@Component
@Aspect
@Slf4j
public class AuthenticationAspect {
    @Pointcut("@annotation(com.lcl.annotation.Authenticate)")
    private  void  pt(){}
    @Around("pt()")
    public  Object  add(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("开始拦截");
        Object[] args = proceedingJoinPoint.getArgs();
     //   Long currentId = BaseContext.getCurrentId();
     //   if (currentId==3) return "权限不足";
        System.out.println(Arrays.toString(args));
        Object proceed = proceedingJoinPoint.proceed();
        Long currentId = BaseContext.getCurrentId();
        if (currentId==3) return "权限不足";
        System.out.println("拦截完成");
        return  proceed;
    }
}
