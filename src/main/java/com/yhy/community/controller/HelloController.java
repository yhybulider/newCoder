package com.yhy.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @Author: yhy
 * @Date: 2020/6/16
 * @Time: 11:17
 */
//controller注解
@Controller
@RequestMapping("/hello")
public class HelloController {
    //映射
    @RequestMapping("/say")
    @ResponseBody
    public String sayHello() {
        return "Hello springboot!";
    }
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            String header = request.getHeader(s);
            System.out.println(s + " : " + header);

        }
        System.out.println(request.getParameter("code"));

        //返回响应数据.
        response.setContentType("text/html; charset=UTF-8");
        try{
            PrintWriter writer = response.getWriter();
            {
                writer.write("<h1> 颜华艺 </h1>");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @RequestMapping(path = "/submit",method = RequestMethod.POST)
    @ResponseBody
    public String submit(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";

    }

}
