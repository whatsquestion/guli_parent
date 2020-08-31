package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-04
 */
@Api(tags = "章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @ApiOperation(value = "根据课程ID查询章节信息")
    @GetMapping("/getChapterListByCourseId/{courseId}")
    public R getChapterListByCourseId(@PathVariable String courseId){

        List<ChapterVo> chapterVoList= chapterService.getChapterList(courseId);

        return R.ok().data("chapterList", chapterVoList);
    }


    @ApiOperation(value = "新增章节")
    @PostMapping("/addChapter")
    public R addChapter( @RequestBody EduChapter chapter){
        chapterService.save(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("/getChapter/{id}")
    public R getChapter(@PathVariable String id){
        EduChapter chapter = chapterService.getById(id);
        return R.ok().data("chapter", chapter);
    }

    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter chapter){
        chapterService.updateById(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("/deleteChapter/{id}")
    public R deleteChapter(@PathVariable String id){

        boolean result = chapterService.deleteChapterById(id);
        return result? R.ok(): R.error().message("删除失败");
    }


}

