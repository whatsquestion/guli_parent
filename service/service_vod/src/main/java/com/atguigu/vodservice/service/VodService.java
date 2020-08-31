package com.atguigu.vodservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    /**
     * 上次小节视频
     * @param file   视频文件
     * @return       视频id
     */
    String uploadVideo(MultipartFile file);

    void removeVideo(String videoId);

    void removeVideoList(List videoIdList);
}
