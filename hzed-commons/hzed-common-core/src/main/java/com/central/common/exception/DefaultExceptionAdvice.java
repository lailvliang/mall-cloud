package com.central.common.exception;

import com.central.common.model.CommonResult;
import com.central.common.model.IErrorCode;
import com.central.common.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * 异常通用处理
 *
 * @author hzed
 */
@ResponseBody
@Slf4j
public class DefaultExceptionAdvice {
    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public CommonResult badRequestException(IllegalArgumentException e) {
        return defHandler(ResultCode.RESOLVE_FAILED, e);
    }

    /**
     * AccessDeniedException异常处理返回json
     * 返回状态码:403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public CommonResult badMethodExpressException(AccessDeniedException e) {
        return defHandler(ResultCode.FORBIDDEN, e);
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public CommonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return defHandler(ResultCode.METHOD_NOT_ALLOWED, e);
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public CommonResult handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return defHandler(ResultCode.UNSUPPORTED_MEDIA_TYPE, e);
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public CommonResult handleSQLException(SQLException e) {
        return defHandler(ResultCode.SQL_EXCEPTION, e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public CommonResult handleException(BusinessException e) {
        return defHandler(ResultCode.BUSINESS_EXCEPTION, e);
    }

    /**
     * IdempotencyException 幂等性异常
     * 返回状态码:200
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IdempotencyException.class)
    public CommonResult handleException(IdempotencyException e) {
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 所有异常统一处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e) {
        return defHandler(ResultCode.UNKNOWN_EXCEPTION, e);
    }

    private CommonResult defHandler(IErrorCode errorCode, Exception e) {
        log.error(errorCode.getMessage(), e);
        return CommonResult.failed(errorCode);
    }
}
