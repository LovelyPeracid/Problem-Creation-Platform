package com.lcl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemInfoVO {
    @ApiModelProperty("最近一次提交commitId")
    private  String commitId;
    private  String message;
    private LocalDateTime updatedAt;
}
