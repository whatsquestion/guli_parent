package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order", fallback = OrderClientImpl.class)
public interface OrderClient {

    @GetMapping("/orderservice/order/isBought/{courseId}/{memberId}")
    public boolean isBought(@PathVariable("courseId") String courseId,
                            @PathVariable("memberId") String memberId);

}
