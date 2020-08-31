package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.query.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-07-28
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "查询所有讲师")
    @GetMapping("/findAll")
    public R findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "删除讲师")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id){
        boolean flag = eduTeacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询老师")
    @GetMapping("/pageQuery/{current}/{size}")
    public R pageQuery(@PathVariable long current, @PathVariable long size){

        //创建分页对象
        Page<EduTeacher> page= new Page<>(current, size);
        eduTeacherService.page(page, null);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();

        Map<String, Object> map= new HashMap<>();
        map.put("total", total);
        map.put("rows", records);

        return R.ok().data(map);

    }

    @ApiOperation(value = "多条分页查询老师")
    @PostMapping("/pageQuery/{current}/{size}")
    public R pageQuery(@PathVariable long current, @PathVariable long size,
                       @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建分页对象
        Page<EduTeacher> page= new Page<>(current, size);
        eduTeacherService.queryPage(page, teacherQuery);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();

        Map<String, Object> map= new HashMap<>();
        map.put("total", total);
        map.put("rows", records);

        return R.ok().data(map);
    }

    @ApiOperation(value = "添加讲师")
    @PostMapping("/add")
    public R add(@RequestBody EduTeacher eduTeacher){
        boolean saveSuccess = eduTeacherService.save(eduTeacher);
        return saveSuccess?R.ok():R.error();
    }

    @ApiOperation(value = "查询单个讲师")
    @GetMapping("find/{id}")
    public R find(@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return teacher==null? null :R.ok().data("teacher", teacher);
    }

    @ApiOperation(value = "修改讲师")
    @PutMapping("/update/{id}")
    public R update(@PathVariable String id, @RequestBody EduTeacher teacher){
        teacher.setId(id);
        boolean flag = eduTeacherService.updateById(teacher);
        return flag? R.ok(): R.error();
    }

}

