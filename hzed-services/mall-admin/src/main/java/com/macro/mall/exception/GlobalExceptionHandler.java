package com.macro.mall.exception;

import com.macro.mall.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *
 * @author dsy on 2019/11/28
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 上传文件大小超限
     * @param ex
     * @return
     */
    @ExceptionHandler({MaxUploadSizeExceededException.class , FileUploadBase.FileSizeLimitExceededException.class})
    public Object maxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.error("上传文件过大:{}",ex.getMessage());
        return CommonResult.failed("上传文件过大");
    }

    @ExceptionHandler({MallFileUploadException.class })
    public Object fileUploadException(MallFileUploadException ex) {
        log.error("上传文件异常:{}",ex.getMessage());
        return CommonResult.failed("上传文件异常："+ex.getMessage());
    }
}
