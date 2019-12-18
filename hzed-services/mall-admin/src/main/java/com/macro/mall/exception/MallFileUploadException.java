package com.macro.mall.exception;

/**
 * 上传文件异常
 * @author dsy on 2019/11/28
 */
public class MallFileUploadException extends RuntimeException{
    public MallFileUploadException() {
        super("上传文件异常");
    }

    public MallFileUploadException(String message) {
        super(message);
    }

    public MallFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MallFileUploadException(Throwable cause) {
        super(cause);
    }

    protected MallFileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
