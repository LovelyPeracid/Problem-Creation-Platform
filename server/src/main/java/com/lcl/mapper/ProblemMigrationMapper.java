package com.lcl.mapper;

import com.lcl.annotation.AutoFill;
import com.lcl.entity.ProblemMigration;
import com.lcl.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface ProblemMigrationMapper {
    //@AutoFill(OperationType.INSERT)
    //@Insert("insert into  problem_migration (prev_space_id, next_space_id, created_at, user_id, problem_id) values(#{prveSpaceId},#{nextSpaceId},#{createdAt},#{userId},#{problemId}) ")
    @Insert("INSERT INTO problem_migration (prev_space_id, next_space_id, created_at, user_id, problem_id) VALUES (#{prevSpaceId}, #{nextSpaceId}, #{createdAt}, #{userId}, #{problemId})")
    void save(ProblemMigration problemMigration);
}
