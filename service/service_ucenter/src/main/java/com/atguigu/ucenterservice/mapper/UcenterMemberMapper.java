package com.atguigu.ucenterservice.mapper;

import com.atguigu.ucenterservice.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author ljd
 * @since 2020-08-18
 */
public interface UcenterMemberMapper extends BaseMapper<Member> {

    Integer countRegister(String date);
}
