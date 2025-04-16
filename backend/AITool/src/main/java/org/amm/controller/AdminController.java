package org.amm.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.amm.model.entity.Admin;
import org.amm.service.IAdminService;
import org.amm.service.IUserService;

import org.amm.utils.JwtUtils;
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
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 5_000; // 1 day
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 604_800_000;
    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Admin admin) {
        int stateCode = adminService.login(admin);
        Map map = new HashMap<String, Object>();
        if (stateCode == 2) {
            Admin adminLogined = adminService.findByAdminName(admin.getAdminName());
            String accessToken = JwtUtils.generateToken(admin.getAdminName(),ACCESS_TOKEN_EXPIRATION_TIME);
            String refreshToken = JwtUtils.generateToken(admin.getAdminName(),REFRESH_TOKEN_EXPIRATION_TIME);
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("stateCode", stateCode);
            map.put("adminId", adminLogined.getAdminId());
            map.put("level", adminLogined.getLevel());
            return map;
        } else {
            map.put("stateCode", stateCode);
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

    @RequestMapping(value="/refresh", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, Object> response = new HashMap<>();

        try {
            // 解析 refreshToken
            Claims claims = JwtUtils.parseToken(refreshToken);
            String username = claims.getSubject();

            // 验证用户是否存在
            Admin admin = adminService.findByAdminName(username);
            if (admin == null) {
                response.put("stateCode", 0);
                return response;
            }

            // 生成新 Token
            String newAccessToken = JwtUtils.generateToken(username, ACCESS_TOKEN_EXPIRATION_TIME);
//            String newRefreshToken = JwtUtils.generateToken(username, REFRESH_TOKEN_EXPIRATION_TIME);

            // 返回新 Token
            response.put("stateCode", 1);
            response.put("accessToken", newAccessToken);
//            response.put("refreshToken", newRefreshToken);

        } catch (ExpiredJwtException e) {
            //Refresh Token 已过期
            response.put("stateCode", 2);
        } catch (Exception e) {
            response.put("stateCode", 3);
        }
        return response;
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
