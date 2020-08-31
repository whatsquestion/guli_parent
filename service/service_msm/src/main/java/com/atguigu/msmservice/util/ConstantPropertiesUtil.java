package com.atguigu.msmservice.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${aliyun.msm.keyid}")
    private String keyId;

    @Value("${aliyun.msm.keysecret}")
    private String keySecret;

    @Value("${aliyun.msm.signname}")
    private String signName;

    @Value("${aliyun.msm.templatecode}")
    private String templateCode;

    //定义常量，在属性设置完成时赋值
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String SIGN_NAME;
    public static String TEMPLATE_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        SIGN_NAME=signName;
        TEMPLATE_CODE= templateCode;
    }
}
