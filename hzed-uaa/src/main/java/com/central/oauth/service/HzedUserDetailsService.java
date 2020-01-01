package com.central.oauth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.ServiceNotFoundException;

/**
 * @author hzed
 * @date 2018/12/28
 */
public interface HzedUserDetailsService extends UserDetailsService {
    /**
     * 根据电话号码查询用户
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);

    UserDetails loadUserByUserName(String userName,int type);
}
