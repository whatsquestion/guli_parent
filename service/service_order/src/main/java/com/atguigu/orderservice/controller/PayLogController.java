package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.constant.OrderConstant;
import com.atguigu.orderservice.service.PayLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map= payLogService.createNative(orderNo);
        System.out.println("【返回的二维码信息】"+map);
        return R.ok().data(map);
    }

    @ApiOperation(value = "查询支付状态并修改")
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String, String> resultMap= payLogService.queryPayStatus(orderNo);
        System.out.println("【返回的支付信息】"+resultMap);
        if (resultMap==null){
            return R.error().message("支付失败");
        }
        if (!OrderConstant.PAY_SUCCESS.equals(resultMap.get("trade_state"))){
            return R.ok().code(25000).message("支付中...");
        }
        payLogService.updateOrderStatus(resultMap);
        return R.ok().message("支付成功");
    }

}

