package com.whllow.community.controller;

import com.whllow.community.entity.CommunityConstant;
import com.whllow.community.entity.User;
import com.whllow.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

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



}
