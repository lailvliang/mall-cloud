package com.macro.mall.service;

/**
 * @author
 * @param <T>
 * @param <U>
 * @param <R>
 */
public interface CalculatePeriodAmountService<T,U,R> {

    /**
     * 计算还款本金
     * @param price 商品金额
     * @param period 商品期数
     * @return  分期还款金额
     */
    R calculatePerRepayAmount(T price, U period);

    /**
     * 手续费
     * @param price 商品金额
     * @param scale 比例
     * @return  手续费
     */
    R calculatePerCharge(T price, U scale);
}
