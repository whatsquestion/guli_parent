package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/index")
@Api(tags = "前台首页数据显示管理")
public class IndexController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    @GetMapping("/index")
    @ApiOperation(value = "显示热门讲师和课程")
    public R index(){
        //查询前8条热门课程
        List<EduCourse> courseList= courseService.selectHotCourseList();
        //查询前4条名师
        List<EduTeacher> teacherList= teacherService.selectHotTeacherList();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }

}
