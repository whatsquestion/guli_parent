package com.atguigu.eduservice.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVo {

    private String id;
    private String title;
    @JsonIgnore
    private String parentId;

    //二级分类集合
    private List<SubjectVo> children= new ArrayList<>();
}
