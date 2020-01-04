package com.central.common.constant;

import com.central.common.exception.ServiceNotFoundException;
import com.central.common.feign.MallUserService;
import com.central.common.feign.UserCenterService;

/**
 * 服务枚举
 *
 * @author hzed
 * @date 2018/7/24 16:05
 */
public enum ServiceEnum {
    /**
     * 普通用户
     */
    MALL_ADMIN(0, MallUserService.class),
    /**
     * MALL_ADMIN服务
     */
    USER_CENTER(1, UserCenterService.class);

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
        throw new ServiceNotFoundException("未找到可用服务！");
    }
}
