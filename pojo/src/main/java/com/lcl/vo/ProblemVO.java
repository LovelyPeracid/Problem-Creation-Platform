package com.lcl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemVO implements Serializable {
    @TableId
    private Long problemId;
    private Long spaceId;
    private String title;
    @TableField("is_deprecated")
    private Boolean isDeprecated;
    private Long forkedFrom;
    private Long author;
}
