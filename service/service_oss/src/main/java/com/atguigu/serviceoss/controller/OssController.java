package com.atguigu.serviceoss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.serviceoss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ossservice/fileoss")
@Api(value = "阿里对象存储OSS")
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation("上传头像文件到Oss")
    @PostMapping("/uploadAvatar")
    public R uploadAvatar(MultipartFile file){

        String url= ossService.uploadAvatar(file);

        return R.ok().data("url", url);
    }

    @ApiOperation("上传封面图片文件到Oss")
    @PostMapping("/uploadCover")
    public R uploadCover(MultipartFile file){

        String url= ossService.uploadCover(file);

        return R.ok().data("url", url);
    }

}
