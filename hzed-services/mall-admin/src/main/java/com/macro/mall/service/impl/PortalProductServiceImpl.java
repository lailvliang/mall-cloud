package com.macro.mall.service.impl;

import com.macro.mall.dto.PmsInstallmentPlantParam;
import com.macro.mall.dto.PortalProductDetail;
import com.macro.mall.common.MathUtil;
import com.macro.mall.dao.PmsProductDao;
import com.macro.mall.dto.PmsInstallmentPlantDetail;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.service.PmsSkuStockService;
import com.macro.mall.service.PortalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class PortalProductServiceImpl implements PortalProductService {

    @Autowired
    private PmsProductDao pmsProductDao;

    @Autowired
    private PmsSkuStockService pmsSkuStockService;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    /**
     * 手续费费率 本金/100
     */
    public static final BigDecimal SERVICE_CHARGE_DIVISOR= new BigDecimal(100);

    @Override
    public PortalProductDetail getProductDetail(Long id) throws Exception{
        PortalProductDetail portalProductDetail = pmsProductDao.getPortalProductDetail(id);
        //将数据库查询出的原始状态转换成前端需要的数据类型
        if(portalProductDetail != null){
            portalProductDetail.transformTypes().transformSkuProducts();
        }
        return portalProductDetail;
    }

    @Override
    public List<PmsInstallmentPlantDetail> getPmsInstallmentPlantDetail(PmsInstallmentPlantParam param) {
        if(Objects.isNull(param)){
            throw  new NullPointerException("param PmsInstallmentPlantParam can not be null");
        }
        String price = getProductPrice(param);
        // ******** 分期计算逻辑 ********
        // 期数
        Integer periods = param.getPeriods();
        //总本金
        BigDecimal principal = new BigDecimal(price);
        // ***每期本金
        BigDecimal eachPeridPrincipal = MathUtil.div(principal,new BigDecimal(periods),2);
        // ***每期手续费
        BigDecimal eachPeridServiceCharge = MathUtil.div(principal,SERVICE_CHARGE_DIVISOR ,2);
        //返回结果
        List<PmsInstallmentPlantDetail> planList = new ArrayList<>();
        for (int i = 1; i <= periods; i++) {
            PmsInstallmentPlantDetail detail = PmsInstallmentPlantDetail.builder().curPeriod(i).serviceCharge(eachPeridServiceCharge).build();
            if(i<periods){
                //非最后一期
                detail.setPrincipal(eachPeridPrincipal);
            }else if(i==periods){
                //最后一期。本金减去非最后一期的本金累加
                //非最后一期的本金累加
                BigDecimal curTotalAmt = planList.stream().map(e->e.getPrincipal()).reduce(BigDecimal.ZERO, BigDecimal::add);
                detail.setPrincipal(principal.subtract(curTotalAmt));
            }
            //月供：本金 + 手续费
            detail.setMonthPay(detail.getPrincipal().add(eachPeridServiceCharge));
            planList.add(detail);
        }
        return planList;
    }

    /**
     * 获取商品价格价格
     * @param param
     * @return
     */
    private String getProductPrice(PmsInstallmentPlantParam param) {
        String price = param.getProductPrice();
        //没有价格时，从数据库取
        if(StringUtils.isEmpty(price)){
            PmsSkuStock sku = pmsSkuStockService.find(param.getSkuCode(),param.getProductId());
            if(Objects.nonNull(sku)){
                price = sku.getPrice().toString();
            }else{
                //获取商品价格
                PmsProduct product = pmsProductMapper.selectByPrimaryKey(param.getProductId());
                price = product.getPrice().toString();
            }
        }
        return price;
    }

//    public static void main(String[] args) {
//        PmsInstallmentPlantParam param = new PmsInstallmentPlantParam();
//        param.setProductPrice("1799");
//        param.setPeriods(12);
//        PortalProductServiceImpl p = new PortalProductServiceImpl();
//        List list = p.getPmsInstallmentPlantDetail(param);
//        System.out.println(list);
//    }
}
