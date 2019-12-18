package com.macro.mall.service;

import com.macro.mall.dto.PmsInstallmentPlantParam;
import com.macro.mall.dto.PortalProductDetail;
import com.macro.mall.dto.PmsInstallmentPlantDetail;

import java.util.List;

/**
 * @author
 */
public interface PortalProductService {

    /**
     * 根据商品编号获取商品详情
     * @param id
     * @return
     * @throws Exception
     */
    PortalProductDetail getProductDetail(Long id) throws Exception;

    /**
     * 获取分期计划详情
     * @param param
     * @return
     */
    List<PmsInstallmentPlantDetail> getPmsInstallmentPlantDetail(PmsInstallmentPlantParam param);
}
