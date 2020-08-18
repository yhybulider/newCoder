package com.whllow.community.service;

import com.whllow.community.dao.DiscussPostMapper;
import com.whllow.community.entity.DiscussPost;
import com.whllow.community.util.SensitiveFiliter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServer {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFiliter sensitiveFiliter;

    public List<DiscussPost> FindDiscussPostLists(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPost(userId,offset,limit);
    }

    public int FindDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost discussPost){
        if(discussPost==null){
            throw new RuntimeException("消息不能为空");
        }

        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        discussPost.setTitle(sensitiveFiliter.Filiter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFiliter.Filiter(discussPost.getContent()));


        return  discussPostMapper.insertDiscussPost(discussPost);
    }

    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }



}
