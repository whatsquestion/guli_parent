package com.atguigu.statisticsservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/statisticsservice/statistics")
@Api(tags = "统计分析管理")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsService;

    @ApiOperation(value = "根据日期创建并查询当日统计")
    @PostMapping("createStatistics/{date}")
    public R createStatistics(@PathVariable String date){
        statisticsService.createStatistics(date);
        return R.ok();
    }

    @ApiOperation(value = "显示折线图")
    @GetMapping("/showCharts/{begin}/{end}/{checkedTypes}")
    public R showCharts(@PathVariable String begin, @PathVariable String end,
                        @PathVariable String[] checkedTypes){
        
        Map<String, Object> map= statisticsService.getCharts(begin, end, checkedTypes);

        return R.ok().data(map);
    }

}

