package com.central.common.model;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    RESOLVE_FAILED(400, "参数解析失败"),
    VALIDATE_FAILED(402, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    METHOD_NOT_ALLOWED(405, "不支持请求当前方法"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),
    SQL_EXCEPTION(500, "服务运行SQLException异常"),
    BUSINESS_EXCEPTION(500, "业务异常"),
    UNKNOWN_EXCEPTION(500, "业务异常"),
    FORBIDDEN(403, "没有相关权限");
    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
