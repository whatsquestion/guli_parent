package com.atguigu.serviceoss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    /**
     * 上传头像文件到对象存储
     * @param file     文件对象
     * @return        头像地址
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 上传封面图像到对象存储
     * @param file
     * @return
     */
    String uploadCover(MultipartFile file);
}
