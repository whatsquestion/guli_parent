package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R removeVideo(String videoId) {
        return R.error().message("删除视频超时...");
    }

    @Override
    public R removeVideoList(List<String> videoIdList) {
        return R.error().message("批量删除视频超时...");
    }
}
