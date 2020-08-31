package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "后台用户登录模块")
@RestController
@RequestMapping("/eduservice/user")
//跨域问题解决
//@CrossOrigin
public class EduLoginController {

    @ApiOperation(value = "登录")
    @RequestMapping("/login")
    public R login(){

        return R.ok().data("token", "admin");
    }

    @ApiOperation(value = "获取用户信息")
    @RequestMapping("/info")
    public R info(){

        Map<String, Object> map= new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", "admin");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        return R.ok().data(map);
    }
}
