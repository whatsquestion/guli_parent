package com.atguigu.statisticsservice.service;

import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author ljd
 * @since 2020-08-28
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void createStatistics(String date);

    /**
     *
     * @param begin          开始时间
     * @param end            结束时间
     * @param checkedTypes   查询类型
     * @return            封装了各种统计的map集合
     */
    Map<String, Object> getCharts(String begin, String end, String[] checkedTypes);
}
