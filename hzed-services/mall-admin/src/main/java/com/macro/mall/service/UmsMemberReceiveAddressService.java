package com.macro.mall.service;

import com.macro.mall.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 用户地址管理Service
 * Created by macro on 2018/8/28.
 */
public interface UmsMemberReceiveAddressService {
    /**
     * 添加收货地址
     */
    int add(UmsMemberReceiveAddress address);

    /**
     * 删除收货地址
     * @param id 地址表的id
     * @param memberId 用户id
     */
    int delete(Long id, Long memberId);

    /**
     * 修改收货地址
     * @param id 地址表的id
     * @param address 修改的收货地址信息
     */
    int update(Long id, UmsMemberReceiveAddress address);

    /**
     * 返回当前用户的收货地址
     * @param memberId 用户id
     */
    List<UmsMemberReceiveAddress> list(Long memberId);

    /**
     * 获取地址详情
     * @param id 地址id
     * @param memberId 用户id
     */
    UmsMemberReceiveAddress getItem(Long id, Long memberId);
}
