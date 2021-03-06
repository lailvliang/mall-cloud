package com.central.log.config;

import com.central.log.properties.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 日志自动配置
 *
 * @author hzed
 * @date 2019/8/13
 */
@EnableConfigurationProperties(TraceProperties.class)
public class LogAutoConfigure {

}
