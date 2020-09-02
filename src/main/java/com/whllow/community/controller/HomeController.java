package com.whllow.community.controller;


import com.whllow.community.entity.DiscussPost;
import com.whllow.community.entity.Page;
import com.whllow.community.entity.User;
import com.whllow.community.service.DiscussPostService;
import com.whllow.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPages(Model model, Page page){

        //page是SpringMVC自动实例化，并将注入model中
        page.setPath("/index");
        page.setRows(discussPostService.FindDiscussPostRows(0));

        System.out.println("offset"+":"+page.getOffset());
        List<DiscussPost> list= discussPostService.FindDiscussPostLists(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost discussPost:list){
                Map<String,Object> map = new HashMap<>();
                map.put("post",discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user",user);
                discussPosts.add(map);

            }

            model.addAttribute("discussPosts",discussPosts);
        }


        return "index";
    }

}
