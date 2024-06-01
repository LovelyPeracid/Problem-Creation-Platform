package com.lcl.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.lcl.constant.RedisConstants;
import com.lcl.dto.user.UserDTO;
import com.lcl.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LovelyPeracid
 */
public class RefreshTokenIntercept implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenIntercept(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("authorization");
        if (StrUtil.isBlank(token)){
            return  true;
        }
        String key= RedisConstants.LOGIN_USER_KEY+token;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        if(entries.isEmpty()){
            //return  true;
            response.setStatus(401);
            return false;
        }
        UserDTO userDTO = BeanUtil.fillBeanWithMap(entries, new UserDTO(), false);
        UserHolder.saveUser(userDTO);
        stringRedisTemplate.expire(key,10, TimeUnit.HOURS);
        return  true;

     //   return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
