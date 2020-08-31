package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.SubjectVo;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //存放课程分类一级目录
    private List<SubjectVo> rootList= new ArrayList<>();

    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService subjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
            //导入课程分类后重新计算rootList
            rootList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<SubjectVo> getAllSujbectList() {

        if (rootList.size()==0){
            List<SubjectVo> subjectVos= new ArrayList<>();
            List<EduSubject> subjects= this.list(null);

            for (EduSubject eduSubject: subjects){
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(eduSubject, subjectVo);
                subjectVos.add(subjectVo);
            }

            Map<String, SubjectVo> map= new HashMap<>();
            //将所有的课程分类存储到map中
            for (SubjectVo subjectVo : subjectVos){
                map.put(subjectVo.getId(), subjectVo);
            }

            for (SubjectVo subjectVo : subjectVos){
                //判断每个subjectVo是否是一级分类
                if (subjectVo.getParentId().equals("0")){
                    rootList.add(subjectVo);
                }else {
                    SubjectVo parentSubject = map.get(subjectVo.getParentId());
                    parentSubject.getChildren().add(subjectVo);
                }
            }
        }

        return rootList;
    }
}
