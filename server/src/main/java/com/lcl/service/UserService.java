package com.lcl.service;

import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.result.PageResult;

import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface UserService {
    List<ExtUser> queryAll();

    PageResult page(UserPageQueryDTO userPageQueryDTO);
}
