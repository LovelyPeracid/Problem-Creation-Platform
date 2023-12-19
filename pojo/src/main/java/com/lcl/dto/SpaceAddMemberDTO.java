package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data

public class SpaceAddMemberDTO implements Serializable {
//    @ApiModelProperty(required = true)
//    private  Long userId;
    @ApiModelProperty(required = true,value = "默认为4是测题人员")
    private  Integer role;
}
