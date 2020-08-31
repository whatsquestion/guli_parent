package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
public interface EduChapterService extends IService<EduChapter> {

    /**
     * 获取章节信息
     * @param courseId     课程Id
     * @return             章节Vo列表
     */
    List<ChapterVo> getChapterList(String courseId);

    /**
     * 删除章节
     * @param id    章节 id
     * @return
     */
    boolean deleteChapterById(String id);

    /**
     * 根据课程id删除章节
     * @param courseId
     */
    void deleteChapterByCid(String courseId);
}
