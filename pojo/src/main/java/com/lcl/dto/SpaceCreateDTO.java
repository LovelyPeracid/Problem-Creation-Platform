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
}
