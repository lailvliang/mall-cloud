package com.central.common.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 *
 * @author hzed
 * @date 2018/11/18 9:20
 */
@Configuration
@ConditionalOnProperty(name = "hzed.swagger.enabled", matchIfMissing = true)
@Import({
        Swagger2DocumentationConfiguration.class
})
public class Swagger2Configuration {
}
