package com.rocketmq.demo.service;

import com.rocketmq.demo.model.Order;

/**
* @author hzed
 */
public interface IOrderService {
    void save(Order order);
}
