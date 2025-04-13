package org.amm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import org.amm.model.entity.User;
import org.amm.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody User user, HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String, Object> map = userService.login(user);
        if ((int) map.get("stateCode") == 2) {
            User userLogined = userService.findByUserName(user.getUserName());
            // 创建Session并存储用户信息
            session.setAttribute("user", userLogined.getUserName());

            // 创建Cookie保存Session ID
            Cookie userIdCookie = new Cookie("userId", String.valueOf(userLogined.getUserId()));
            Cookie userNameCookie = new Cookie("userName", userLogined.getUserName());
            Cookie profilePhotoUrlCookie = new Cookie("profilePhotoUrl", URLEncoder.encode(userLogined.getProfilePhotoUrl(), StandardCharsets.UTF_8));

            // 设置HttpOnly为false
            userIdCookie.setHttpOnly(false);
            userNameCookie.setHttpOnly(false);
            profilePhotoUrlCookie.setHttpOnly(false);

            // 设置Cookie的路径
            userIdCookie.setPath("/");
            userNameCookie.setPath("/");
            profilePhotoUrlCookie.setPath("/");

            // 添加Cookie到响应
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.addCookie(userIdCookie);
            response.addCookie(userNameCookie);
            response.addCookie(profilePhotoUrlCookie);

            return map;
        }
        return map;
    }
//    public Map<String, Object> login(@RequestBody User user, HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException {
//        Map<String, Object> map = userService.login(user);
//        if((int) map.get("stateCode")==2){
//            User userLogined = userService.findByUserName(user.getUserName());
//            // 创建Session并存储用户信息
//            session.setAttribute("user", userLogined.getUserName());
//
//            // 创建Cookie保存Session ID
//            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
//            Cookie userIdCookie = new Cookie("userId", String.valueOf(userLogined.getUserId()));
//            Cookie userNameCookie = new Cookie("userName", userLogined.getUserName());
//            Cookie profilePhotoUrlCookie = new Cookie("profilePhotoUrl", URLEncoder.encode(userLogined.getProfilePhotoUrl(), StandardCharsets.UTF_8));
//
//            System.out.println(URLEncoder.encode(userLogined.getProfilePhotoUrl(), StandardCharsets.UTF_8));
//            System.out.println(userLogined.getProfilePhotoUrl());
//
//            response.addCookie(sessionCookie);
//            response.addCookie(userIdCookie);
//            response.addCookie(userNameCookie);
//            response.addCookie(profilePhotoUrlCookie);
//            return map;
//        }
//        return map;
//    }


    //注册
    @RequestMapping(value="/register",method=RequestMethod.POST)
    @ResponseBody
    public Map register(@RequestBody User user) throws Exception{
        Map map = new HashMap<String, Object>();
        int stateCode = userService.register(user);
        map.put("stateCode", stateCode);
        return map;
    }




//    //退出
//    @RequestMapping("/exit")
//    public void exit(HttpServletRequest request){
//        request.getSession().invalidate();  //销毁会话
//    }
//
//    //修改密码
//    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public Map updatePassword(@RequestBody User user){
//
//        userService.updatePassword(user);
//        Map map = new HashMap<>();
//        map.put("msg", "修改密码成功，请重新登录！");
//        return map;
//    }

//    //修改邮箱
//    @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
//    @ResponseBody
//    public Map updateEmail(@RequestBody User user){
//        user = userService.login(user);
//        Map map = new HashMap<>();
//        if(user != null){
//            userService.updateEmail(user);
//            map.put("msg", "修改邮箱成功");
//            map.put("code", "0");
//            return map;
//        } else {
//            map.put("msg", "用户名或密码不正确，修改失败");
//            map.put("code", "1");
//            return map;
//        }
//
//    }

//    // 注销
//    @RequestMapping(value="/deleteUser", method = RequestMethod.POST)
//    @ResponseBody
//    public Map deleteUser(@RequestBody User user){
//        userService.deleteUser(user);
//        Map map = new HashMap<>();
//        map.put("msg", "注销成功");
//        return map;
//    }

    
//    //test
//    @RequestMapping("/test")
//    @ResponseBody
//    public String test() {
//        return "success";
//    }


}
