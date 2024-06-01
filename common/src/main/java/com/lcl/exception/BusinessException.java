package com.lcl.exception;

import com.lcl.enumeration.ErrorCode;
import lombok.Getter;

/**
 * @author LovelyPeracid
 */
@Getter
public class BusinessException extends RuntimeException {
    private int code;
   // private String msg;
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMsg());
        this.code=errorCode.getCode();
      //  this.msg=errorCode.getMsg();

    }
    public BusinessException(String msg){
        super(msg);
        this.code=405;

    }
}
