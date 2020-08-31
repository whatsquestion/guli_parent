package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterList(String courseId) {

        //根据课程id查询所有的章节信息
        QueryWrapper<EduChapter> wrapper= new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<EduChapter> chapterList = this.list(wrapper);

        //根据课程id查询所有的小节信息
        QueryWrapper<EduVideo> wrapper2= new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        List<EduVideo> videoList= videoService.list(wrapper2);

        List<ChapterVo> chapterVoList= new ArrayList<>();
        chapterList.forEach(chapter ->{
            ChapterVo chapterVo= new ChapterVo();
            List<VideoVo> videoVoList= new ArrayList<>();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);
            videoList.forEach(video ->{
                if (video.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo= new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            });

            chapterVo.setChildren(videoVoList);
        });

        return chapterVoList;
    }

    @Override
    public boolean deleteChapterById(String id) {

        QueryWrapper<EduVideo> wrapper= new QueryWrapper<>();
        wrapper.eq("chapter_id", id);
        int count = videoService.count(wrapper);

        if (count>0){
            throw new GuliException(20001, "请先删除章节下的小节!");
        }
        return this.removeById(id);
    }

    @Override
    public void deleteChapterByCid(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
