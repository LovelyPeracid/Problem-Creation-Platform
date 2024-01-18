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
@TableName("problem")
public class Problem implements Serializable {
    @TableId
    private Long problemId;
    private Long spaceId;
    private String title;
    @TableField("is_deprecated")
    private Boolean isDeprecated;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    @TableField("created_at")
    private LocalDateTime createdAt;
    private Long forkedFrom;
    @TableField("gitlab_id")
    private Long gitlabId;
    //private Long author;

}
