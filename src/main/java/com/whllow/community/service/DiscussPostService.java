package com.whllow.community.service;

import com.whllow.community.dao.DiscussPostMapper;
import com.whllow.community.entity.DiscussPost;
import com.whllow.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> FindDiscussPostLists(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPost(userId,offset,limit);
    }

    public int FindDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    // 增加帖子的方法
    public int addDiscussPost(DiscussPost post){
        if (post == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        //转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        //过滤敏感次
        post.setTitle(sensitiveFilter.filiter(post.getContent()));
        post.setContent(sensitiveFilter.filiter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }
    //根据id查询帖子详情
    public DiscussPost findDisscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
}
