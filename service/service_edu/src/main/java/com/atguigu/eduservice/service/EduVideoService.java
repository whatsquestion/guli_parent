package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
public interface EduVideoService extends IService<EduVideo> {

    void deleteVideoByCid(String courseId);

    /**
     * 根据小节id删除小节以及视频
     * @param id    小节id
     * @return
     */
    boolean removeVideoById(String id);
}
