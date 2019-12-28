package com.central.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String msg;
    private T datas;


    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param datas 获取的数据
     */
    public static <T> CommonResult<T> success(T datas) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), datas);
    }

    /**
     * 成功返回结果
     *
     * @param msg 提示信息
     */
    public static <T> CommonResult<T> success(String msg) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), msg, null);
    }

    /**
     * 成功返回结果
     *@param code 提示信息
     * @param msge 提示信息
     */
    public static <T> CommonResult<T> success(Integer code,String msge) {
        return new CommonResult<T>(code, msge, null);
    }

    /**
     * 成功返回结果
     *
     * @param datas 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResult<T> success(T datas, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, datas);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T datas) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), datas);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T datas) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), datas);
    }




}
