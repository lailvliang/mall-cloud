package com.central.oauth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author hzed
 * @date 2018/12/28
 */
public interface hzedUserDetailsService extends UserDetailsService {
    /**
     * 根据电话号码查询用户
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);
}
