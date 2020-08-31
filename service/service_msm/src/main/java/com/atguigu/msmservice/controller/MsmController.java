package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msmservice/msm")
@Api(tags = "阿里云短信服务管理")
public class MsmController {

    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "发送短信验证码")
    @GetMapping("/send/{phone}")
    public R code(@PathVariable String phone){
        //先从redis中获取验证码,如果存在,直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //否则，发送验证码到手机中，再将验证码存到redis中去
        code= RandomUtil.getFourBitRandom();
        Map<String, Object> codeMap= new HashMap<>();
        codeMap.put("code", code);
        System.out.println("【验证码】:"+code);
//        boolean isSuccess= msmService.sendMsg(codeMap, phone);
        boolean isSuccess= true;

        if (isSuccess){
            //设置有效时间为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        }

        return R.error().message("短信发送失败");
    }

}
