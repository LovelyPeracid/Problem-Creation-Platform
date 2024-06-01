package com.lcl.mapper;

import com.github.pagehelper.Page;
import com.lcl.annotation.AutoFill;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.ExtUser;
import com.lcl.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Select("select * from ext_user_info where email=#{email} and password=#{passwd}")
    ExtUser queryByPasswd(String email, String passwd);

    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    @Insert("insert into  ext_user_info (email,username,password,created_at,updated_at) values (#{email},#{username},#{password},#{updatedAt},#{createdAt})")
    Long insert(ExtUser user);

    @Select("select user_id from ext_user_info where email=#{email}")
    Long getByEmail(String email);
//    @Select("select  *from  ext_user_info")
//    ExtUser queryAll();
}
