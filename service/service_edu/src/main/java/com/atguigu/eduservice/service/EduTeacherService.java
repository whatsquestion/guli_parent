package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.query.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-07-28
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void queryPage(Page<EduTeacher> page, TeacherQuery teacherQuery);

    List<EduTeacher> selectHotTeacherList();

    Map<String, Object> pageListWeb(Page<EduTeacher> page);
}
