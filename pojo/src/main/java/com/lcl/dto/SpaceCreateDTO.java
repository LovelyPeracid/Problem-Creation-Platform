package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceCreateDTO implements Serializable {
    @Size(max = 64)
    @ApiModelProperty(required = true )
    private  String spaceName;
    @ApiModelProperty(value = "空间类型，1表示个人空间 0是公共空间", required = true)
    private boolean type;//1是个人空间
    @ApiModelProperty(value ="拥有者" ,required = true )
    private Long owner; //
}
