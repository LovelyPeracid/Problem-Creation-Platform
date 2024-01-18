package com.lcl.aspect;

import com.lcl.annotation.AutoFill;
import com.lcl.constant.AutoFillConstant;
import com.lcl.context.BaseContext;
import com.lcl.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Component
@Aspect
@Slf4j

public class AutoFillAspect {
    @Pointcut("execution(* com.lcl.mapper.*.*(..)) && @annotation(com.lcl.annotation.AutoFill) ")
    public  void autoFillPointCut(){
    }
    //@Around()
    @Before("autoFillPointCut()")
    public  void   autoFill(JoinPoint joinPoint){
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType=annotation.value();
        Object[] args = joinPoint.getArgs();
        if(args==null || args.length ==0){
            return;
        }
        Object arg = args[0];
        LocalDateTime now = LocalDateTime.now();
        switch (operationType) {
            case INSERT:{
                try {
                    Method setCreateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                   // Method setCreateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                 //   Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    setCreateTime.invoke(arg,now);
                    setUpdateTime.invoke(arg,now);
                 //   setCreateUser.invoke(arg,currendId);
                 //   setUpdateUser.invoke(arg,currendId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }break;
            case UPDATE  :{
                try {
                    Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                  //  Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setUpdateTime.invoke(arg,now);
                  //  setUpdateUser.invoke(arg,currendId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } break;
            default : System.out.println("其他操作");
                break;
        }
    }
}

