package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
@ToString
@TableName("ext_user_info")
public class ExtUser  implements Serializable {

    private  String username;
    private  String  password;

    private LocalDateTime createdAt;
    private   LocalDateTime updatedAt;
    @TableId
    private   Long userId;

    private  String email;

    @TableField(value = "is_suspended")
    private  Boolean isSuspended;
    @TableField("is_admin")
    private  Boolean isAdmin;
}
