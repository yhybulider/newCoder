package com.whllow.community.util;

import com.whllow.community.entity.User;
import org.springframework.stereotype.Component;


/**
 * 持有用户信息
 * 用于储存user
 * 取代使用Session，在多线程中安全存储user
 * */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }

    public void clean(){
        users.remove();
    }


}
