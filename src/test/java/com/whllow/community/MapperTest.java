package com.whllow.community;

import com.whllow.community.dao.UserMapper;
import com.whllow.community.entity.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectId(){
        User user = userMapper.selectById(101);
        System.out.println(user);
    }



}
