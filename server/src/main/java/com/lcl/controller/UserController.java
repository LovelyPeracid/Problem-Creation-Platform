package com.lcl.controller;

import com.lcl.annotation.Authenticate;
import com.lcl.constant.JwtClaimsConstant;
import com.lcl.dto.UserLoginDTO;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.dto.user.UserDTO;
import com.lcl.dto.user.UserRegistryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.UserService;

import com.lcl.utils.JwtUtil;
import com.lcl.utils.UserHolder;
import com.lcl.vo.UserLoginVO;
import com.lcl.vo.UserReportVO;
import com.lcl.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Result<String> logout(HttpServletRequest request) {
        return userService.logout(request);
    }
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        return  Result.success(token);
    }
    @GetMapping("/me")
    public  Result<UserDTO> me(){
        UserDTO user = UserHolder.getUser();
        return  Result.success(user);
    }
    @PostMapping()
    private  Result<UserVO> register(@RequestBody UserRegistryDTO userRegistry){
       return userService.registry(userRegistry);
    }

}
