package org.amm.controller;

import org.amm.config.FileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileConfig fileConfig;

    @RequestMapping("/upload")
    public Map<String, Object>  uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) String category) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 0. 校验文件是否为空
            if (file.isEmpty()) {
                logger.warn("上传文件为空");
                result.put("stateCode", 0);
                return result;
            }

            // 1. 获取配置的存储路径
            String baseDir = fileConfig.getLocation();
            logger.debug("存储根目录: {}", baseDir);

            // 2. 处理分类目录
            String categoryDir = (category != null && !category.trim().isEmpty()) ? category.trim() + "/" : "";


            // 4. 拼接完整路径（自动处理分隔符）
            Path dirPath = Paths.get(baseDir, categoryDir).normalize();
            logger.debug("目标目录: {}", dirPath);

            // 5. 创建目录（如果不存在）
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                logger.info("创建目录: {}", dirPath);
            }

            // 6. 生成安全文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = getFileSuffix(originalFilename);
            String filename = UUID.randomUUID() + suffix;
            String safeFilename = filename.replaceAll("[^a-zA-Z0-9.-]", "_"); // 过滤非法字符

            // 7. 保存文件
            Path filePath = dirPath.resolve(safeFilename);
            file.transferTo(filePath.toFile());
            logger.info("文件保存成功: {}", filePath.toAbsolutePath());

            // 8. 返回编码后的访问路径
            String encodedFilename = URLEncoder.encode(safeFilename, StandardCharsets.UTF_8);

            result.put("stateCode", 1);
            result.put("url", "/images/" + encodedFilename);

            return result;
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            result.put("stateCode", 0);
            return result;
        }
    }

    private String getFileSuffix(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}


