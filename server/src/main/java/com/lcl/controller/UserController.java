package com.lcl.controller;

import com.lcl.annotation.Authenticate;
import com.lcl.constant.JwtClaimsConstant;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.UserService;

import com.lcl.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LovelyPeracid
 */
@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @ApiOperation("获取全部用户")
    public Result list(){
        List<ExtUser> list= userService.queryAll();
        return  Result.success(list);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO){
        PageResult pageResult=userService.page(userPageQueryDTO);
        return  Result.success(pageResult);
    }
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
//    @PostMapping("/login")
//    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
//        log.info("员工登录：{}", employeeLoginDTO);
//
//        Employee employee = employeeService.login(employeeLoginDTO);
//
//        //登录成功后，生成jwt令牌
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
//        String token = JwtUtil.createJWT(
//                jwtProperties.getAdminSecretKey(),
//                jwtProperties.getAdminTtl(),
//                claims);
//
//        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
//                .id(employee.getId())
//                .userName(employee.getUsername())
//                .name(employee.getName())
//                .token(token)
//                .build();
//
//        return Result.success(employeeLoginVO);
//    }
}
