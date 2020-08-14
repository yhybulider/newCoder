package com.whllow.community.controller;

import com.whllow.community.Dao.DiscussPostMapper;
import com.whllow.community.Dao.UserMapper;
import com.whllow.community.entity.DiscussPost;
import com.whllow.community.entity.User;
import com.whllow.community.service.AppServer;
import com.whllow.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/hello")
public class appController {


    @RequestMapping("/easy")
    @ResponseBody
    public String handle(){
        return "hello world.";
    }

    @Autowired
    private AppServer appServer;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @RequestMapping("/list")
    @ResponseBody
    public String handleList(){
        List<DiscussPost> list =discussPostMapper.selectDiscussPost(149,0,10);
        for(DiscussPost discussPost:list){
            System.out.println(discussPost);
        }
        return "List succuss";
    }

    @RequestMapping("/Rows")
    @ResponseBody
    public String handleRows(){
        System.out.println(discussPostMapper.selectDiscussPostRows(149));
        return discussPostMapper.selectDiscussPostRows(0)+"";
    }

    @RequestMapping("/select")
    @ResponseBody
    public String handleSelect(){
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByEmail("nowcoder124@sina.com");
        System.out.println(user);

        user = userMapper.selectByName("ooo");
        System.out.println(user);
        return appServer.find();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public String handleInsert(){
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setSalt("bbf47");
        user.setCreateTime(new Date());
        user.setEmail("haha@qq.com");
        user.setActivationCode("FFFFFF");
        user.setHeaderUrl("http://images.nowcoder.com/head/506t.png");
        user.setStatus(1);
        user.setType(1);
        userMapper.insertUser(user);

        return "insert success";
    }
    @RequestMapping("/update")
    @ResponseBody
    public String handleUpdate(){
        userMapper.updateHeader(101,"http://images.nowcoder.com/head/506t.png");
        userMapper.updatePassword(101,"123546");
        userMapper.updateStatus(101,1);
    return "update success";
    }



    @RequestMapping("/Auto")
    @ResponseBody
    public String handle1(){
        return appServer.find();
    }

    @RequestMapping("/http")
    public void handleHttp(HttpServletRequest request, HttpServletResponse response){

        System.out.println(request.getMethod());
        System.out.println(request.getCookies());
        System.out.println(request.getContextPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
           String name =  enumeration.nextElement();
           String value = request.getHeader(name);
            System.out.println(name +" : "+value);
        }
        System.out.println(request.getParameter("code"));
        response.setContentType("text/html;character=utf-8");
        //响应
        try(
                PrintWriter writer = response.getWriter();
                ){
            writer.write("<h1>whllow</h1>");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String handleRequestParam(
         @RequestParam(name = "current",required = false,defaultValue = "1")int current,
         @RequestParam(name="limit",required = false,defaultValue = "10") int limit
    ){
        System.out.println(current);
        System.out.println(limit);
        return "whllow world";
    }

    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String handleEPL(@PathVariable("id") int id){
        System.out.println(id);
        return "whllwo";
    }

    @RequestMapping(path = "/stu" , method = RequestMethod.POST)
    @ResponseBody
    public String handle(String name,int age){
        System.out.println(name+": "+age);
        return "a student";
    }

    @RequestMapping(path = "/student",method = RequestMethod.GET)

    public ModelAndView getStudent(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age","18");
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","李老师");
        model.addAttribute("age","65");
        return "/demo/view";
    }
    @RequestMapping(path = "/emp" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",18);
        emp.put("salary",2000);
        return emp;
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",18);
        emp.put("salary",2000);
        list.add(emp);

        Map<String,Object> emp2 = new HashMap<>();
        emp2.put("name","李四");
        emp2.put("age",28);
        emp2.put("salary",12000);
        list.add(emp2);

        Map<String,Object> emp3 = new HashMap<>();
        emp3.put("name","王五");
        emp3.put("age",108);
        emp3.put("salary",222);
        list.add(emp3);

        Map<String,Object> emp4 = new HashMap<>();
        emp4.put("name","赵六");
        emp4.put("age",70);
        emp4.put("salary",20400);
        list.add(emp4);
        return list;
    }

    @Autowired
    private MailClient mailClient;

    @RequestMapping("/sendMail")
    public String send(Map<String,String> map){
        mailClient.sendMail("1342346379@qq.com","test","JavaEmailSend");
        map.put("username","haha");
        return "/mail/demo";
    }

    @RequestMapping("/SetCookies")
    @ResponseBody
    public String SetCookies(HttpServletResponse response){

        //创建一个cookie，一个cookie只有一个键值对
        Cookie cookie = new Cookie("code",UUID.randomUUID().toString());
        //设置什么样式的请求才能获取cookie
        cookie.setPath("/community/hello");
        //设置cookie最大生存时间
        cookie.setMaxAge(60*10);
        //向响应头添加cookie
        response.addCookie(cookie);

        return "succeed in Setting";
    }

    @RequestMapping(path = "/GetCookies",method = RequestMethod.GET)
    @ResponseBody
     public String GetCookies(@CookieValue("code") String code)
    {
        System.out.println(code);
        return code;
    }

    @RequestMapping(path = "/SetSession",method = RequestMethod.GET)
    @ResponseBody
    public String SetSession(HttpSession session){
        session.setAttribute("id",11);
        session.setAttribute("name","test");
        return "succeed in set session";
    }



    @RequestMapping(path = "/GetSession",method = RequestMethod.GET)
    @ResponseBody
    public String GetSession(HttpSession session)
    {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "succeed in get session";
    }


}
