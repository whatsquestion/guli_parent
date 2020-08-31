package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
public interface PayLogService extends IService<PayLog> {

    /**
     * 生成订单
     * @param orderNo  订单号
     * @return         封装支付信息的第三方数据
     */
    Map createNative(String orderNo);

    /**
     * 查询订单支付状态
     * @param orderNo  订单号
     * @return         封装支付信息的第三方数据
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 更新订单表状态和日志表
     * @param resultMap  封装支付信息的第三方数据
     */
    void updateOrderStatus(Map<String, String> resultMap);
}
