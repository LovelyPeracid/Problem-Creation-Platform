package com.lcl.mapper;

import com.github.pagehelper.Page;
import com.lcl.annotation.AutoFill;
import com.lcl.dto.ProblemPageQueryDTO;
import com.lcl.entity.Problem;
import com.lcl.enumeration.OperationType;
import com.lcl.vo.ProblemVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface ProblemMapper {


    Page<ProblemVO> page(ProblemPageQueryDTO problemPageQueryDTO);
    @Select("select * from problem where problem_id=#{problemId}")
    Problem getById(Long problemId);

    @Select("select * from problem where space_id=#{spaceId}")
    List<ProblemVO> getBySpaceId(Long spaceId);
    @AutoFill(OperationType.INSERT)
    @Insert("insert into problem (title,forked_from,created_at,updated_at,space_id,author,gitlab_id) values(#{title},#{forkedFrom},#{createdAt},#{updatedAt},#{spaceId},#{author},#{gitlabId})")
    void save(Problem problem);
    @Select("select *from problem where title=#{title}")
    Problem getByTitle(String title);
    @AutoFill(OperationType.UPDATE)
    void update(Problem problem);

    //void save(Problem problem);
}
