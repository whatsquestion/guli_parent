package com.atguigu.vodservice.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.vodservice.service.VodService;
import com.atguigu.vodservice.util.AliyunVodSDKUtils;
import com.atguigu.vodservice.util.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/vodservice/video")
@Api(tags = "视频点播管理")
public class VodController {

    @Autowired
    private VodService vodService;

    @ApiOperation(value = "小节视频上传")
    @PostMapping("/uploadVideo")
    public R uploadVideo(MultipartFile file){
        String videoId= vodService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }

    @ApiOperation(value = "删除小节视频")
    @DeleteMapping("/removeVideo/{videoId}")
    public R removeVideo(@PathVariable String videoId){
        vodService.removeVideo(videoId);
        return R.ok().message("视频删除成功");
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("/removeVideoList")
    public R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeVideoList(videoIdList);
        return R.ok().message("视频批量删除成功");
    }

    @ApiOperation(value = "获取视频凭证")
    @GetMapping("/getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable String videoId){
        try {
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils
                    .initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID,
                            ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //获取请求
            GetVideoPlayAuthRequest request= new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //获取凭证
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth", playAuth);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取视频凭证失败");
        }
    }
}
