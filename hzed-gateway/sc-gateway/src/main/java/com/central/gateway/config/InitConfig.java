package com.central.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitConfig {

    static {
        //init GatewayBlockHandler
        GatewayCallbackManager.setBlockHandler(new GatewayBlockHandler());
    }
}
