package com.whllow.community.dao;

import com.whllow.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPost(int userId,int offset,int limit);

    //@param()这个注解用于给参数起别名
    //在使用动态拼接（使用<if>）的SQL语句   并且方法参数有且只有一个时，一定要起别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);
    // 根據id來查询帖子详情
    DiscussPost selectDiscussPostById(int id);

}
