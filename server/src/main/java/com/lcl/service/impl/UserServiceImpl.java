package com.lcl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.RedisConstants;
import com.lcl.dto.UserLoginDTO;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.dto.user.UserRegistryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.enumeration.ErrorCode;
import com.lcl.exception.BaseException;
import com.lcl.exception.BusinessException;
import com.lcl.exception.LoginFailedException;
import com.lcl.mapper.UserMapper;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.UserService;
import com.lcl.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LovelyPeracid
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String passwd = userLoginDTO.getPassword();
        ExtUser user = userMapper.queryByPasswd(email,passwd);
        if(user==null){
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }
        String token = UUID.randomUUID().toString(true);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(user);
        Map<String, String> stringStringMap = new HashMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
            //System.out.println();
            stringStringMap.put(stringObjectEntry.getKey(),stringObjectEntry.getValue().toString());
        }
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY+token,stringStringMap);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY+token,10, TimeUnit.HOURS);
        return  token;
    }

    @Override
    public Result<UserVO> registry(UserRegistryDTO userRegistry) {
        String email = userRegistry.getEmail();
        String username = userRegistry.getUsername();
        String password = userRegistry.getPassword();
        if(StringUtils.isAnyBlank(email, username, password)){
            throw  new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        ExtUser user = new ExtUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        Long userId=null;
        synchronized (email.intern()){
           Long f= userMapper.getByEmail(email);
           if(f!=null){
               throw new BusinessException(MessageConstant.USER_EXITST);
           }
             userId= userMapper.insert(user);
            if(userId==null){
                throw  new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }
        UserVO userVO = UserVO.ObjToVO(user);
        return  Result.success(userVO);
    }

    @Override
    public Result<String> logout(HttpServletRequest request) {

        return null;
    }
}
