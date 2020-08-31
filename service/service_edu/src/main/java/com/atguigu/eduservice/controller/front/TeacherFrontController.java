package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "前台讲师管理")
@RestController
@RequestMapping("/eduservice/front/teacher")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("/{current}/{size}")
    public R pageListWeb(@PathVariable Long current, @PathVariable Long size){

        Page<EduTeacher> page= new Page<>(current, size);
        Map<String, Object> teacherMap= teacherService.pageListWeb(page);

        return R.ok().data(teacherMap);
    }

    @ApiOperation(value = "根据讲师Id查询讲师及其课程")
    @GetMapping("/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        List<EduCourse> courseList= courseService.getCourseByTid(id);

        return R.ok().data("teacher",teacher).data("courseList", courseList);
    }
}
