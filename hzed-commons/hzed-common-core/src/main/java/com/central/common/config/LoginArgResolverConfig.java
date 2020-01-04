package com.central.common.config;

import com.central.common.feign.UserCenterService;
import com.central.common.feign.UserService;
import com.central.common.resolver.ClientArgumentResolver;
import com.central.common.resolver.TokenArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * 公共配置类, 一些公共工具配置
 *
 * @author hzed
 * @date 2018/8/25
 */
public class LoginArgResolverConfig implements WebMvcConfigurer {
    @Lazy
    @Autowired
    private Map<String, UserService> userServices;
    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new TokenArgumentResolver(userServices));
        //注入应用信息
        argumentResolvers.add(new ClientArgumentResolver());
    }
}
