package com.atguigu.vodservice.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.vodservice.service.VodService;
import com.atguigu.vodservice.util.AliyunVodSDKUtils;
import com.atguigu.vodservice.util.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {


    @Override
    public String uploadVideo(MultipartFile file){
        try {
            InputStream inputStream= file.getInputStream();
            String originalFilename= file.getOriginalFilename();
            String title=originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(StringUtils.isEmpty(videoId)){
                    throw new GuliException(20001, errorMessage);
                }
            }

            return videoId;
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "VOD上次视频服务失败");
        }
    }

    @Override
    public void removeVideo(String videoId) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId());

        } catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "删除小节视频失败!");
        }
    }

    @Override
    public void removeVideoList(List videoIdList) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            String videoIds = String.join(",", videoIdList);
            request.setVideoIds(videoIds);
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId());

        } catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "批量删除小节视频失败!");
        }
    }

}
