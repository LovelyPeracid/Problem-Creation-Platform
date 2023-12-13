package com.lcl.aspect;

import com.lcl.constant.MessageConstant;
import com.lcl.context.BaseContext;
import com.lcl.entity.ExtUser;
import com.lcl.entity.Space;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;
import java.util.Arrays;

/**
 * @author LovelyPeracid
 */
@Component
@Aspect
@Slf4j
public class AuthenticationAspect {
    @Autowired
    private SpaceMapper spaceMapper;
    @Autowired
    private UserMapper userMapper;
    @Pointcut("@annotation(com.lcl.annotation.Authenticate)")
    private  void  pt(){}
    @Around("pt()")
    public  Object  add(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("开始拦截");
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Space byId = spaceMapper.getById(spaceId);
        Long currentId = BaseContext.getCurrentId();
        ExtUser user= userMapper.getById(currentId);
        if(byId.getOwner()!=currentId){
            throw new AuthException(MessageConstant.ACCESS_DENIED);
        }
        System.out.println(Arrays.toString(args));
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("拦截完成");
        return  proceed;
    }
}
