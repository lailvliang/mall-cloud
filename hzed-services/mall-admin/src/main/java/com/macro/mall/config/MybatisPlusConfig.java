package com.macro.mall.config;

import com.central.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hzed
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.macro.mall.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {
}
