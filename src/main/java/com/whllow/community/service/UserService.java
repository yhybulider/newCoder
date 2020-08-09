package com.whllow.community.service;

import com.whllow.community.Dao.UserMapper;
import com.whllow.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User FindUserById(int id){
        return userMapper.selectById(id);
    }


}
