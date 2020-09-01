package com.whllow.community.controller;

import com.whllow.community.entity.DiscussPost;
import com.whllow.community.entity.User;
import com.whllow.community.service.DiscussPostService;
import com.whllow.community.service.UserService;
import com.whllow.community.util.CommunityUtil;
import com.whllow.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录");

        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    //根据id查询帖子详情   ?
    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDisscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        // 帖子
        DiscussPost post = discussPostService.findDisscussPostById(discussPostId);
        model.addAttribute("post",post);
        // 作者
        User user = userService.FindUserById(discussPostId);
        model.addAttribute("user",user);

        return "/site/discuss-detail";

    }
}
