package com.lcl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
@TableName("authors")
public class Authors implements Serializable {
    private Long  authorsId;
    private Long problemId;
    private Long userId;
}
