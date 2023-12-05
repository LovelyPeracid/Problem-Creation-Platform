package com.lcl.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LovelyPeracid
 */
@Data
public class SpaceQueryDTO implements Serializable {
    private String  spaceName;
    private Long owner;
}
