package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.Api;
import lombok.Data;

@Api(tags = "小节信息")
@Data
public class VideoVo {

    private String id;
    private String title;
    private String videoSourceId;
}
