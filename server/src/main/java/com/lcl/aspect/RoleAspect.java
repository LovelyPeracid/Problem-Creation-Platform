package com.lcl.aspect;

import com.lcl.constant.MessageConstant;
import com.lcl.constant.RoleConstant;
import com.lcl.context.BaseContext;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.enumeration.Role;
import com.lcl.exception.BaseException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceUserMapper;
import com.lcl.mapper.UserMapper;
import com.lcl.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
    @Pointcut("@annotation(com.lcl.annotation.Role)")
    private void role() {

    }
    /**
     * 把枚举类Role改了一点点
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("role()")
    public Object role(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        Role role = getRole(proceedingJoinPoint);
        Long spaceId = (Long) args[0];
        Space space= spaceMapper.getById(spaceId);
        //Long currentId = BaseContext.getCurrentId();
        Long currentId= UserHolder.getUser().getUserId();
        SpaceUser currentUserId = spaceUserMapper.getByUserIdAndSpace(spaceId, currentId);
        ///SpaceUser userId = spaceUserMapper.getByUserId(spaceId, currentId);/
        if (space == null) {
            throw new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }

        if(currentUserId==null|| currentUserId.getRole() > role.getAccessLevel() || currentUserId.getIsSuspended()) {
            throw new AuthException(MessageConstant.ACCESS_DENIED);
        }
        return proceedingJoinPoint.proceed();
    }
    private Role getRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        com.lcl.annotation.Role role = signature.getMethod().getAnnotation(com.lcl.annotation.Role.class);
        return role.value();
    }


}
