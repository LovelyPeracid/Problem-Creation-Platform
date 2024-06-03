package com.lcl.enumeration;

import lombok.Getter;

/**
 * @author LovelyPeracid
 */
@Getter
public enum AddStatus {

    accept(2),
    wait(0),
    refuse(1);
    private final int status;
    AddStatus(int status) {
        this.status = status;
    }
}
