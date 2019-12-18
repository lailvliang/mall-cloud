package com.macro.mall.controller;

import com.macro.mall.domain.MallHomeDetail;
import com.macro.mall.service.PmsProductService;
import com.macro.mall.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商城首页controller
 * @author madaijun
 * @version 1.0
 * @date 2019\11\14 0014 11:49
 */
@Controller
@Api(tags = "MallHomeController", description = "商城首页内容管理")
@RequestMapping("/portal/mallHome")
public class MallHomeController {
    @Autowired
    private PmsProductService pmsProductService;

    @ApiOperation("首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MallHomeDetail> content() {
        return pmsProductService.getMallHomeContent();
    }

}
