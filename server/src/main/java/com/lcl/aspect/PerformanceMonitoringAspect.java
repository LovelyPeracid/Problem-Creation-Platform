package com.lcl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author LovelyPeracid
 */
@Slf4j
@Component
//@Aspect
@Order(5)//来控制 洋葱模星
public class PerformanceMonitoringAspect {
    //@Pointcut("@an")
    @Pointcut("execution(* com.lcl.*.service.*.*(..))")
    private void pointCut() {
    }
    ;
    //public 是别的也可以调用 //可以做一个切入点类
    // @Around("execution(* com.example.demo18.service.*.*(..))")
    @Around("pointCut()")
    public Object recordTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object proceed = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info(proceedingJoinPoint.getSignature() + "方法耗时：{}ms", end - begin);
        return proceed;
    }
}
