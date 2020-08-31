package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourseDescription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程简介 Mapper 接口
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Repository
@Mapper
public interface EduCourseDescriptionMapper extends BaseMapper<EduCourseDescription> {

}
