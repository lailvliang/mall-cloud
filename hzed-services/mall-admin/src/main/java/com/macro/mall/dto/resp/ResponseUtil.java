package com.macro.mall.dto.resp;

/**
 * @author maijiaheng
 * @date 2019/7/11 15:32
 */
public class ResponseUtil {
    public static Response generateSuccessResponse() {
        return new Response(ResponseCode.SUCCESS, "成功");
    }

    public static <T> Response<T> generateSuccessResponse(T payload) {
        return new Response(ResponseCode.SUCCESS, "成功", payload);
    }

    private ResponseUtil() {
    }
}
