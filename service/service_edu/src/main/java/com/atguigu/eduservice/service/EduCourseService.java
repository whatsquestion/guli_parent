package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.query.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.front.CourseWebVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseById(String courseId);

    void updateCourse(CourseInfoVo courseInfoVo);

    CoursePublishVo getCoursePublishVoByCid(String id);

    boolean publishCourseById(String id);

    void pageQuery(Page<EduCourse> current, CourseQuery courseQuery);

    boolean removeCourseById(String courseId);

    List<EduCourse> selectHotCourseList();

    List<EduCourse> getCourseByTid(String id);

    Map<String, Object> pageListWeb(Page<EduCourse> page, com.atguigu.eduservice.entity.query.front.CourseQuery courseQuery);

    CourseWebVo getCourseWebInfo(String courseId);

}
