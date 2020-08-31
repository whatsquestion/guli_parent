package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.SubjectVo;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    @ApiOperation(value = "从Excel中添加课程分类")
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        try {
            subjectService.importSubjectData(file, subjectService);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    @GetMapping("/getSubjectList")
    public R getSubjectList(){

        //获取所有课程列表及对应关系
        List<SubjectVo> list= subjectService.getAllSujbectList();

        return R.ok().data("items", list);
    }

}

