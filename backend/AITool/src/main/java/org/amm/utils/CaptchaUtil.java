package org.amm.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {
    // 验证码字符集
    private static final char[] CHARS = {
            '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int IMAGE_WIDTH = 120;
    // 图片高度
    private static final int IMAGE_HEIGHT = 40;
    // 干扰线数量
    private static final int LINE_COUNT = 20;

    /**
     * 生成随机验证码
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    /**
     * 生成验证码图片
     */
    public static BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 绘制字符
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandomColor());
            g.setFont(new Font("微软雅黑", Font.BOLD, 30));
            g.drawString(String.valueOf(code.charAt(i)), 20 + i * 25, 30);
        }

        // 绘制干扰线
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(getRandomColor());
            int x1 = new Random().nextInt(IMAGE_WIDTH);
            int y1 = new Random().nextInt(IMAGE_HEIGHT);
            int x2 = new Random().nextInt(IMAGE_WIDTH);
            int y2 = new Random().nextInt(IMAGE_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();
        return image;
    }

    /**
     * 获取随机颜色
     */
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(
                random.nextInt(200),
                random.nextInt(200),
                random.nextInt(200)
        );
    }
}