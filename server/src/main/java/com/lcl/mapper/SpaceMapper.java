package com.lcl.mapper;

import com.lcl.annotation.AutoFill;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;
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
public interface SpaceMapper {

    @Select("select  * from  space where space_id=#{id}")
    Space getById(Long id);


    List<Space> getByIds(List<Long> ids);
    @Select("select  * from space where space_name =#{spaceName}")
    Space getByName(String spaceName);
    @AutoFill(OperationType.INSERT)
    @Insert("insert into  space(space_name, updated_at, created_at, is_deleted,gitlab_id)"
        +"values (#{spaceName},#{updatedAt},#{createdAt},#{isDeleted},#{gitlabId})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "spaceId")
    Boolean save(Space space);
    @AutoFill(OperationType.UPDATE)
    void update(Space space);
    @Select("select space_id from space where owner=#{userId} and type=1")
    Long getPrivateSpaceByUserId(Long userId);
}
