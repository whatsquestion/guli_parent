package com.atguigu.aliyunvod;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;

import java.util.List;

public class VodTest {

    String accessKeyId = "LTAI4G3Ff6LLsnBsntqNYNUX";
    String accessKeySecret = "9CGTLyz3APKCD1Z1iD1aET0mvubDnb";

    @Test
    //获取视频播放地址
    //这种方式是无法观看加密的视频
    public void testGetPalyInfo() throws Exception{
        //初始化客户端
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);
        //获取请求和响应
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        //设置请求参数
        request.setVideoId("ff2b14a7a5864a5c9be5d21f8314a80a");
        response= client.getAcsResponse(request);

        //遍历response中的播放信息
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        for (GetPlayInfoResponse.PlayInfo playInfo: playInfoList){
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //输出视频的base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        //输出请求id
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    @Test
    //获取视频播放凭证，后期结合阿里的视频播放器可以播放
    //这种方法无论是加密还是不加密的都可以
    public void testGetPlayAuth() throws Exception{
        //初始化客户端
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);
        //获取请求
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        //设置请求参数
        request.setVideoId("3242db54239f484dbff53af0857f6e9f");
        //获取响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        //播放凭证
        System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
        //VideoMeta信息
        System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        //输出请求id
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }


    @Test
    public void testUploadVideo(){

        //1.音视频上传-本地文件上传
        //视频标题(必选)
        String title = "myTest - upload by sdk";
        //本地文件上传和文件流上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
        //文件名必须包含扩展名
        String fileName = "C:\\Users\\ljd\\Desktop\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4";
        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(1 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
        注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
        request.setEnableCheckpoint(false);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }

    }
}
