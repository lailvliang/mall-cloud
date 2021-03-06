package com.central.common.config;

import com.central.common.feign.UserCenterService;
import com.central.common.feign.UserService;
import com.central.common.resolver.ClientArgumentResolver;
import com.central.common.resolver.TokenArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.Map;

/**
 * 默认SpringMVC拦截器
 *
 * @author hzed
 * @date 2019/8/5
 */
public class DefaultWebMvcConfig extends WebMvcConfigurationSupport {
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

	/**
	 * 设置资源文件目录
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/resources/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/public/");
		super.addResourceHandlers(registry);
	}
}
