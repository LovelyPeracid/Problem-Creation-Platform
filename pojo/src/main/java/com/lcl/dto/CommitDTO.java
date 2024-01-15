package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author LovelyPeracid
 */
@Data
public class CommitDTO {
    private List<ActionDTO> actions;
    @ApiModelProperty(value = "提交信息 ",required = false)
    private  String message;
}
