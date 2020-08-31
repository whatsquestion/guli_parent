package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.constant.OrderConstant;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.util.HttpClient;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-27
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map createNative(String orderNo) {

        //根据orderNo查询订单
        QueryWrapper<Order> wrapper= new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        //设置支付参数
        Map paramMap= new HashMap();
        paramMap.put("appid", OrderConstant.APP_ID);
        paramMap.put("mch_id", OrderConstant.MCH_ID);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", order.getCourseTitle());
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
        paramMap.put("trade_type", "NATIVE");

        try {
            //httpclient调用第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置client参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, OrderConstant.PARTNER_KEY));
            client.setHttps(true);
            client.post();
            //返回第三方数据
            String xmlContent = client.getContent();
            Map<String, String> contentMap = WXPayUtil.xmlToMap(xmlContent);
            //封装返回结果
            Map resultMap= new HashMap();
            resultMap.put("out_trade_no", orderNo);
            resultMap.put("course_id", order.getCourseId());
            resultMap.put("total_fee", order.getTotalFee());
            resultMap.put("result_code", contentMap.get("result_code"));
            resultMap.put("code_url", contentMap.get("code_url"));

            //微信支付二维码15分钟过期，可采取15分钟未支付取消订单
            redisTemplate.opsForValue().set(orderNo, resultMap, 15, TimeUnit.MINUTES);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"调用httpClient接口出错！");
        }

    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        //封装参数
        Map paramMap= new HashMap();
        paramMap.put("appid", OrderConstant.APP_ID);
        paramMap.put("mch_id", OrderConstant.MCH_ID);
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            //设置请求
            HttpClient client= new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, OrderConstant.PARTNER_KEY));
            client.setHttps(true);
            client.post();
            //返回第三方数据
            String xmlContent = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlContent);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "查询支付状态异常!");
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> resultMap) {
        //获取订单id
        String orderNo = resultMap.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        if(OrderConstant.PAY_STATUS_YES.equals(order.getStatus())) {
            //如果订单状态未已支付，则不需要更新
            return;
        }
        order.setStatus(OrderConstant.PAY_STATUS_YES);
        orderService.updateById(order);

        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(OrderConstant.PAY_STATUS_YES);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(resultMap.get("trade_state"));//支付状态
        payLog.setTransactionId(resultMap.get("transaction_id")); //流水号
        payLog.setAttr(JSONObject.toJSONString(resultMap));   //resultmap中的所有属性
        //插入到支付日志表
        baseMapper.insert(payLog);
    }

}
