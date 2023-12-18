package com.lcl.mapper;

import com.lcl.annotation.AutoFill;
import com.lcl.entity.Problem;
import com.lcl.entity.SpaceProblem;
import com.lcl.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface SpaceProblemMapper {
    @AutoFill(OperationType.INSERT)
    @Insert("insert into  space_problem (space_problem_id, space_id, is_deleted, created_at, problem_id, updated_at, is_reference) values" +
            " (#{spaceProblemId}," +
            "#{spaceId},#{isDeleted},#{createdAt},#{problemId},#{updatedAt},#{isReference})")
    void save(SpaceProblem spaceProblem);

    void update(SpaceProblem problem);
    @Select("select * from space_problem where space_id=#{spaceId} and problem_id=#{problemId}")
    SpaceProblem getBySpaceIdAndProblemId(Problem problem);
}
