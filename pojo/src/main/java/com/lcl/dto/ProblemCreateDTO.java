package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemCreateDTO  implements Serializable {
    @ApiModelProperty(required = true,value = "题目")
    @Size(max = 64)
    private String title;
    @ApiModelProperty(required = false,value = "fork")
    private Long forkedFrom;
}
