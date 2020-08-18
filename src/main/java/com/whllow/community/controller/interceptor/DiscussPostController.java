package com.whllow.community.controller.interceptor;

import com.whllow.community.entity.DiscussPost;
import com.whllow.community.entity.User;
import com.whllow.community.service.DiscussPostServer;
import com.whllow.community.service.UserService;
import com.whllow.community.util.CommunityUtil;
import com.whllow.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostServer discussPostServer;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONSrting(403,"你还没有登录，请登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostServer.addDiscussPost(post);


        return CommunityUtil.getJSONSrting(0,"成功发布");

    }


    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){

        DiscussPost discussPost = discussPostServer.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);

        //查询作者
        User user  = userService.FindUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        return "site/discuss-detail";
    }





}
