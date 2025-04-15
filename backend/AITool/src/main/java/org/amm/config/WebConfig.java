package org.amm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileConfig fileConfig;

    @Autowired
    public WebConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取配置的存储路径并转换为URI格式
        Path locationPath = Paths.get(fileConfig.getLocation()).toAbsolutePath().normalize();
        String locationUri = locationPath.toUri().toString();

        // 注册静态资源映射
        registry.addResourceHandler("/images/**")
                .addResourceLocations(locationUri);
    }
}