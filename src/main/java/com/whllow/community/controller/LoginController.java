package com.whllow.community.controller;

import com.google.code.kaptcha.Producer;
import com.whllow.community.entity.CommunityConstant;
import com.whllow.community.entity.User;
import com.whllow.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/register" ,method = RequestMethod.GET)
    public String getRegisterPage(){
        return "site/register";
    }

    @RequestMapping(path = "/login" ,method = RequestMethod.GET)
    public String getLoginPage(){

        return "site/login";
    }
//注册功能实现
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if(map==null||map.isEmpty()){
                model.addAttribute("msg","你已经注册成功，稍后有一封邮件发送到你的邮箱中，请尽快激活");
                model.addAttribute("target","/Mybaits/index");
                return "/site/operate-result";
        }else{
            model.addAttribute("userNameMsg",map.get("userNameMsg"));
            System.out.println(map.get("userNameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "site/register";
        }

    }
    //http://localhost:8081/community/activation/155/5dccbcf3ed4f4fdbb97d5f99a71cae16
    @RequestMapping(path= "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code")String code){
        int result = userService.activation(userId,code);
        if(result==CommunityConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","你已经成功激活");
            model.addAttribute("target","/login");
        }else if(result==CommunityConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","激活失败，该账号已经激活了");
            model.addAttribute("target","/Mybaits/index");
        }else{
            model.addAttribute("msg","激活失败，激活码有误");
            model.addAttribute("target","/Mybaits/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //创建一个验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存储在服务器中
        session.setAttribute("kaptcha",text);
        System.out.println(text);
        //输出图片到服务器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("服务器响应验证码出错"+e.getMessage());
        }


    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(Model model,
                        String username,String password,String code,boolean rememberme,
                        HttpSession session, HttpServletResponse response
    ){
        String kaptcha = (String)session.getAttribute("kaptcha");
        if(StringUtils.isBlank(code)||StringUtils.isBlank(kaptcha)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        int expiredSeconds = rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map = userService.login(username,password,expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",(String)map.get("ticket"));
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/Mybaits/index";
        }else{
            model.addAttribute("userNameMsg",map.get("userNameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }



    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }



}
