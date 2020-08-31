package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface EduSubjectMapper extends BaseMapper<EduSubject> {

}
