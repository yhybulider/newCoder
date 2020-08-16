package com.whllow.community.controller.interceptor;

import com.whllow.community.entity.LoginTicket;
import com.whllow.community.entity.User;
import com.whllow.community.service.UserService;
import com.whllow.community.util.CookieUtil;
import com.whllow.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从Request中取出Cookie
        String ticket = CookieUtil.getCookie(request,"ticket");
        if(ticket!=null){
            //获取凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //判断凭证是否有效
            if(loginTicket!=null&&loginTicket.getStatus()==0
                    &&loginTicket.getExpired().after(new Date())){
                User user = userService.FindUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            hostHolder.clean();
    }
}
