package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
public interface EduSubjectService extends IService<EduSubject> {

    void importSubjectData(MultipartFile file, EduSubjectService subjectService);

    List<SubjectVo> getAllSujbectList();
}
