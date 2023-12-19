package com.lcl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author LovelyPeracid
 */
@Slf4j
@Component
public class VisitorAspect {
    @Pointcut("@annotation(com.lcl.annotation.VisitorAuth)")
    private void  visitor(){};

}
