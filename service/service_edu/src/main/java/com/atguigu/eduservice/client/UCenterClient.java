package com.atguigu.eduservice.client;

import com.atguigu.commonutils.vo.MemberInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter", fallback = UCenterClientImpl.class)
public interface UCenterClient {

    //根据用户Id获取Member信息
    @GetMapping("/ucenterservice/member/getMemberInfo/{memberId}")
    public MemberInfoVo getMemberInfo(@PathVariable("memberId") String memberId);
}
