package com.atguigu.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBought(String courseId, String memberId) {
        return false;
    }
}
