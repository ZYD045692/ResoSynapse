package org.amm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;


@Configuration
@ConfigurationProperties(prefix = "file-upload")
public class FileConfig {

    @Value("${file-upload.location}")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}