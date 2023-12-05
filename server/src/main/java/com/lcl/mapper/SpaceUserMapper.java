package com.lcl.mapper;

import com.lcl.annotation.AutoFill;
import com.lcl.entity.SpaceUser;
import com.lcl.enumeration.OperationType;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface SpaceUserMapper  {
    @AutoFill(OperationType.INSERT)
    @Insert("INSERT INTO user_space (user_id, space_id, role, created_at, updated_at, is_suspended) VALUES (#{userId}, #{spaceId}, #{role}, #{createdAt}, #{updatedAt}, #{isSuspended})")
   void addMember(SpaceUser spaceUser) ;


    @Select("select *from user_space where space_id=#{spaceId} and user_id=#{UserId}")
    SpaceUser getByUserId(Long spaceId,Long UserId );
    @AutoFill(OperationType.INSERT)
    void update(SpaceUser spaceUser);
}
