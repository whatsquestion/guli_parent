package com.atguigu.statisticsservice.schedule;

import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.atguigu.statisticsservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StatisticsSchedule {

    @Autowired
    private StatisticsDailyService statisticsService;

    /**
     * 每天凌晨1点执行生成统计报表任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        String date = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        statisticsService.createStatistics(date);

    }
}
