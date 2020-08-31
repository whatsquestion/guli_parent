package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    /**
     * 发送短信验证码
     * @param codeMap  封装了验证码的map
     * @param phone     手机号
     * @return           是否发送成功
     */
    boolean sendMsg(Map<String, Object> codeMap, String phone);
}
