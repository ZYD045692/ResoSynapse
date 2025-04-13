package org.amm.config;  // 关键包路径

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.amm.dao") // 确保指向正确的Mapper接口包
public class MyBatisConfig {
    // 空类即可，不需要任何方法
}