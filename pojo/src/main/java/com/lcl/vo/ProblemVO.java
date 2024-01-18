package com.lcl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemVO implements Serializable {
    private Long problemId;
    private Long spaceId;
    private String title;
    private Boolean isDeprecated;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Long forkedFrom;
    private List<String> authors;

}
