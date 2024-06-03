package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.management.relation.Role;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Data
@TableName("user_space")
public class SpaceUser  implements Serializable {
    @TableId
    private Long userSpaceId;
    private Long userId;
    private Long spaceId;
    private Integer role;//1是最大

    //0是待接受 1是拒绝 2是接受
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableField("is_suspended")
    private Boolean isSuspended;
}
