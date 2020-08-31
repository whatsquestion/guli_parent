package com.atguigu.serviceoss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//常量类，读取application.properties中oos的配置
@Component
//读取配置类
@PropertySource("classpath:oss.properties")
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${aliyun.oss.file.endpoint}")
    private String endPoint;
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;
    @Value("${aliyun.oss.file.contentname.avatar}")
    private String avatar;
    @Value("${aliyun.oss.file.contentname.cover}")
    private String cover;


    //定义常量，在属性设置完成时赋值
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    public static String CONTENT_NAME_AVATAR;
    public static String CONTENT_NAME_COVER;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT=endPoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
        CONTENT_NAME_AVATAR= avatar;
        CONTENT_NAME_COVER= cover;
    }
}
