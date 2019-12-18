package com.macro.mall.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author madaijun
 * @version 1.0
 * @date 2019\11\16 0016 9:41
 */
@Data
@Configuration
public class QmWalletAppConfig {
    /**
     * 全民钱包app购物借款订单请求url
     */
    @Value("${mall.qmwalletApp.orderReceivUrl}")
    private String orderReceivUrl;
}
