package com.atguigu.serviceoss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.serviceoss.service.OssService;
import com.atguigu.serviceoss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadAvatar(MultipartFile file) {
        String avatar= ConstantPropertiesUtil.CONTENT_NAME_AVATAR;
        return getOssBaseInfo(file, avatar);
    }

    @Override
    public String uploadCover(MultipartFile file) {
        String cover= ConstantPropertiesUtil.CONTENT_NAME_COVER;
        return getOssBaseInfo(file, cover);
    }

    private String getOssBaseInfo(MultipartFile file, String baseContent){
        // 上海地区的EndPoint
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 云账号AccessKey有所有API访问权限
        // 建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维
        // 请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName= ConstantPropertiesUtil.BUCKET_NAME;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            //上传文件名
            String originalFilename = file.getOriginalFilename();
            String fileName= UUID.randomUUID().toString();
            String newName= fileName+originalFilename.substring(originalFilename.lastIndexOf("."));
            String datePath= new DateTime().toString("yyyyMMdd");
            //https://gulisfile.oss-cn-shanghai.aliyuncs.com/avatar/33.jpg
            //https://gulisfile.oss-cn-shanghai.aliyuncs.com/avatar/a86f108c-02b2-4821-ba5e-4c71ac5f679d.jpg
            String fileUrl= baseContent+"/"+datePath+"/"+newName;
            ossClient.putObject(bucketName, fileUrl, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //获取url地址
            String uploadUrl= "https://"+bucketName+"."+endpoint+"/"+fileUrl;
            return uploadUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
