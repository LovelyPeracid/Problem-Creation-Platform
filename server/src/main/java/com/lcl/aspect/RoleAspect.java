package com.lcl.aspect;

import com.lcl.constant.MessageConstant;
import com.lcl.constant.RoleConstant;
import com.lcl.context.BaseContext;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.exception.BaseException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceUserMapper;
import com.lcl.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;

/**
 * @author LovelyPeracid
 */
@Component
@Aspect
@Slf4j
@Order(10)
public class RoleAspect {
    @Autowired
    private SpaceMapper spaceMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SpaceUserMapper spaceUserMapper;
    @Pointcut("@annotation(com.lcl.annotation.Admin)")
    private void  admin(){};
    @Pointcut("@annotation(com.lcl.annotation.UserAuth)")
    private void  user(){};
    @Pointcut("@annotation(com.lcl.annotation.VisitorAuth)")
    private void  visitor(){};
    @Around("admin()")
    public Object admin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Space byId = spaceMapper.getById(spaceId);
        Long currentId = BaseContext.getCurrentId();
        SpaceUser currentUserId = spaceUserMapper.getByUserId(spaceId, currentId);
        if(byId==null){
            throw new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }
        if(currentUserId.getRole()> RoleConstant.ADMIN||currentUserId.getIsSuspended()){
            throw  new AuthException(MessageConstant.ACCESS_DENIED);
        }
        Object proceed = proceedingJoinPoint.proceed();
        return  proceed;

    }
    @Around("visitor()")
    public Object visitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Space byId = spaceMapper.getById(spaceId);
        Long currentId = BaseContext.getCurrentId();
        SpaceUser currentUserId = spaceUserMapper.getByUserId(spaceId, currentId);
        if(byId==null){
            throw new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }
        if(currentUserId.getRole()> RoleConstant.VISITOR||currentUserId.getIsSuspended()){
            throw  new AuthException(MessageConstant.ACCESS_DENIED);
        }
        Object proceed = proceedingJoinPoint.proceed();
        return  proceed;

    }

    @Around("user()")
    public Object user(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Space byId = spaceMapper.getById(spaceId);
        Long currentId = BaseContext.getCurrentId();
        SpaceUser currentUserId = spaceUserMapper.getByUserId(spaceId, currentId);
        if(byId==null){
            throw new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }
        if(currentUserId.getRole()> RoleConstant.USER||currentUserId.getIsSuspended()){
            throw  new AuthException(MessageConstant.ACCESS_DENIED);
        }
        Object proceed = proceedingJoinPoint.proceed();
        return  proceed;

    }


}
