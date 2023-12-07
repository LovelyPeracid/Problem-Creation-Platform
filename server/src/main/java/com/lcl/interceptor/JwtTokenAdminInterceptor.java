package com.lcl.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.lcl.constant.JwtClaimsConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.MethodConstant;
import com.lcl.context.BaseContext;
import com.lcl.entity.ExtUser;
import com.lcl.mapper.UserMapper;
import com.lcl.properties.JwtProperties;
import com.lcl.result.Result;
import com.lcl.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        //Long id = Long.valueOf(request.getHeader("id"));
        Long id=null;
        System.out.println("拦截器开始");
        try {
           id= Long.valueOf(request.getHeader("id"));
            String method = request.getMethod();
            if(method!= MethodConstant.GET){
                ExtUser byId =  userMapper.getById(id);
                if(byId==null||byId.getIsSuspended()){
                    throw new  AuthException(MessageConstant.ACCESS_DENIED);
                }
            }
        }
        catch (NumberFormatException | NullPointerException e){
            response.setStatus(401);
            response.setContentType("application/json");
            //response.
            Result<Object> error = Result.error(MessageConstant.HEADER_FORMAT_ERROR);
            String jsonString = JSONObject.toJSONString(error);
            response.getWriter().write(jsonString);
        }
        catch (AuthException e){
            response.setStatus(403);
            response.setContentType("application/json");
            Result<Object> error = Result.error(e.getMessage());
            String jsonString = JSONObject.toJSONString(error);
            response.getWriter().write(jsonString);
        }
        catch (Exception e) {
             response.setStatus(401);
             return false;
       }
        BaseContext.setCurrentId(id);
        return true;
//        if (!(handler instanceof HandlerMethod)) {
//            //当前拦截到的不是动态方法，直接放行
//            return true;
//        }
//
//        //1、从请求头中获取令牌
//        String token = request.getHeader(jwtProperties.getAdminTokenName());
//
//        //2、校验令牌
//        try {
//            log.info("jwt校验:{}", token);
//            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
//            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
//            log.info("当前员工id：", empId);
//            //3、通过，放行
//            return true;
//        } catch (Exception ex) {
//            //4、不通过，响应401状态码
//            response.setStatus(401);
//            return false;
//        }
    }
}
