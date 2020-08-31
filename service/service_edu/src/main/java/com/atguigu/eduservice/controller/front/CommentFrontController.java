package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.MemberInfoVo;
import com.atguigu.eduservice.client.UCenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author ljd
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/eduservice/comment")
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UCenterClient uCenterClient;

    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("/{current}/{size}")
    public R index(@PathVariable Long current, @PathVariable Long size, String courseId) {
        Page<EduComment> page = new Page<>(current, size);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);

        commentService.page(page,wrapper);
        List<EduComment> commentList = page.getRecords();
        //获取分页数据
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());

        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(memberId);

        MemberInfoVo memberInfo = uCenterClient.getMemberInfo(memberId);

        comment.setNickname(memberInfo.getNickname());
        comment.setAvatar(memberInfo.getAvatar());

        commentService.save(comment);
        return R.ok();
    }
}
