package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseInfoVo;
import com.atguigu.commonutils.vo.MemberInfoVo;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UCenterClient;
import com.atguigu.orderservice.constant.OrderConstant;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.util.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UCenterClient uCenterClient;

    @Override
    public String saveOrder(String courseId, String memberId) {

        CourseInfoVo courseInfoVo = eduClient.getCourseInfo(courseId);
        MemberInfoVo memberInfoVo = uCenterClient.getMemberInfo(memberId);

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoVo.getTitle());
        order.setCourseCover(courseInfoVo.getCover());
        order.setTeacherName(courseInfoVo.getTeacherName());
        order.setTotalFee(courseInfoVo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberInfoVo.getMobile());
        order.setNickname(memberInfoVo.getNickname());
        //设置订单状态未支付
        order.setStatus(OrderConstant.PAY_STATUS_NO);
        //设置支付类型为 微信 支付
        order.setPayType(OrderConstant.PAY_TYPE_WX);
        baseMapper.insert(order);

        return order.getOrderNo();
    }

    @Override
    public boolean isBought(String courseId, String memberId) {
        QueryWrapper<Order> wrapper= new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", OrderConstant.PAY_STATUS_YES);
        int count = this.count(wrapper);
        return count>0;
    }
}
