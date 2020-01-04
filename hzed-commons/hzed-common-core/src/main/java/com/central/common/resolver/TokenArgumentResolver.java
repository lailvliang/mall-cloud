package com.central.common.resolver;

import cn.hutool.core.util.StrUtil;
import com.central.common.annotation.LoginUser;
import com.central.common.constant.SecurityConstants;
import com.central.common.constant.ServiceEnum;
import com.central.common.exception.ServiceNotFoundException;
import com.central.common.feign.UserCenterService;
import com.central.common.feign.UserService;
import com.central.common.model.SysRole;
import com.central.common.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Token转化SysUser
 *
 * @author hzed
 * @date 2018/12/21
 */
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private Map<String, UserService> userServices;

    public TokenArgumentResolver(Map<String, UserService> userCenterService) {
        this.userServices = userCenterService;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) && methodParameter.getParameterType().equals(SysUser.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        LoginUser loginUser = methodParameter.getParameterAnnotation(LoginUser.class);
        boolean isFull = loginUser.isFull();
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        String roles = request.getHeader(SecurityConstants.ROLE_HEADER);
        String serivcetype = request.getHeader(SecurityConstants.SERVICE_TYPE_HEADER);

        Class serviceClass = ServiceEnum.getServiceClassByType(Integer.valueOf(serivcetype).intValue());
        UserService userService = userServices.get(serviceClass.getName());
        if(serviceClass == null){
            log.error("未找到可用的微服务");
            return null;
        }
        if (StrUtil.isBlank(username)) {
            log.warn("resolveArgument error username is empty");
            return null;
        }
        SysUser user;
        if (isFull) {
            user = userService.findByUsername(username);
        } else {
            user = new SysUser();
            user.setId(Long.valueOf(userId));
            user.setUsername(username);
        }
        List<SysRole> sysRoleList = new ArrayList<>();
        Arrays.stream(roles.split(",")).forEach(role -> {
            SysRole sysRole = new SysRole();
            sysRole.setCode(role);
            sysRoleList.add(sysRole);
        });
        user.setRoles(sysRoleList);
        return user;
    }
}
