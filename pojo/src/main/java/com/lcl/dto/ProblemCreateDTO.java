package com.lcl.dto;

import lombok.Data;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class ProblemCreateDTO  implements Serializable {
    private String title;

}
