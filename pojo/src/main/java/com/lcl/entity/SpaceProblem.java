package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
@ToString
@TableName("space_problem")
public class SpaceProblem implements Serializable {
    @TableId
    private  Long spaceProblemId;
    private Long spaceId;
    @TableField("is_deleted")
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private Long problemId;
    private LocalDateTime updatedAt;
    @TableField("is_reference")
    private Boolean isReference;
}
