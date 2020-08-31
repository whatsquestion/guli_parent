package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Api(tags = "课时管理")
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;


    @ApiOperation(value = "新增课时")
    @PostMapping("/addVideo")
    public R addVideo( @RequestBody EduVideo video){
        videoService.save(video);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询课时")
    @GetMapping("/getVideo/{id}")
    public R getVideo(@PathVariable String id){
        EduVideo video = videoService.getById(id);
        return R.ok().data("video", video);
    }

    @ApiOperation(value = "根据ID修改课时")
    @PutMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo video){
        videoService.updateById(video);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("/deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){

        boolean result = videoService.removeVideoById(id);
        return result? R.ok(): R.error().message("删除失败");
    }

}

