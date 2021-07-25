package com.atguigu.statisticsservice.service.impl;

import com.atguigu.statisticsservice.client.UCenterClient;
import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.atguigu.statisticsservice.mapper.StatisticsDailyMapper;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author ljd
 * @since 2020-08-28
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UCenterClient uCenterClient;

    @Override
    public void createStatistics(String date) {

        //首先删除当期日期的所有数据
        QueryWrapper<StatisticsDaily> wrapper= new QueryWrapper<>();
        wrapper.eq("date_calculated", date);
        this.remove(wrapper);

        //获取统计数据
        //统计当天注册人数
        Integer registerCounts = uCenterClient.getMemberRegisterStatistics(date);
        //TODO  统计当天登录人数
        Integer loginNum = RandomUtils.nextInt(100, 200);
        //TODO  统计视频播放数量
        Integer videoViewNum = RandomUtils.nextInt(100, 200);
        //TODO  统计课程发布数量
        Integer courseNum = RandomUtils.nextInt(100, 200);

        //创建统计对象并插入值
        StatisticsDaily statistics= new StatisticsDaily();
        statistics.setRegisterNum(registerCounts);
        statistics.setLoginNum(loginNum);
        statistics.setVideoViewNum(videoViewNum);
        statistics.setCourseNum(courseNum);
        statistics.setDateCalculated(date);

        baseMapper.insert(statistics);
    }

    @Override
    public Map<String, Object> getCharts(String begin, String end, String[] checkedTypes) {

        QueryWrapper<StatisticsDaily> wrapper= new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", "login_num", "register_num",
                "video_view_num", "course_num");
        List<StatisticsDaily> statisticsList = baseMapper.selectList(wrapper);

        //构建返回list
        List<String> dateList= new ArrayList<>();
        List<Integer> loginNumList= new ArrayList<>();
        List<Integer> registerNumList= new ArrayList<>();
        List<Integer> videoViewNumList= new ArrayList<>();
        List<Integer> courseNumList= new ArrayList<>();
        statisticsList.forEach(statistics ->{
            dateList.add(statistics.getDateCalculated());
            for (String checkedType : checkedTypes) {
                switch (checkedType){
                    case "学员登录数统计":
                        loginNumList.add(statistics.getLoginNum());
                        break;
                    case "学员注册数统计":
                        registerNumList.add(statistics.getRegisterNum());
                        break;
                    case "课程播放数统计":
                        videoViewNumList.add(statistics.getVideoViewNum());
                        break;
                    case "每日课程数统计":
                        courseNumList.add(statistics.getCourseNum());
                        break;
                    default:
                        break;

                }
            }
        });

        //将各个list封装到map中去
        Map<String, Object> map= new HashMap<>();
        map.put("dateList", dateList);
        map.put("loginNumList", loginNumList);
        map.put("registerNumList", registerNumList);
        map.put("videoViewNumList", videoViewNumList);
        map.put("courseNumList", courseNumList);

        return map;
    }
}
