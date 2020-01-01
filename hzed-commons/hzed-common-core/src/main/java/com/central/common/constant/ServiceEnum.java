package com.central.common.constant;

import com.central.common.feign.UserService;

/**
 * 权限常量
 *
 * @author hzed
 * @date 2018/7/24 16:05
 */
public enum ServiceEnum {
    /**
     * MALL_ADMIN服务
     */
    MALL_ADMIN(1, UserService.class),
    /**
     * 普通用户
     */
    USER(2,UserService.class),
    /**
     * 匿名用户
     */
    FILE(3,UserService.class);

    private int type ;
    private Class serviceClass;

    ServiceEnum(int type,Class serviceClass) {
        this.type = type;
        this.serviceClass = serviceClass;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public static Class getServiceClassByType(int type){
        for(ServiceEnum serviceEnum : ServiceEnum.values()){
            if(serviceEnum.type == type){
                return serviceEnum.getServiceClass();
            }
        }
        return null;
    }
}
