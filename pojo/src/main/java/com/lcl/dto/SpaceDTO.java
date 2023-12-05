package com.lcl.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceDTO implements Serializable {
    @Size(max=64)
    private  String spaceName;
    private Boolean type;//1是个人空间
    private Long owner; //
}
