package com.whllow.community.service;

import com.whllow.community.dao.DiscussPostMapper;
import com.whllow.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServer {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> FindDiscussPostLists(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPost(userId,offset,limit);
    }

    public int FindDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
