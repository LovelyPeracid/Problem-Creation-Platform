package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */

//@ToString
@TableName("problem_migration")
@Data
public class ProblemMigration implements Serializable {
    @TableId
    private  Long migrationId;
    private Long   prevSpaceId;
    private  Long  nextSpaceId;
    private LocalDateTime createdAt;
    private Long userId;
    private Long problemId;
}
