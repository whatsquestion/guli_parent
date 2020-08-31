package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.MemberInfoVo;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-18
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/ucenterservice/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        //根据request对象获取token中的id信息
        String memeberId = JwtUtils.getMemberIdByJwtToken(request);
        Member loginInfo = memberService.getById(memeberId);
        return R.ok().data("loginInfo",loginInfo);
    }

    @ApiOperation(value = "根据会员Id获取会员信息")
    @GetMapping("/getMemberInfo/{memberId}")
    public MemberInfoVo getMemberInfo(@PathVariable String memberId){

        Member member = memberService.getById(memberId);
        MemberInfoVo memberInfoVo= new MemberInfoVo();
        BeanUtils.copyProperties(member, memberInfoVo);
        return memberInfoVo;
    }

    @ApiOperation(value = "统计每次注册人数")
    @GetMapping("/getMemberRegisterStatistics/{date}")
    public Integer getMemberRegisterStatistics(@PathVariable String date){
        //根据日期查询当日注册人数
        Integer registerCount= memberService.countRegister(date);

        return registerCount;
    }

}

