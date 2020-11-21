package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.pojo.AdminUser;
import com.site.blog.my.core.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户登陆、登出以及修改信息功能
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;


    /**
    * @Author Wczy
    * @Date 2020-11-18 9:40
    * @Param []
    * @Return java.lang.String
    * @description 跳转到登陆界面
    **/
    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:15
    * @Param [request]
    * @Return java.lang.String
    * @description 跳转到主页面
    **/
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        //查询分类总数
        request.setAttribute("categoryCount", categoryService.getTotalCategories());
        //查询博客总数
        request.setAttribute("blogCount", blogService.getTotalBlogs());
        //查询链接总数
        request.setAttribute("linkCount", linkService.getTotalLinks());
        //查询标签总数
        request.setAttribute("tagCount", tagService.getTotalTags());
        //查询评论总数
        request.setAttribute("commentCount", commentService.getTotalComments());
        return "admin/index";
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:09
    * @Param [userName, password, verifyCode, session]
    * @Return java.lang.String
    * @description 登陆验证
    **/
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        //验证码是否正确，Object转为String，使用这种方式不必担心obj为空
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        //从数据库获取用户信息
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null) {
            //adminUser对象不为空，则登陆成功，将用户昵称和用户id存入session
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            //重定向到管理首页
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "用户名或密码错误");
            return "admin/login";
        }
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:18
    * @Param [request]
    * @Return java.lang.String
    * @description 跳转到修改密码界面
    **/
    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        //验证是否为当前用户
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        //保存路径信息，用于导航栏高亮显示
        request.setAttribute("path", "profile");
        //保存用户名和昵称
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:54
    * @Param [request, originalPassword, newPassword]
    * @Return java.lang.String
    * @description 修改密码
    **/
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:54
    * @Param [request, loginUserName, nickName]
    * @Return java.lang.String
    * @description 修改昵称和登陆名称
    **/
    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
    * @Author Wczy
    * @Date 2020-11-18 10:54
    * @Param [request]
    * @Return java.lang.String
    * @description 登出功能
    **/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //清楚session中的用户id、登陆用户名以及errorMsg，然后跳转到登陆页面
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
