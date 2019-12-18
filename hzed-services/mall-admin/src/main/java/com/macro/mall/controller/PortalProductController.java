package com.macro.mall.controller;

import com.macro.mall.dto.PmsInstallmentPlantParam;
import com.macro.mall.dto.PortalProductDetail;
import com.macro.mall.service.PortalProductService;
import com.macro.mall.common.CommonResult;
import com.macro.mall.dto.PmsInstallmentPlantDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author
 */
@RestController
@RequestMapping("/portalProduct")
@Api(tags = "PortalProductController", description = "APP前端商品管理")
@Slf4j
public class PortalProductController {

    @Autowired
    private PortalProductService portalProductService;

    @ApiOperation("获取商品明细")
    @RequestMapping(value = "/getProductDetailFromId", method = RequestMethod.GET)
    public CommonResult<PortalProductDetail> getProductDetailFromId(long id) throws Exception{
        PortalProductDetail portalProductDetail = portalProductService.getProductDetail(id);
        return CommonResult.success(portalProductDetail);
    }

    @ApiOperation("获取商品分期明细")
    @RequestMapping(value = "/getPmsInstallmentPlantDetail", method = RequestMethod.POST)
    public CommonResult<List<PmsInstallmentPlantDetail>> getPmsInstallmentPlantDetail(@Valid @RequestBody PmsInstallmentPlantParam pmsInstallmentPlantParam , BindingResult bindingResult){
        return CommonResult.success(portalProductService.getPmsInstallmentPlantDetail(pmsInstallmentPlantParam));
    }

}
