package com.atguigu.ucenterservice.service;

import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-18
 */
public interface UcenterMemberService extends IService<Member> {

    /**
     * 会员登录
     * @param loginVo   登录vo
     * @return          token令牌
     */
    String login(LoginVo loginVo);

    /**
     * 会员注册
     * @param registerVo   注册vo
     */
    void register(RegisterVo registerVo);

    /**
     * 根据openid查询会员
     * @param openid     openid
     * @return
     */
    Member getMemberByOpenId(String openid);

    /**
     * 根据日期查询当日注册人数
     * @param date    日期
     * @return        注册人数
     */
    Integer countRegister(String date);
}
