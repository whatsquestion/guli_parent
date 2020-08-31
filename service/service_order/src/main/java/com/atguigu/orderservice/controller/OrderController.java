package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成订单")
    @PostMapping("/createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据课程id和会员id生成订单
        String orderNo= orderService.saveOrder(courseId, memberId);

        return R.ok().data("orderNo", orderNo);
    }

    @ApiOperation(value = "根据订单id查询订单")
    @GetMapping("/getOrder/{orderNo}")
    public R getOrder(@PathVariable String orderNo){
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", orderNo));
        return R.ok().data("item", order);
    }

    @ApiOperation(value = "查询课程是否被购买")
    @GetMapping("/isBought/{courseId}/{memberId}")
    public boolean isBought(@PathVariable String courseId, @PathVariable String memberId){
        return orderService.isBought(courseId, memberId);
    }


}

