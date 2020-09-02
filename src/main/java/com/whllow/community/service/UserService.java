package com.whllow.community.service;

import com.whllow.community.dao.LoginTicketMapper;
import com.whllow.community.dao.UserMapper;
import com.whllow.community.entity.CommunityConstant;
import com.whllow.community.entity.LoginTicket;
import com.whllow.community.entity.User;
import com.whllow.community.util.CommunityUtil;
import com.whllow.community.util.HostHolder;
import com.whllow.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {


// dao层对象

    @Autowired
    private UserMapper userMapper;
    // 邮件客户端
    @Autowired
    private MailClient mailClient;
    // 模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    // 设置一个map用来储存user信息--注册功能
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        //空值判断
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("userNameMsg", "userName不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "password不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "email不能为空");
            return map;
        }

        //验证用户的账号，用来提示
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("userNameMsg", "用户名已存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "邮箱已存在");
            return map;
        }

        //初始化用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        // 随机选取牛客的头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 用户激活信息，呈现在邮件中
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        // 利用模板引擎来生成邮件内容
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "activation", content);

        return map;
    }

    //
    //激活码查询，错或者对都应该有状态码返回
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        // 激活码=1，表示激活过了
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            // 激活码没被激活，设置=1，返回成功信息
            userMapper.updateStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {

        Map<String, Object> map = new HashMap<>();
        //空值判断
        if (StringUtils.isBlank(username)) {
            map.put("userNameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("userNameMsg", "账号不存在");
            return map;
        }
        //验证是否激活
        if (user.getStatus() == 0) {
            map.put("userNameMsg", "账号未激活");
            return map;
        }
        //验证密码
        if (!CommunityUtil.md5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("passwordMsg", "密码有误");
            return map;
        }
        //生成凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        // 当前时间往后推
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int uploadHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }


    public int updatePassword(int userId, String password) {
        User user = userMapper.selectById(userId);
        String salt = user.getSalt();
        password = password + salt;
        return userMapper.updatePassword(userId, CommunityUtil.md5(password));
    }

}







