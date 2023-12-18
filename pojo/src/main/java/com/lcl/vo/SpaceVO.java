package com.lcl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceVO implements Serializable {
    private  Long spaceId;
    private  String spaceName;
    private boolean type;
    private  Long owner;
    private Boolean isDeleted;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
