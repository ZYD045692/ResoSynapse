package org.amm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.amm.utils.CaptchaUtil;

import javax.imageio.ImageIO;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class VerifyController {
    // 生成图片验证码
    @PostMapping("/captcha/image")
    public Map<String, Object> generateCaptcha(@RequestBody Map<String, Object> userJson, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码
        String code = CaptchaUtil.generateCode();

        // 存入session
        HttpSession session = request.getSession();

        session.setAttribute("captcha"+userJson.get("userId"), code);
        session.setMaxInactiveInterval(60); // 设置Session超时时间为1分钟

        // 设置响应头
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成图片并输出
        BufferedImage image = CaptchaUtil.generateImage(code);
        //ImageIO.write(image, "PNG", response.getOutputStream());

        // 将图片转换为 Base64 字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos); // 将图片写入字节流
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes); // 转换为 Base64 字符串

        Map<String, Object> map = new HashMap<>();
        map.put("image", base64Image);
        map.put("stateCode", 1);
        System.out.println("/captcha/image:");
        System.out.println(code);
        System.out.println(map);
        return map;
    }

    //验证图片验证码
    @PostMapping("/captcha/validate")
    public Map<String, Object> validateCaptcha(@RequestBody Map<String, Object> userVerifyJson, HttpServletRequest request) {
        String codeInput = (String) userVerifyJson.get("codeInput");
        int userId = (int) userVerifyJson.get("userId");

        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession(false);   //false 表示不创建新session
        if (session == null) {
            result.put("stateCode", 1);    //超时
            return result;
        }

        String savedCode = (String) session.getAttribute("captcha"+userId);
        if (codeInput != null && codeInput.equalsIgnoreCase(savedCode)){
            result.put("stateCode", 2);   // 验证成功
            session.removeAttribute("captcha"+userId);       // 验证成功后清除session中的验证码
//            session.invalidate();     // 销毁整个 Session
        } else {
            result.put("stateCode", 0);   // 验证码错误
        }

        return result;
    }
}
    

