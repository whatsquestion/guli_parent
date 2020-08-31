package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseInfoVo;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.query.front.CourseQuery;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.front.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "前台课程管理")
@RestController
@RequestMapping("/eduservice/front/course")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "分页带条件查询")
    @PostMapping("/{current}/{size}")
    public R pageListWeb(@PathVariable Long current, @PathVariable Long size,
                         @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> page= new Page<>(current, size);
        Map<String, Object> courseMap= courseService.pageListWeb(page, courseQuery);

        return R.ok().data(courseMap);
    }

    @ApiOperation(value = "根据ID查询课程详情信息")
    @GetMapping("/getCourseWebInfo/{courseId}")
    public R getCourseWebInfo(@PathVariable String courseId, HttpServletRequest request){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.getCourseWebInfo(courseId);
        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.getChapterList(courseId);
        //查询当前课程是否被购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBought = orderClient.isBought(courseId, memberId);

        return R.ok().data("courseWebVo", courseWebVo).data("chapterVoList", chapterVoList).data("isBought", isBought);
    }

    @ApiOperation(value = "根据ID查询课程详情信息(订单使用)")
    @GetMapping("/getCourseInfo/{courseId}")
    public CourseInfoVo getCourseInfo(@PathVariable String courseId){
        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.getCourseWebInfo(courseId);
        CourseInfoVo courseInfoVo= new CourseInfoVo();
        BeanUtils.copyProperties(courseWebVo, courseInfoVo);
        return courseInfoVo;
    }
}
