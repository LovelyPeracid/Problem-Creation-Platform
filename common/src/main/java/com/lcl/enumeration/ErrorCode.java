package com.lcl.enumeration;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorCode {
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    API_REQUEST_ERROR(50010, "接口调用失败"),
    DATABASE_ERROR(500012,"数据库访问失败");

    private  final  int code;
    private  final String msg;
    ErrorCode(int code,String msg) {
        this.code=code;
        this.msg=msg;
    }


}
