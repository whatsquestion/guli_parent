package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.query.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/eduservice/course")
@Api(tags = "课程管理")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        String id= courseService.addCourseInfo(courseInfoVo);

        return R.ok().data("courseId", id);
    }

    @ApiOperation(value = "根据课程Id查询课程")
    @GetMapping("/getCourseById/{courseId}")
    public R getCourseById(@PathVariable String courseId){
        CourseInfoVo courseInfoVo= courseService.getCourseById(courseId);
        return R.ok().data("courseInfo", courseInfoVo);
    }

    @ApiOperation(value = "更新课程")
    @PutMapping("/updateCourse")
    public R updateCourse(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourse(courseInfoVo);
        return R.ok();
    }

    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("getCoursePublishVoByCid/{id}")
    public R getCoursePublishVoByCid(@PathVariable String id){

        CoursePublishVo coursePublishInfo = courseService.getCoursePublishVoByCid(id);
        return R.ok().data("coursePublishInfo", coursePublishInfo);
    }

    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publishCourse/{id}")
    public R publishCourseById(@PathVariable String id){

        courseService.publishCourseById(id);
        return R.ok();
    }

    @ApiOperation(value = "分页课程列表")
    @PostMapping("/pageQuery/{current}/{size}")
    public R pageQuery(@PathVariable Long current, @PathVariable Long size,
                       @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> page= new Page<>(current, size);
        courseService.pageQuery(page, courseQuery);
        long total = page.getTotal();
        List<EduCourse> courseList = page.getRecords();

        return R.ok().data("total", total).data("rows", courseList);

    }

    @ApiOperation(value = "删除课程")
    @DeleteMapping("/deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId){

        boolean result = courseService.removeCourseById(courseId);

        return result?R.ok():R.error().message("删除失败");
    }


}

