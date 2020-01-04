package com.central.oauth.service.impl;

import com.central.common.exception.ServiceNotFoundException;
import com.central.common.feign.UserCenterService;
import com.central.common.feign.UserService;
import com.central.oauth.service.HzedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.central.common.model.LoginAppUser;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author hzed
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements HzedUserDetailsService{

    @Autowired
    private Map<String,UserService> userServices;

    @Override
    public UserDetails loadUserByUserId(String openId,String serviceName) {
        UserService userService = getService(serviceName);
        LoginAppUser loginAppUser = userService.findByOpenId(openId);
        return checkUser(loginAppUser);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile,String serviceName) {
        UserService userService = getService(serviceName);
        LoginAppUser loginAppUser = userService.findByMobile(mobile);
        return checkUser(loginAppUser);
    }

    @Override
    public UserDetails loadUserByUserName(String userName, String serviceName)  {
        UserService userService = getService(serviceName);
        LoginAppUser loginAppUser = userService.findByUsername(userName);
        if (loginAppUser == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }
        return checkUser(loginAppUser);
    }

    private LoginAppUser checkUser(LoginAppUser loginAppUser) {
        if (loginAppUser != null && !loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }

    private UserService getService(String serviceName){
        UserService userService = userServices.get(serviceName);
        if(userService == null){
            throw new ServiceNotFoundException("未找到可用服务！");
        }
        return userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LoginAppUser loginAppUser = null;
        if (loginAppUser == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }
        return checkUser(loginAppUser);
    }
}
