package com.lcl.mapper;

import com.lcl.annotation.AutoFill;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.enumeration.OperationType;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface SpaceUserMapper  {
    @AutoFill(OperationType.INSERT)
    @Insert("INSERT INTO user_space (user_id, space_id, role, created_at, updated_at, is_suspended) VALUES (#{userId}, #{spaceId}, #{role}, #{createdAt}, #{updatedAt}, #{isSuspended})")
   void addMember(SpaceUser spaceUser) ;


    @Select("select *from user_space where space_id=#{spaceId} and user_id=#{userId}")
    SpaceUser getByUserIdAndSpace(Long spaceId,Long userId );
    @AutoFill(OperationType.INSERT)
    void update(SpaceUser spaceUser);

    @Select("select space.* from space inner  join user_space on space.space_id=user_space.space_id where user_space.user_id=#{userId}")
    List<Space> getByUserId(Long userId);
}
