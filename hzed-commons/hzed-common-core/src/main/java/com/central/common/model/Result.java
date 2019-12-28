package com.central.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: hzed
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private T datas;
    private Integer resp_code;
    private String resp_msg;


    public static <T> Result<T> succeed() {
        return newResultWith(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    public static <T> Result<T> succeedWithMsg(String msg) {
        return newResultWith(null, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeedWithDatasAndMsg(T datas, String msg) {
        return newResultWith(datas, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeedWithDatas(T datas) {
        return newResultWith(datas, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }


    public static <T> Result<T> failedWithMsgAndIErrorCode(String msg,IErrorCode error) {
        return newResultWith(null, error.getCode(), msg);
    }

    public static <T> Result<T> failedWithIErrorCode(IErrorCode error) {
        return newResultWith(null, error.getCode(), error.getMessage());
    }

    public static <T> Result<T> failedWithDatasAndIErrorCode(T datas, IErrorCode error) {
        return newResultWith(datas, error.getCode(), error.getMessage());
    }


    private static <T> Result<T> newResultWith(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }

}
