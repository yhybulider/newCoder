package com.whllow.community.controller;

import com.whllow.community.annotation.LoginRequired;
import com.whllow.community.entity.User;
import com.whllow.community.service.FollowService;
import com.whllow.community.service.LikeService;
import com.whllow.community.service.UserService;
import com.whllow.community.util.CommunityUtil;
import com.whllow.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.whllow.community.util.CommunityConstant.ENTITY_TYPE_USER;

// 设置用户信息功能的，包括设置头像等，换头像
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.uploadPath}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }


    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerFile, Model model){
        //判断文件是否为空
        if(headerFile==null){
            model.addAttribute("error","文件为空");
            return "/site/setting";
        }

        //获取文件的后缀名
        String filename =  headerFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","上传格式有误");
            return "/site/setting";
        }

        //上传文件
        filename = CommunityUtil.generateUUID() + suffix;
        File file = new File(uploadPath + "/" + filename);
        try {
            headerFile.transferTo(file);
        } catch (IOException e) {
            logger.error("上传失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器出现异常" + e.getMessage());
        }

        //更新头像的路径
        //http://localhost:8081/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath+ "/user/header/" + filename;
        userService.uploadHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }
// 向浏览器响应图片
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){

        fileName = uploadPath + "/" +fileName;
        // 获取文件名字
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try(
                InputStream is = new BufferedInputStream(new FileInputStream(fileName));
                ) {
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b=is.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("文件获取失败" + e.getMessage());
        }


    }

    @LoginRequired
    @RequestMapping(path = "/updatePassword",method = RequestMethod.POST)
    public String updatePassword(Model model,
            String oldPassword,String newPassword, String againPassword){

    //空值判断
    if(StringUtils.isBlank(oldPassword)){
        model.addAttribute("oldPasswordErr","旧密码不能为空");
        return "/site/setting";
    }
    if(StringUtils.isBlank(newPassword)){
        model.addAttribute("newPasswordErr","新密码不能为空");
        return "/site/setting";
    }
    if(StringUtils.isBlank(againPassword)){
        model.addAttribute("againPasswordErr","再次确定密码不能为空");
        return "/site/setting";
    }


    User user = hostHolder.getUser();
    oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
    if(!oldPassword.equals(user.getPassword())){
        model.addAttribute("oldPasswordErr","与原始密码不一致");
        return "/site/setting";
    }
    if(!newPassword.equals(againPassword)){
        model.addAttribute("againPasswordErr","再次确定密码与新密码不一致");
        return "/site/setting";
    }

    userService.updatePassword(user.getId(),newPassword);
    return "redirect:/index";


    }


    // 个人主页
    // 进入到个人主页 查询各种信息，点赞 关注 粉丝
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }




}
