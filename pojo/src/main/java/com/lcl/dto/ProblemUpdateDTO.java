package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemUpdateDTO  implements Serializable {
    private  Long problemId;
    @ApiModelProperty(required = false)
    private String title;
    @ApiModelProperty(required = false)
    private Long spaceId;
}
