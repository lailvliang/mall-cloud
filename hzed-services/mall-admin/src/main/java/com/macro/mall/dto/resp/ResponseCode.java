package com.macro.mall.dto.resp;

/**
 * @author maijiaheng
 * @date 2019/7/11 15:27
 */
public class ResponseCode {
    public transient static final String SUCCESS = "0000";
    public transient static final String FAIL = "1111";
    /**
     *  余额不足
     */
    public transient static final String INSUFFICIENT_BALANCE = "1000";


    private ResponseCode() {
    }
}
