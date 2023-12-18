package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.swing.text.StyledEditorKit;
import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemStartOrStopDTO implements Serializable {
    @ApiModelProperty(required = true)
    private  Long problemId;
    @ApiModelProperty(required = true)
    private Boolean is_deprecated;
}
