package com.lcl.vo;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lcl.entity.Space;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.springframework.beans.BeanUtils;

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
    private Boolean isDeleted;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    public  static  SpaceVO ObjToVo(Space space){
        SpaceVO spaceVO = new SpaceVO();
        BeanUtils.copyProperties(space,spaceVO);
        return spaceVO;
    }
}
