package com.lcl.service;

import com.lcl.dto.UserLoginDTO;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.dto.user.UserRegistryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface UserService {
    List<ExtUser> queryAll();

    PageResult page(UserPageQueryDTO userPageQueryDTO);

    String login(UserLoginDTO userLoginDTO);

    Result<UserVO> registry(UserRegistryDTO userRegistry);

    Result<String> logout(HttpServletRequest request);
}
