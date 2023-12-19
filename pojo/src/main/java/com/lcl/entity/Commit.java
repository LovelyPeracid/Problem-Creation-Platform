package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
@TableName("commit")
public class Commit implements Serializable {
    private  Long commitId;
    private Long problemId;
    private Long author;
    private String short_id;
    private Long problem_gitlab_id;
    private LocalDateTime createdAt;
    private String describe;
}
