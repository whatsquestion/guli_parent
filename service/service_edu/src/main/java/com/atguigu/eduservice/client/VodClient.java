package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    @ApiOperation(value = "删除视频")
    @DeleteMapping("/vodservice/video/removeVideo/{videoId}")
    public R removeVideo(@PathVariable("videoId") String videoId);

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("/vodservice/video/removeVideoList")
    public R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);

}
