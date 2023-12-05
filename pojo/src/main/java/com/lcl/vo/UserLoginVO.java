package com.lcl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    @ApiModelProperty("主键值")
    private Long user_id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("jwt令牌")
    private String token;

}
