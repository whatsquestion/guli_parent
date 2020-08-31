package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.query.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.front.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService descriptionService;
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private EduChapterService chapterService;

    @Override
    public String addCourseInfo(CourseInfoVo courseInfoVo) {
        //属性copy，将vo中course的属性赋值过来
        EduCourse course= new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, course);

        course.setStatus(EduCourse.COURSE_DRAFT);
        boolean save = this.save(course);
//        int insert = baseMapper.insert(course);
        if (!save){
            throw new GuliException(20001, "保存课程基本信息失败");
        }

        EduCourseDescription description= new EduCourseDescription();
        description.setId(course.getId());
        description.setDescription(courseInfoVo.getDescription());
        boolean save2 = descriptionService.save(description);
        if (!save2){
            throw new GuliException(20001, "保存课程描述信息失败");
        }

        return course.getId();
    }

    @Override
    public CourseInfoVo getCourseById(String courseId) {

        //获取课程信息
        EduCourse course = this.getById(courseId);
        CourseInfoVo infoVo= new CourseInfoVo();
        BeanUtils.copyProperties(course, infoVo);
        //获取课程描述信息
        EduCourseDescription description = descriptionService.getById(courseId);
        infoVo.setDescription(description.getDescription());

        return infoVo;
    }

    @Override
    public void updateCourse(CourseInfoVo courseInfoVo) {
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, course);
        //更新课程表
        boolean update = this.updateById(course);
        if (!update){
            throw new GuliException(20001, "更新课程失败");
        }
        //更新课程描述表
        EduCourseDescription description = descriptionService.getById(course.getId());
        description.setDescription(courseInfoVo.getDescription());
        boolean update1 = descriptionService.updateById(description);
        if (!update1){
            throw new GuliException(20001, "更新课程描述失败");
        }
    }

    @Override
    public CoursePublishVo getCoursePublishVoByCid(String id) {
        return baseMapper.getCoursePublishVoByCid(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus(EduCourse.COURSE_NORMAL);
        Integer count = baseMapper.updateById(course);
        return null != count && count > 0;
    }

    @Override
    public void pageQuery(Page<EduCourse> page, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");

        if (courseQuery==null){
            baseMapper.selectPage(page, null);
            return ;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();

        //分别获取查询条件
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            wrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }

        baseMapper.selectPage(page, wrapper);

    }

    @Override
    public boolean removeCourseById(String courseId) {

        //删除video
        videoService.deleteVideoByCid(courseId);
        //删除chapter
        chapterService.deleteChapterByCid(courseId);
        //删除description
        descriptionService.removeById(courseId);
        //删除course
        boolean result = this.removeById(courseId);

        return result;
    }

    @Override
    @Cacheable(value = "courseList", key = "'selectHotCourseList'")
    public List<EduCourse> selectHotCourseList() {
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        wrapperCourse.orderByDesc("id");
        wrapperCourse.last("limit 8");
        return this.list(wrapperCourse);
    }

    @Override
    public List<EduCourse> getCourseByTid(String id) {
        QueryWrapper<EduCourse> wrapper= new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        //按照最后更新时间倒序排列
        wrapper.orderByDesc("gmt_modified");
        return this.list(wrapper);
    }

    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> page, com.atguigu.eduservice.entity.query.front.CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper= new QueryWrapper<>();
        if (courseQuery==null){
            wrapper.orderByDesc("gmt_modified");
            baseMapper.selectPage(page, wrapper);
        }else {
            //获取courseQuery中的查询条件
            String title = courseQuery.getTitle();
            String teacherId = courseQuery.getTeacherId();
            String subjectParentId = courseQuery.getSubjectParentId();
            String subjectId = courseQuery.getSubjectId();
            String priceSort = courseQuery.getPriceSort();
            String gmtCreateSort = courseQuery.getGmtCreateSort();
            String buyCountSort = courseQuery.getBuyCountSort();

            if (!StringUtils.isEmpty(title)){
                wrapper.like("title", title);
            }
            if (!StringUtils.isEmpty(teacherId)){
                wrapper.eq("teacher_id", teacherId);
            }
            if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
                wrapper.eq("subject_parent_id", subjectParentId);
            }
            if (!StringUtils.isEmpty(courseQuery.getSubjectId())) {
                wrapper.eq("subject_id", subjectId);
            }
            //排序
            if (!StringUtils.isEmpty(buyCountSort)) {
                wrapper.orderByDesc("buy_count");
            }
            if (!StringUtils.isEmpty(gmtCreateSort)) {
                wrapper.orderByDesc("gmt_create");
            }
            if (!StringUtils.isEmpty(priceSort)) {
                wrapper.orderByDesc("price");
            }
            baseMapper.selectPage(page, wrapper);
        }

        //分页逻辑
        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getCourseWebInfo(String courseId) {
        return baseMapper.getCourseWebInfo(courseId);
    }

}
