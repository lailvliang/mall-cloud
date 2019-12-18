package com.macro.mall.service.impl;

import com.macro.mall.common.MathUtil;
import com.macro.mall.service.CalculatePeriodAmountService;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author
 */
@Service
public class CalculatePeriodAmountServiceImpl implements CalculatePeriodAmountService<BigDecimal,String,BigDecimal> {

    /**
     * 手续费份额,先默认,后面可以从数据库或配置文件获取
     */
    @Getter
    private static String scale = "0.01";

    @Override
    public BigDecimal calculatePerRepayAmount(BigDecimal skuPrice,String payPeriod) {
        return MathUtil.divOfTwoDecimals(skuPrice,payPeriod);
    }

    @Override
    public BigDecimal calculatePerCharge(BigDecimal skuPrice,String scale) {
        return MathUtil.mul(skuPrice,new BigDecimal(scale)).setScale(2,MathUtil.DEFAULT_ROUND_MODE);
    }
}
