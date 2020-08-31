package com.atguigu.statisticsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter", fallback = UCenterClientImpl.class)
public interface UCenterClient {

    @GetMapping("/ucenterservice/member/getMemberRegisterStatistics/{date}")
    public Integer getMemberRegisterStatistics(@PathVariable("date") String date);
}
