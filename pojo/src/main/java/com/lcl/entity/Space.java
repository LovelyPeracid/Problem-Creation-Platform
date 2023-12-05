package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.BooleanTypeHandler;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * @author LovelyPeracid
 */
@TableName("space")
@ToString
@Data
public class Space  implements Serializable {
    @TableId(type = AUTO)
    private  Long spaceId;
    private  String spaceName;
    private boolean type;
    @TableField("owner")
    private  Long owner;
    @TableField(value = "is_deleted",typeHandler = BooleanTypeHandler.class)
    private Boolean isDeleted;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    @TableField("created_at")
    private LocalDateTime createdAt;
}
