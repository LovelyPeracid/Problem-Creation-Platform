package com.lcl.mapper;

import com.lcl.entity.OperationRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LovelyPeracid
 */
@Mapper
public interface OperationRecordMapper {
    @Insert("INSERT INTO operation_record (operator, operation_at, user_id, ip, space_id, operation_type, user_agent, problem_id) VALUES (#{operator}, #{operationAt}, #{userId}, #{ip}, #{spaceId}, #{operationType}, #{userAgent}, #{problemId})")
    void save(OperationRecord operationRecord);



}
