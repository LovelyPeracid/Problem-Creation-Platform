package com.lcl.vo;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.lcl.entity.ExtUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author LovelyPeracid
 */
@Data
public class UserVO {
    private  Long userId;
    private String username;
    private String icon;
    public static UserVO ObjToVO(ExtUser user){
        if(user==null) return  null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return  userVO;
    }
}
