package com.lcl.interceptor;

import com.lcl.constant.JwtClaimsConstant;
import com.lcl.context.BaseContext;
import com.lcl.properties.JwtProperties;
import com.lcl.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
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
        Long id;
        try {
           id= Long.valueOf(request.getHeader("id"));
        }
         catch (Exception e) {
            //4、不通过，响应401状态码
          //  response.setStatus(401);
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
