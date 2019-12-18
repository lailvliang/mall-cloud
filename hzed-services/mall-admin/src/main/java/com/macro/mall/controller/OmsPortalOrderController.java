package com.macro.mall.controller;


import com.macro.mall.domain.OrderParam;
import com.macro.mall.service.OmsPortalOrderService;
import com.macro.mall.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理Controller
 * Created by macro on 2018/8/30.
 */
@Controller
@Api(tags = "OmsPortalOrderController",description = "商城订单管理")
@RequestMapping("/portal/order")
@Slf4j
public class OmsPortalOrderController {
    @Autowired
    private OmsPortalOrderService portalOrderService;

    @ApiOperation("根据支付数据信息生成订单")
    @RequestMapping(value = "/generateOrder",method = RequestMethod.POST)
    @ResponseBody
    public Object generateOrder(@RequestBody OrderParam orderParam){
        try {
            return portalOrderService.generateOrder(orderParam);
        } catch (Exception e) {
            log.error("############## 根据支付数据信息生成订单失败: {}", e.getMessage());
            return CommonResult.failed("下单失败");
        }
    }

    @ApiOperation("支付成功的回调")
    @RequestMapping(value = "/paySuccess",method = RequestMethod.POST)
    @ResponseBody
    public Object paySuccess(@RequestParam Long orderId){
        return portalOrderService.paySuccess(orderId);
    }

}
