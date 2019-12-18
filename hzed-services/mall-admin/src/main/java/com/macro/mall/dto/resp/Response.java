package com.macro.mall.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinxujun
 * @since 2019/5/16
 */
@Data
@NoArgsConstructor
public class Response<T> {
    private String code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response(String errorCode, String msg) {
        this.code = errorCode;
        this.msg = msg;
    }

    public Response(String errorCode, String msg, T data) {
        this.code = errorCode;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 判断响应结果是否成功
     *
     * @return 是否成功
     */
    @JsonIgnore
    public boolean isSuccess() {
        return ResponseCode.SUCCESS.equals(this.getCode());
    }

    public static Response getSuccessResponse() {
        return new Response(ResponseCode.SUCCESS, "SUCCESS");
    }

    public static <T> Response getSuccessResponse(T data) {
        return new Response(ResponseCode.SUCCESS, "SUCCESS", data);
    }

    public static Response getFailResponse(String msg) {
        return new Response(ResponseCode.FAIL, msg);
    }

    public static Response getFailResponse(String code, String msg) {
        return new Response(code, msg);
    }



}