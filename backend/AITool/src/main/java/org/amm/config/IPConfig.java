package org.amm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPConfig {
    @Value("${ip.path}")
    private String ipPath;

    public String getWholeIp(String inputUrl) {
        return ipPath + inputUrl;
    }
    public String getIp() {
        return ipPath;
    }
}