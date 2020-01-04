package com.central.common.feign;

import com.central.common.model.LoginAppUser;

public interface UserService {


    /**
     * 通过userName查找用户信息
     *
     * @param username
     * @return
     */
    LoginAppUser findByUsername(String username);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    LoginAppUser findByMobile(String mobile);

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    LoginAppUser findByOpenId(String openId);
}
