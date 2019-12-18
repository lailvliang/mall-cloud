package com.central.business.controller;


import com.central.business.service.BusinessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hzed
 * @date 2019/9/14
 */
@RestController
public class BusinessController {
    @Resource
    private BusinessService servicesService;

    /**
     * 下单场景测试-正常
     */
    @RequestMapping(path = "/placeOrder")
    public Boolean placeOrder() {
        servicesService.placeOrder("U001");
        return true;
    }

    /**
     * 下单场景测试-回滚
     */
    @RequestMapping(path = "/placeOrderFallBack")
    public Boolean placeOrderFallBack() {
        servicesService.placeOrder("U002");
        return true;
    }
}
