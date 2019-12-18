package com.central.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.central.gateway.route.NacosRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态路由配置 nacos配置网关规则能同步路由到应用内存中
 *
 * @author hzed
 * @date 2019/10/7
 * <p>
 * Blog: https://blog.csdn.net/hzed2000
 * Github: https://github.com/hzed2000
 */
@Configuration
@ConditionalOnProperty(prefix = "hzed.gateway.dynamicRoute", name = "enabled", havingValue = "true")
public class DynamicRouteConfig {
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Nacos实现方式
     */
    @Configuration
    @ConditionalOnProperty(prefix = "hzed.gateway.dynamicRoute", name = "dataType", havingValue = "nacos", matchIfMissing = true)
    public class NacosDynRoute {
        @Autowired
        private NacosConfigProperties nacosConfigProperties;

        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(publisher, nacosConfigProperties);
        }
    }
}
