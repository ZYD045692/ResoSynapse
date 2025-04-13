package org.amm.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.amm.model.entity.Admin;
import org.amm.service.IAdminService;
import org.amm.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IUserService userService;

    //登录
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody Admin admin, HttpServletResponse response, HttpSession session) {
        Map map = new HashMap<String, Object>();
        int stateCode = adminService.login(admin);
        if (stateCode == 2) {
            Admin adminLogined = adminService.findByAdminName(admin.getAdminName());
            // 创建Session并存储用户信息
            session.setAttribute("admin", adminLogined.getAdminName());

            // 创建Cookie
            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            Cookie adminIdCookie = new Cookie("adminId", String.valueOf(adminLogined.getAdminId()));
            Cookie adminNameCookie = new Cookie("adminName", adminLogined.getAdminName());

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.addCookie(sessionCookie);
            response.addCookie(adminIdCookie);
            response.addCookie(adminNameCookie);

            map.put("stateCode", stateCode);
            map.put("adminId", adminLogined.getAdminId());
            map.put("level", adminLogined.getLevel());
            return map;
        } else {
            map.put("stateCode", stateCode);
            map.put("adminId", -1);
            map.put("level", -1);
            return map;
        }
    }

    // 注册
    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    public Map register(@RequestBody Admin admin) {
        Map map = new HashMap<>();
        int stateCode = adminService.login(admin);
        map.put("stateCode", stateCode);
        return map;
    }




//    //退出
//    @RequestMapping("/exit")
//    @ResponseBody
//    public void exit(HttpServletRequest request){
//        request.getSession().invalidate();  //销毁会话
//    }
//
//    //修改密码
//    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public Map updatePassword(@RequestBody Admin admin){
//        adminService.updateAdminPasswordHash(admin);
//        Map map = new HashMap<>();
//        map.put("msg", "修改密码成功，请重新登录！");
//        return map;
//    }
//
//    // 人脸验证
////    @RequestMapping("/faceVerify")
////    public Map faceVerify(HttpServletRequest request){
////
////    }
//
//    // 注销
//    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
//    @ResponseBody
//    public Map deleteAdmin(@RequestBody Admin admin){
//        adminService.deleteAdmin(admin);
//        Map map = new HashMap<>();
//        map.put("msg", "注销成功");
//        return map;
//    }
//
//
//
//    //查看用户列表
//    @RequestMapping(value = "/findUserList", method = RequestMethod.GET)
//    @ResponseBody
//    public List<User> findUserList() {
//        List<User> userList = userService.userList();
//        return userList;
//    }
//
//
}
