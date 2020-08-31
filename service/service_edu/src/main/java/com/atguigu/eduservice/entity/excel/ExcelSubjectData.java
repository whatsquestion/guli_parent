package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelSubjectData {

    @ExcelProperty(value = "课程分类", index = 0)
    private String fristSubjectName;

    @ExcelProperty(value = "课程名称", index = 1)
    private String secondSubjectName;

}
