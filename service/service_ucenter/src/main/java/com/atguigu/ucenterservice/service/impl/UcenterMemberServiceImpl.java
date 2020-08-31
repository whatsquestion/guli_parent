package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-18
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, Member> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(password)) {
            throw new GuliException(20001,"手机号或者密码不能为空");
        }

        //获取会员
        Member member = baseMapper.selectOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if(null == member) {
            throw new GuliException(20001,"此手机号未存在，请注册");
        }

        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(20001,"密码错误");
        }

        //校验是否被禁用
        if(member.getIsDisabled()) {
            throw new GuliException(20001,"用户已被禁用，请联系管理员");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"请完善以上信息");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobileCode)) {
            throw new GuliException(20001,"验证吗不正确");
        }

        //查询数据库中是否存在相同的手机号码
        Integer count = baseMapper.selectCount(new QueryWrapper<Member>().eq("mobile", mobile));
        if(count.intValue() > 0) {
            throw new GuliException(20001,"手机号已注册，请登录");
        }

        //添加注册信息到数据库
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://gulisfile.oss-cn-shanghai.aliyuncs.com/avatar/" +
                "20200803/16cce4d0-d803-49ef-8386-afc47906fa71.png");
        this.save(member);
    }

    @Override
    public Member getMemberByOpenId(String openid) {
        QueryWrapper<Member> wrapper= new QueryWrapper<>();
        wrapper.eq("openid", openid);
        Member member = baseMapper.selectOne(wrapper);

        return member;
    }

    @Override
    public Integer countRegister(String date) {
        return baseMapper.countRegister(date);
    }
}
