package com.lcl.aspect;

import com.lcl.annotation.Authenticate;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.RoleConstant;
import com.lcl.context.BaseContext;
import com.lcl.dto.SpaceUpdateDTO;
import com.lcl.dto.SpaceUserUpdateDTO;
import com.lcl.entity.ExtUser;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.exception.BaseException;
import com.lcl.mapper.ProblemMapper;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceUserMapper;
import com.lcl.mapper.UserMapper;
import com.lcl.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;
import java.util.Arrays;
import java.util.Objects;

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
    @Autowired
    private SpaceUserMapper  spaceUserMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Pointcut("@annotation(com.lcl.annotation.Authenticate)")
    private  void  pt(){};
    @Pointcut("@annotation(com.lcl.annotation.UserAuth)")
    private  void  userPt(){};
    @Pointcut("@annotation(com.lcl.annotation.Admin)")
    private void  admin(){};
    @Pointcut("@annotation(com.lcl.annotation.HigherRole)")
    private void  HigherRole(){};
    @Pointcut("@annotation(com.lcl.annotation.UserInOtherSpace)")
    private void CloneOrQuote(){};
    @Pointcut("@annotation(com.lcl.annotation.UserInOtherSpace)")
    private void UserInOtherSpace(){};
    /**
     * @param proceedingJoinPoint:
     * @return Object
     * @author LovelyPeracid
     * @description 删除空间
     * @date 2023/12/18 14:33
     */

    @Around("pt()")
    public  Object  root(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("开始拦截");
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Space byId = spaceMapper.getById(spaceId);
        Long currentId = BaseContext.getCurrentId();
  //      ExtUser user= userMapper.getById(currentId);
        if(!Objects.equals(byId.getOwner(), currentId)){
            throw new AuthException(MessageConstant.ACCESS_DENIED);
          //  return Result.error("权限不足");
        }
        System.out.println(Arrays.toString(args));
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("拦截完成");
        return  proceed;
    }

    @Around("HigherRole()")
    public  Object desc(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        Long spaceId=(Long) args[0];
        Long UserId=(Long) args[1];
        Long currentId = BaseContext.getCurrentId();
        SpaceUser byUserId = spaceUserMapper.getByUserId(spaceId,UserId);
        SpaceUser currentUserId = spaceUserMapper.getByUserId(spaceId, currentId);
        if(currentUserId.getRole()>=byUserId.getRole()){
            throw new AuthException(MessageConstant.ACCESS_DENIED);
        }
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("拦截完成");
        return  proceed;
    }
    @Around("UserInOtherSpace()")
    public  Object transfer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        //Long problemId=(Long) args[0];
        Long newSpaceId=(Long) args[1];
        //Problem byId = problemMapper.getById(problemId);
        SpaceUser userId = spaceUserMapper.getByUserId(newSpaceId, BaseContext.getCurrentId());
        if(userId.getRole()>RoleConstant.USER){
            throw new AuthException(MessageConstant.ACCESS_DENIED);
        }
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("拦截完成");
        return  proceed;
    }

    //@Around()
//    @Around("admin()")
//    public Object admin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//        Object[] args = proceedingJoinPoint.getArgs();
//        Long spaceId=(Long) args[0];
//        Space byId = spaceMapper.getById(spaceId);
//        Long currentId = BaseContext.getCurrentId();
//        SpaceUser currentUserId = spaceUserMapper.getByUserId(spaceId, currentId);
//        SpaceUserUpdateDTO spaceUpdateDTO =(SpaceUserUpdateDTO) args[1];
//        if(byId==null){
//            throw new BaseException(MessageConstant.SPACE_NOT_EXIST);
//        }
//        if(currentUserId.getRole()> RoleConstant.ADMIN||currentUserId.getIsSuspended()){
//            throw  new AuthException(MessageConstant.ACCESS_DENIED);
//        }
//
//        if(spaceUpdateDTO.getRole()!=null){
//            if(spaceUpdateDTO.getRole()<=currentUserId.getRole()){
//                throw  new AuthException(MessageConstant.ACCESS_DENIED);
//            }
//        }
//
//        Object proceed = proceedingJoinPoint.proceed();
//        System.out.println("拦截完成");
//        return  proceed;
//
//    }
}
