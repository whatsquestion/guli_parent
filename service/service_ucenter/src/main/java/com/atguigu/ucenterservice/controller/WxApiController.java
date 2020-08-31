package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.util.ConstantPropertiesUtil;
import com.atguigu.ucenterservice.util.HttpClientUtil;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
@Api(tags = "微信扫码登录")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "获取扫描人信息")
    @GetMapping("/callback")
    public String callback(String code, String state){

        //得到授权临时票据code
        System.out.println("【code】:"+code);
        System.out.println("【state】:"+state);

        //向认证服务器发送请求获取access_token
        String baseAccessTokenUrl= "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl= String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET, code);
        String result;
        try {
            result = HttpClientUtil.get(accessTokenUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取accessToken失败");
        }

        //解析result字符串
        Gson gson= new Gson();
        HashMap accessTokenMap = gson.fromJson(result, HashMap.class);
        String accessToken = (String)accessTokenMap.get("access_token");
        String openid = (String)accessTokenMap.get("openid");

        //查询当前数据库是否存在此openid
        Member member= memberService.getMemberByOpenId(openid);
        if (null==member){
            System.out.println("新用户注册");
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl= String.format(baseUserInfoUrl, accessToken, openid);
            String userInfo;
            try {
                userInfo = HttpClientUtil.get(userInfoUrl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GuliException(20001, "获取微信用户信息失败");
            }
            //解析userInfo
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            System.out.println("【用户信息】："+userInfoMap);
            String nickName = (String) userInfoMap.get("nickname");
            String headimgurl = (String) userInfoMap.get("headimgurl");

            //向数据库中插入用户
            member= new Member();
            member.setNickname(nickName);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }

        //登录  生成JWT
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
        //因为端口号不同存在跨域问题，cookie不能跨域，所以这里使用url重写
        return "redirect:http://localhost:3000?token=" + token;

    }

    @ApiOperation(value = "生成微信扫描二维码")
    @GetMapping("/login")
    public String genQrConnect(){

        // 微信开放平台授权baseUrl
        String baseUrl= "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=atguigu" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl= ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl= URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GuliException(20001, e.getMessage());
        }

        String qrcodeUrl= String.format(baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID, redirectUrl);


        return "redirect:"+qrcodeUrl;
    }

}
