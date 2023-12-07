package com.lcl.mapper;

import com.github.pagehelper.Page;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.ExtUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface UserMapper {
    @Select("select  *from  ext_user_info")
    List<ExtUser> queryAll();

    Page<ExtUser> page(UserPageQueryDTO userPageQueryDTO);
    @Select("select * from ext_user_info where user_id =#{currendId}")
    ExtUser getById(Long currentId);
//    @Select("select  *from  ext_user_info")
//    ExtUser queryAll();
}
