package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceUpdateDTO implements Serializable {
    @Size(max=64)
    private String spaceName;
    //@ApiModelProperty()
    //private Long owner;
//    private boolean type;
    @ApiModelProperty(required = true,value = "剩下字段更改那个传那个")
    private Long spaceId;
}
