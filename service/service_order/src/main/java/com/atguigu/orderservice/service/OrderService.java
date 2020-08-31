package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据课程id和会员id生成订单
     * @param courseId  课程id
     * @param membeId   会员id
     * @return          订单号
     */
    String saveOrder(String courseId, String memberId);

    /**
     * 查询课程是否被购买
     * @param courseId     课程id
     * @param memberId     用户id
     * @return             是否被购买
     */
    boolean isBought(String courseId, String memberId);
}
