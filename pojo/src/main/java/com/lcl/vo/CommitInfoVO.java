package com.lcl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author LovelyPeracid
 */
@Data
public class CommitInfoVO {
    private String commitId;
    private String message;
    @ApiModelProperty("这个目前是调用的API获取的 是date类型 可以")
    private Date updatedAt;
}
