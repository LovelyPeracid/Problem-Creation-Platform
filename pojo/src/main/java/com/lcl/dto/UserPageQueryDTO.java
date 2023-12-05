package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class UserPageQueryDTO implements Serializable {
    @ApiModelProperty(value = "模糊查询用户名时使用")
    private  String username;
    @ApiModelProperty(required = true)
    private int page;
    @ApiModelProperty(required = true)
    //每页显示记录数
    private int pageSize;

}
