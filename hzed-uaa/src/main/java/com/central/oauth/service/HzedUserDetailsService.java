package com.central.oauth.service;

import com.central.common.model.LoginAppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.ServiceNotFoundException;

/**
 * @author hzed
 * @date 2018/12/28
 */
public interface HzedUserDetailsService extends UserDetailsService{
    /**
     * 根据电话号码查询用户
     *
     * @param mobile
     * @return
     */
    LoginAppUser loadUserByMobile(String mobile, String serviceName);

    LoginAppUser loadUserByUserName(String userName,String serviceName);

    LoginAppUser loadUserByUserId(String userId,String serviceName);
}
