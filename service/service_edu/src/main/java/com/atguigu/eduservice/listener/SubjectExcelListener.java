package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    private EduSubjectService subjectService;

    public SubjectExcelListener(){

    }

    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService= subjectService;
    }


    //一行一行去读取excle内容
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {


        if (data==null){
            throw new GuliException(20001, "文件内容为空");
        }

        //添加一级分类
        EduSubject firstSubject = this.existFirstSubject(data.getFristSubjectName());
        if (firstSubject==null){
            firstSubject= new EduSubject();
            firstSubject.setTitle(data.getFristSubjectName());
            firstSubject.setParentId("0");
            subjectService.save(firstSubject);
        }

        //获取一级分类的id值
        String pid = firstSubject.getId();

        //添加二级分类
        EduSubject secondSubject = this.existSecondSubject(data.getSecondSubjectName(), pid);
        if (secondSubject==null){
            secondSubject= new EduSubject();
            secondSubject.setTitle(data.getSecondSubjectName());
            secondSubject.setParentId(pid);
            subjectService.save(secondSubject);
        }


    }

    private EduSubject existFirstSubject(String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id","0");
        return subjectService.getOne(wrapper);
    }

    private EduSubject existSecondSubject(String name, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id",pid);
        return subjectService.getOne(wrapper);
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
