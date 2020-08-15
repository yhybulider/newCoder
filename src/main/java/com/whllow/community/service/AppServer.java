package com.whllow.community.service;

import com.whllow.community.dao.hlloDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class AppServer {

    public AppServer(){
        System.out.println("构造AppServer");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化 Bean");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("销毁 Bean");
    }

    @Autowired
    private hlloDao dao;

    public String find(){
        return dao.select();
    }


}
