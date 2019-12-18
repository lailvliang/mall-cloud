package com.macro.mall.dto.resp;

import lombok.Data;

/**
 * @author madaijun
 * @version 1.0
 * @date 2019\11\16 0016 10:01
 */
@Data
public class QmOrderResponse {
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应内容
     */
    private String message;
}
