package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public void deleteVideoByCid(String courseId) {
        //删除视频
        QueryWrapper<EduVideo> wrapper= new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper);
        //遍历Video,将source_id放到新的list中
        List<String> videoIdList= new ArrayList<>();
        eduVideoList.forEach(video -> {
            String videoSourceId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)){
                videoIdList.add(videoSourceId);
            }
        });

        //调用vod服务删除远程视频
        if(videoIdList.size() > 0){
            vodClient.removeVideoList(videoIdList);
        }

        //删除小节
        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        baseMapper.delete(wrapper2);
    }

    @Override
    public boolean removeVideoById(String id) {

        //查询阿里云中的视频id
        EduVideo video = baseMapper.selectById(id);
        String videoId = video.getVideoSourceId();
        //判断小节中是否存在视频
        if (!StringUtils.isEmpty(videoId)){
            //远程调用，删除视频
            vodClient.removeVideo(videoId);
        }
        //删除小节
        int count = baseMapper.deleteById(id);

        return count>0;
    }
}
