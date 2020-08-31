package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu", fallback = EduClientImpl.class)
public interface EduClient {

    @GetMapping("/eduservice/front/course/getCourseInfo/{courseId}")
    public CourseInfoVo getCourseInfo(@PathVariable("courseId") String courseId);
}
