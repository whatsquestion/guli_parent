package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.Api;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "章节信息")
@Data
public class ChapterVo {

    private String id;
    private String title;

    //章节中的小节列表
    private List<VideoVo> children= new ArrayList<>();

}
