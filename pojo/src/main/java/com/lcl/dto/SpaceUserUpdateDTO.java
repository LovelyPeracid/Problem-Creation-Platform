package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceUserUpdateDTO implements Serializable {
    @ApiModelProperty(required = false)
    private Boolean is_suspended;
    @ApiModelProperty(required = false)
    private Integer role;
    @ApiModelProperty(required = true)
    private Long userId;
}
