package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.query.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-07-28
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void queryPage(Page<EduTeacher> page, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> wrapper= new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level", level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }

        //排序，按照添加时间降序
        wrapper.orderByDesc("gmt_create");

        baseMapper.selectPage(page, wrapper);

    }

    @Override
    @Cacheable(value = "teacherList", key = "'selectHotTeacherList'")
    public List<EduTeacher> selectHotTeacherList() {
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        return this.list(wrapperTeacher);
    }

    @Override
    public Map<String, Object> pageListWeb(Page<EduTeacher> page) {

        QueryWrapper<EduTeacher> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("sort");

        baseMapper.selectPage(page, wrapper);

        List<EduTeacher> records = page.getRecords();
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


}
