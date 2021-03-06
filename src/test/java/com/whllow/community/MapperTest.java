package com.whllow.community;

import com.whllow.community.dao.LoginTicketMapper;
import com.whllow.community.dao.UserMapper;
import com.whllow.community.entity.LoginTicket;
import com.whllow.community.entity.User;
import com.whllow.community.util.MailClient;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
//template是直接封装在spring组件中的
    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    private MailClient mailClient;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Test
    public void testTextMail(){
        //对应参数
        mailClient.sendMail("1065205068@qq.com", "TEST", "Welcome you");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","user");
//使用的是模板引擎搞的--template
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        //目标邮箱，标题，内容
        mailClient.sendMail("1065205068@qq.com","标题",content);
    }

    @Test
    public void testLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("yhy");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);

    }






}
