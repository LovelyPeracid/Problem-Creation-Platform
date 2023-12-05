package com.lcl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.mapper.UserMapper;
import com.lcl.result.PageResult;
import com.lcl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<ExtUser> queryAll() {
        List<ExtUser> list = userMapper.queryAll();
        return  list;
    }

    @Override
    public PageResult page(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<ExtUser> page=userMapper.page(userPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult()) ;
    }
}
