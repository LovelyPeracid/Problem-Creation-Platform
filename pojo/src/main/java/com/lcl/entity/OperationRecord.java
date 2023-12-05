package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * @author LovelyPeracid
 */
@TableName("operation_record")
@Data
@ToString
public class OperationRecord  implements Serializable {
    @TableId(type = AUTO)
    private  Long operationRecordId;
    private  Long operator;
    private LocalDateTime operationAt;
    private Long userId;
    private String ip;
    private  Long SpaceId;
    private String operationType;
    private  String userAgent;
    private  Long problemId;
}
